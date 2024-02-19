package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.timedata.db.RoomTimeKeeper;
import edu.ucsd.cse110.successorator.timedata.db.SuccessoratorTimeDatabase;

public class SuccessoratorApplication extends Application {
    private DataSource dataSource;
    private GoalRepository goalRepositoryComplete;

    private GoalRepository goalRepositoryIncomplete;

    private TimeKeeper timeKeeper;

    @Override
    public void onCreate(){
        super.onCreate();

// Pre persistence model
//        this.dataSource = new DataSource();
//        this.goalRepository= new SimpleGoalRepository(dataSource);

        var database = Room.databaseBuilder(getApplicationContext(),
                SuccessoratorDatabase.class, "successorator-database")
                .allowMainThreadQueries()
                .build();
        this.goalRepositoryComplete = new RoomGoalRepository(database.goalDao());

        var database2 = Room.databaseBuilder(getApplicationContext(),
                        SuccessoratorDatabase.class, "successorator-database2")
                .allowMainThreadQueries()
                .build();
        this.goalRepositoryIncomplete = new RoomGoalRepository(database2.goalDao());

        var database3 = Room.databaseBuilder(getApplicationContext(),
                        SuccessoratorTimeDatabase.class, "successorator-database3")
                .allowMainThreadQueries()
                .build();
        this.timeKeeper = new RoomTimeKeeper(database3.timeDao());

        // can use default goals to test
        var sharedPreferences = getSharedPreferences("Successorator", MODE_PRIVATE);

        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if (isFirstRun && database3.timeDao().count() == 0){
            timeKeeper.setDateTime(LocalDateTime.of(1900, 1, 20, 10, 30));

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public GoalRepository getGoalRepositoryComplete(){
        return goalRepositoryComplete;
    }

    public GoalRepository getGoalRepositoryIncomplete(){
        return goalRepositoryIncomplete;
    }

    public TimeKeeper getTimeKeeper() {
        return timeKeeper;
    }
}
