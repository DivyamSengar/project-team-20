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
    public Subject<Goal> find(int id){
        return dataSource.getGoalEntrySubject(id);
    }

    // get all Goals, Incomplete and Complete
    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalEntrySubject();
    }

    // add a goal to the list
    public void save(Goal goal) {
        dataSource.putGoalEntry(goal);
    }

    // adds a set of goals to the list
    public void save(List<Goal> goals) {dataSource.putGoalEntries(goals);}

    // markAsIncomplete
    public void remove(int id){
        dataSource.removeGoal(id);
    }

    /*
    WILL BE USEFUL FOR SHIFTING GOALS, but not exactly these methods
     */
    public void append(Goal goal){
        dataSource.putGoalEntry(
                goal.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    public void prepend(Goal goal){
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putGoalEntry(
                goal.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }
}
