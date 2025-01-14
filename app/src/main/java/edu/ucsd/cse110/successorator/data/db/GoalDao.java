package edu.ucsd.cse110.successorator.data.db;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

/**
 * Interface for GoalDao, describing the sql queries and goal insert logic
 */
@Dao
public interface GoalDao {

    /**
     * Only one insert since we only insert one at time
     * @param goal to insert
     * @return Long value after insertion
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GoalEntity goal);


    /**
     * Query all the goals with a given id
     * @param id of goal to find
     * @return goalEntity with specific id
     */
    @Query("SELECT * FROM Goals WHERE id = :id")
    GoalEntity find(int id);

    /**
     * Query all the goals by sort_order
     * @return List of GoalEntity objects in sort_order
     */
    @Query("SELECT * FROM Goals ORDER BY sort_order")
    List<GoalEntity> find();

    /**
     * Query all the goals with a given id
     * @param id of goals to find
     * @return LiveData<GoalEntity> object of specific id
     */
    @Query("SELECT * FROM Goals WHERE id = :id")
    LiveData<GoalEntity> findAsLiveData(int id);

    /**
     * Query all goals by sort_order
     * @return LiveData list of GoalEntity objects in sort_order
     */
    @Query("SELECT * FROM Goals ORDER BY sort_order")
    LiveData<List<GoalEntity>> findAllAsLiveData();

    /**
     * Query all goals where the complete status is equal to that of the parameter
     * @param isComplete is the status we want to query the goals by
     * @return List of goal entity objects with a given complete status
     */
    @Query("SELECT * FROM Goals WHERE isComplete = :isComplete")
    List<GoalEntity> find(boolean isComplete);

    /**
     * Get the minimum sort_order from goals
     * @return integer minimum sort_order
     */
    @Query("SELECT MIN(sort_order) FROM Goals")
    int getMinSortOrder();

    /**
     * Get the maximum sort_order from goals
     * @return integer maximum sort_order
     */
    @Query("SELECT MAX(sort_order) FROM Goals")
    int getMaxSortOrder();


    @Query("SELECT MAX(goalPair) FROM Goals")
    int getMaxgoalPair();




    /**
     * Delete goals with given id
     * @param id of goal to delete
     */
    @Query("DELETE from Goals WHERE id = :id")
    void delete(int id);

    /**
     * Delete goals with isComplete being true
     */
    @Query("DELETE from Goals WHERE isComplete = true AND year <= :year AND month <= :month AND day <= :day" )
    void deleteComplete(int year, int month, int day);

    @Query("DELETE from Goals WHERE isComplete = true AND recurring != 1 AND year <= :year AND month <= :month AND day <= :day" )
    void deleteCompleteNonDaily(int year, int month, int day);

    @Query("SELECT * from Goals WHERE Pending = true")
    LiveData<List<GoalEntity>> getPending();

    @Query("SELECT * from Goals WHERE year = :year AND month = :month AND day = :day")
    LiveData<List<GoalEntity>> getByDay(int year, int month, int day);

    @Query("SELECT * from Goals WHERE year <= :year AND month <= :month AND day <= :day")
    LiveData<List<GoalEntity>> getLessThanOrEqualToDay(int year, int month, int day);

    @Query("SELECT * from Goals WHERE recurring != 0")
    List<GoalEntity> getRecurring();

    @Query("SELECT * from Goals WHERE recurring != 0 AND year = :year AND month = :month AND day = :day")
    LiveData<List<GoalEntity>> getRecurringByDay(int year, int month, int day);

    @Query("SELECT * from Goals WHERE context = 1")
    LiveData<List<GoalEntity>> getContextHome();

    @Query("SELECT * from Goals WHERE context = 2")
    LiveData<List<GoalEntity>> getContextWork();

    @Query("SELECT * from Goals WHERE context = 3")
    LiveData<List<GoalEntity>> getContextSchool();

    @Query("SELECT * from Goals WHERE context = 4")
    LiveData<List<GoalEntity>> getContextErrands();

    @Query("SELECT * from Goals WHERE goalPair = :goalPair")
    List<GoalEntity> getGoalPairVals(int goalPair);


    @Query("SELECT * FROM Goals WHERE id = :id")
    LiveData<List<GoalEntity>> findListOfGoalsById(int id);

    /**
     * Append a goal to the list
     * @param goal goal to append
     * @return integer of the index after insert
     */
    @Transaction
    default int append(GoalEntity goal){
        var newGoalEntity = new GoalEntity(goal.id, goal.text, goal.isComplete,
                getMaxSortOrder()+1, goal.pending, goal.recurring,
                goal.minutes, goal.hour,goal.day, goal.month, goal.year, goal.context, goal.goalPair);
        return Math.toIntExact(insert(newGoalEntity));
    }

    /**
     * Prepend a goal to the list
     * @param goal goal to be prepended
     * @return integer of the index after insert
     */
    @Transaction
    default int prepend(GoalEntity goal){
        var newGoalEntity = new GoalEntity(goal.id, goal.text, goal.isComplete,
                getMinSortOrder()-1, goal.pending, goal.recurring,
                goal.minutes, goal.hour,goal.day, goal.month, goal.year, goal.context, goal.goalPair);
        return Math.toIntExact(insert(newGoalEntity));
    }

    @Transaction
    default int InsertWithSortOrder(GoalEntity goal, int sortOrder){
        var newGoalEntity = new GoalEntity(goal.id, goal.text, goal.isComplete,
                sortOrder, goal.pending, goal.recurring,
                goal.minutes, goal.hour,goal.day, goal.month, goal.year, goal.context, goal.goalPair);
        return Math.toIntExact(insert(newGoalEntity));
    }

    @Transaction
    default int InsertWithSortOrderAndRecurring(GoalEntity goal, int sortOrder, int recurring){
        var newGoalEntity = new GoalEntity(goal.id, goal.text, goal.isComplete,
                sortOrder, goal.pending, recurring,
                goal.minutes, goal.hour,goal.day, goal.month, goal.year, goal.context, goal.goalPair);
        return Math.toIntExact(insert(newGoalEntity));
    }

}