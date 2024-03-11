

package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;


import javax.xml.crypto.Data;

import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {

    private DataSource dataSource;

    public SimpleGoalRepository(DataSource dataSource) { this.dataSource = dataSource; }

    public DataSource getDataSource() { return this.dataSource; }
    @Override
    public Subject<Goal> find(int id) {
        return (Subject<Goal>) dataSource.getGoalEntrySubject(id);
    }

    @Override
    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalEntrySubject();
    }

    @Override
    public Subject<List<Goal>> getPendingGoals() {
        return null;
    }

    @Override
    public Subject<List<Goal>> getRecurringGoals() {
        return null;
    }

    @Override
    public Subject<List<Goal>> getGoalsByDay(int year, int month, int day) {
        return null;
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsByDay(int year, int month, int day) {
        return null;
    }

    @Override
    public Subject<List<Goal>> getGoalsLessThanOrEqualToDay(int year, int month, int day) {
        return null;
    }

    @Override
    public void remove(int id) {
        dataSource.removeGoal(id);
    }

    @Override
    public void append(Goal goal) {
        dataSource.putGoalEntry(goal.withSortOrder(dataSource.getMaxSortOrder() + 1));
    }

    @Override
    public void prepend(Goal goal) {
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putGoalEntry(
                goal.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }

    @Override
    public void deleteCompleted() {
        for (Goal goal : dataSource.getGoals()) {
            if (goal.isComplete()) {
                if (goal.id() != null) {
                    dataSource.removeGoal(goal.id());
                }
            }
        }
    }
}