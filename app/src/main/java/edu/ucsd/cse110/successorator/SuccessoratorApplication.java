package edu.ucsd.cse110.successorator;

import android.app.Application;

import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;

public class SuccessoratorApplication extends Application {
    private DataSource dataSource;
    private GoalRepository goalRepository;

    @Override
    public void onCreate(){
        super.onCreate();


        this.dataSource = DataSource.fromDefault();
        this.goalRepository= new SimpleGoalRepository(dataSource);
    }

    public GoalRepository getGoalRepository(){
        return goalRepository;
    }
}
