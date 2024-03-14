package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.context.db.RoomContextRepository;
import edu.ucsd.cse110.successorator.context.db.SuccessoratorContextDatabase;
import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.ContextRepository;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.timedata.db.RoomTimeKeeper;
import edu.ucsd.cse110.successorator.timedata.db.SuccessoratorTimeDatabase;

/**
 * The Successorator Application
 */
public class SuccessoratorApplication extends Application {
    private GoalRepository goalRepositoryComplete;

    private GoalRepository goalRepositoryIncomplete;

    private GoalRepository goalRepositoryRecurring;

    private TimeKeeper timeKeeper;

    private ContextRepository contextRepository;

    private LocalDateTime todayTime;

    /**
     * Initializes the databases for the application upon application creation
     */
    @Override
    public void onCreate(){
        super.onCreate();


        // Initialize the database for the repository of complete goals
        var database = Room.databaseBuilder(getApplicationContext(),
                SuccessoratorDatabase.class, "successorator-database")
                .allowMainThreadQueries()
                .build();
        this.goalRepositoryComplete = new RoomGoalRepository(database.goalDao());

        // Initialize the database for the repository of incomplete goals
        var database2 = Room.databaseBuilder(getApplicationContext(),
                        SuccessoratorDatabase.class, "successorator-database2")
                .allowMainThreadQueries()
                .build();
        this.goalRepositoryIncomplete = new RoomGoalRepository(database2.goalDao());

        // Initialize the database for keeping track of the time the app was last previously opened
        var database3 = Room.databaseBuilder(getApplicationContext(),
                        SuccessoratorTimeDatabase.class, "successorator-database3")
                .allowMainThreadQueries()
                .build();
        this.timeKeeper = new RoomTimeKeeper(database3.timeDao());

        var database4 = Room.databaseBuilder(getApplicationContext(),
                        SuccessoratorDatabase.class, "successorator-database4")
                .allowMainThreadQueries()
                .build();
        this.goalRepositoryRecurring = new RoomGoalRepository(database4.goalDao());

        var database5 = Room.databaseBuilder(getApplicationContext(),
                SuccessoratorContextDatabase.class, "successorator-database5")
                .allowMainThreadQueries()
                .build();
        this.contextRepository = new RoomContextRepository(database5.contextDao());

        // Initalize a default value into the time keeper database
        var sharedPreferences = getSharedPreferences("Successorator", MODE_PRIVATE);

        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if (isFirstRun && database3.timeDao().count() == 0 && database5.contextDao().count() == 0){
            timeKeeper.setDateTime(LocalDateTime.of(1900, 1, 20, 10, 30));
            contextRepository.setContext(0);


            // might be able to use sharedPreferences to edit the issue with resetting date.
            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
        this.todayTime = LocalDateTime.now();
    }

    /**
     * Getter for the repository for complete goals
     *
     * @return The repository for incomplete goals
     */
    public GoalRepository getGoalRepositoryComplete(){
        return goalRepositoryComplete;
    }

    /**
     * Getter for the repository for incomplete goals
     *
     * @return The repository for incomplete goals
     */
    public GoalRepository getGoalRepositoryIncomplete(){
        return goalRepositoryIncomplete;
    }

    public GoalRepository getGoalRepositoryRecurring(){ return goalRepositoryRecurring;}

    /**
     * Getter for the app's TimeKeeper
     *
     * @return The TimeKeeper of the app
     */
    public TimeKeeper getTimeKeeper() {
        return timeKeeper;
    }

    public ContextRepository getContextRepository() {
        return contextRepository;
    }

    public LocalDateTime getTodayTime(){ return todayTime;}
}
