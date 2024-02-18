package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;

public class SuccessoratorApplication extends Application {
    private DataSource dataSource;
    private GoalRepository goalRepository;

    @Override
    public void onCreate(){
        super.onCreate();

// Pre persistence model
//        this.dataSource = DataSource.fromDefault();
//        this.goalRepository= new SimpleGoalRepository(dataSource);

        var database = Room.databaseBuilder(getApplicationContext(),
                SuccessoratorDatabase.class, "successorator-database")
                .allowMainThreadQueries()
                .build();
        this.goalRepository = new RoomGoalRepository(database.goalDao());

        // can use default goals to test
//        var sharedPreferences = getSharedPreferences("Successorator", MODE_PRIVATE);
//        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
//        if (isFirstRun && database.goalDao().count() == 0){
//            goalRepository.save(DataSource.DEFAULT_GOALS);
//
//            sharedPreferences.edit()
//                    .putBoolean("isFirstRun", false)
//                    .apply();
//        }
    }

    public GoalRepository getGoalRepository(){
        return goalRepository;
    }
}
