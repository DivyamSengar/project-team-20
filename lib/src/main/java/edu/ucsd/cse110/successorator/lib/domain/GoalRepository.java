package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;


import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class GoalRepository {

    // main backing database/datasource
    private DataSource dataSource;

    // base constructor
    public GoalRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }

    // DataSource getter used for testing
    protected DataSource getDataSource(){
        return this.dataSource;
    }
    // Potentially useful for UI Implementation
    public Integer count() {return dataSource.getGoals().size();}

    // getting a SimpleSubject to a goal based on its id
    public SimpleSubject<Goal> find(int id){
        return dataSource.getGoalEntrySubject(id);
    }

    // get all Goals, Incomplete and Complete
    public SimpleSubject<List<Goal>> findAll() {
        return dataSource.getAllGoalEntrySubject();
    }

    // add a goal to the list
    public void save(Goal goal) {
        dataSource.putGoalEntry(goal);
    }
}
