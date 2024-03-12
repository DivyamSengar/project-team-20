

package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


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
        // Retrieve all goals from the data source
        List<Goal> allGoals = dataSource.getGoals();

        // Filter out pending goals
        List<Goal> pendingGoals = allGoals.stream()
                .filter(goal -> !goal.isComplete())
                .collect(Collectors.toList());

        // Create a SimpleSubject to hold the list of pending goals
        SimpleSubject<List<Goal>> pendingGoalsSubject = new SimpleSubject<>();

        // Set the value of the SimpleSubject to the list of pending goals
        pendingGoalsSubject.setValue(pendingGoals);

        return pendingGoalsSubject;
//        return null;
    }

    @Override
    public Subject<List<Goal>> getRecurringGoals() {
        // Retrieve the goals from the data source
        List<Goal> allGoals = dataSource.getGoals();

        // Filter goals to select only recurring ones
        List<Goal> recurringGoals = allGoals.stream()
                .filter(goal -> isRecurringGoal(goal.getRecurring()))
                .collect(Collectors.toList());

        // Create a SimpleSubject to hold the list of recurring goals
        SimpleSubject<List<Goal>> recurringGoalsSubject = new SimpleSubject<>();

        // Set the value of the SimpleSubject to the list of recurring goals
        recurringGoalsSubject.setValue(recurringGoals);

        return recurringGoalsSubject;
    }


    // Helper method to determine if a goal is recurring based on the recurring string value
    private boolean isRecurringGoal(String recurring) {
        // Assuming "daily", "weekly", "monthly", and "yearly" are considered recurring
        return "daily".equalsIgnoreCase(recurring)
                || "weekly".equalsIgnoreCase(recurring)
                || "monthly".equalsIgnoreCase(recurring)
                || "yearly".equalsIgnoreCase(recurring);
    }

    @Override
    public Subject<List<Goal>> getGoalsByDay(int year, int month, int day) {
        // Assuming the DataSource has a method to filter goals by day

        List<Goal> goalsForDay = dataSource.getGoalsForDay(year, month, day);

        // Creating a SimpleSubject to hold the list of goals
        SimpleSubject<List<Goal>> goalsSubject = new SimpleSubject<>();

        // Setting the value of the SimpleSubject to the list of goals
        goalsSubject.setValue(goalsForDay);

        return goalsSubject;
        
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsByDay(int year, int month, int day) {
        // Retrieve the recurring goals from the data source for the specified day
        List<Goal> recurringGoalsByDay = dataSource.getGoals().stream()
                .filter(goal -> isRecurringGoal(goal.getRecurring())) // Filter recurring goals
                .filter(goal -> isGoalOccursOnDay(goal, year, month, day)) // Filter recurring goals occurring on the specified day
                .collect(Collectors.toList());

        // Create a SimpleSubject to hold the list of recurring goals by day
        SimpleSubject<List<Goal>> recurringGoalsByDaySubject = new SimpleSubject<>();

        // Set the value of the SimpleSubject to the list of recurring goals by day
        recurringGoalsByDaySubject.setValue(recurringGoalsByDay);

        return recurringGoalsByDaySubject;
    }

    // Helper method to determine if a goal occurs on the specified day
    private boolean isGoalOccursOnDay(Goal goal, int year, int month, int day) {
        // Extract the year, month, and day from the goal's date
        int goalYear = goal.getYear();
        int goalMonth = goal.getMonth();
        int goalDay = goal.getDay();

        // Check if the goal occurs on the specified day
        return goalYear == year && goalMonth == month && goalDay == day;
    }

    @Override
    public Subject<List<Goal>> getGoalsLessThanOrEqualToDay(int year, int month, int day) {
        // Retrieve all goals from the data source
        List<Goal> allGoals = dataSource.getGoals();

        // Filter goals less than or equal to the specified day
        List<Goal> goalsLessThanOrEqualToDay = allGoals.stream()
                .filter(goal -> {
                    // Compare goal's date components with the specified day
                    if (goal.getYear() < year) return true;
                    if (goal.getYear() > year) return false;
                    if (goal.getMonth() < month) return true;
                    if (goal.getMonth() > month) return false;
                    if (goal.getDay() < day) return true;
                    if (goal.getDay() > day) return false;
                    // If day is equal, compare hours and minutes
                    if (goal.getHour() < 23) return true;  // Assuming 23 is the end of the day
                    if (goal.getHour() > 23) return false;
                    return goal.getMinutes() <= 59;  // Assuming 59 is the end of the day
                })
                .collect(Collectors.toList());

        // Create a SimpleSubject to hold the list of goals
        SimpleSubject<List<Goal>> goalsSubject = new SimpleSubject<>();

        // Set the value of the SimpleSubject to the filtered list of goals
        goalsSubject.setValue(goalsLessThanOrEqualToDay);

        return goalsSubject;
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