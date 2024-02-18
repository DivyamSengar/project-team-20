package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;


import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {

    // main backing database/datasource
    private DataSource dataSource;

    // base constructor
    public SimpleGoalRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }

    // DataSource getter used for testing
    public DataSource getDataSource(){
        return this.dataSource;
    }
    // Potentially useful for UI Implementation
    @Override
    public Integer count() {return dataSource.getGoals().size();}

    // getting a SimpleSubject to a goal based on its id
    @Override
    public Subject<Goal> find(int id){
        return (Subject<Goal>) dataSource.getGoalEntrySubject(id);
    }

    // get all Goals, Incomplete and Complete
    @Override
    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalEntrySubject();
    }

    // add a goal to the list
    @Override
    public void save(Goal goal) {
        dataSource.putGoalEntry(goal);
    }

    // adds a set of goals to the list
    @Override
    public void save(List<Goal> goals) {dataSource.putGoalEntries(goals);}

    // markAsIncomplete
    @Override
    public void remove(int id){
        dataSource.removeGoal(id);
    }

    /*
    WILL BE USEFUL FOR SHIFTING GOALS, but not exactly these methods
     */
    @Override
    public void append(Goal goal){
        dataSource.putGoalEntry(
                goal.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    @Override
    public void prepend(Goal goal){
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putGoalEntry(
                goal.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }
}
