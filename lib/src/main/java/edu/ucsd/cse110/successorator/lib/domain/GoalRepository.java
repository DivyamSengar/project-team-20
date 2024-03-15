package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;
import java.util.List;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Interface to describe the attribute and behavior of a goal repository/list
 */
public interface GoalRepository {

    // getting a SimpleSubject to a goal based on its id

    /**
     * Find specific goal subject given a goal id
     * @param id = id of goal to find
     * @return a subject of the goal given a specific id
     */
    Subject<Goal> find(int id);

    /**
     * Returns a subject of a list of all the goals
     * @return subject of a list of all the goals
     */
    Subject<List<Goal>> findAll();

    Subject<List<Goal>> getPendingGoals();

    Subject<List<Goal>> getRecurringGoals();

    Subject<List<Goal>> getRecurringGoalsIncomplete();

    Subject<List<Goal>> getRecurringGoalsComplete();

    Subject<List<Goal>> getGoalsByDayIncomplete(int year, int month, int day);

    Subject<List<Goal>> getGoalsByDayComplete(int year, int month, int day);


    Subject<List<Goal>> getGoalsByDay(int year, int month, int day);

    Subject<List<Goal>> getRecurringGoalsByDay(int year, int month, int day);
    Subject<List<Goal>> getRecurringGoalsByDayComplete(int year, int month, int day);

    Subject<List<Goal>> getGoalsLessThanOrEqualToDay(int year, int month, int day);

    void removeGoalComplete(int id);
    void removeGoalIncomplete(int id);

    void appendComplete(Goal goal);

    void appendIncomplete(Goal goal);
    boolean isGoalsEmpty();


    /**
     * Removes a goal with a specific id from the list
     * @param id of goal to remove
     */
    void remove(int id);

    /**
     * Appends a goal to the back of the list
     * @param goal to be appended
     */
    void append(Goal goal);

    /**
     * Prepends goal to the front of the list
     * @param goal to be prepended
     */
    void prepend(Goal goal);

    /**
     * Removes all the goals in the list
     */
    void deleteCompleted();


    void getContextHome();

    void deleteCompleted(int year, int monthValue, int dayOfMonth);

    void getContextSchool();

    void InsertWithSortOrder(Goal goal, int sortOrder);

    void InsertWithSortOrderAndRecurring(Goal goal, int sortOrder, String recurring);

    Subject<Object> findListOfGoalsById(int id);
}
