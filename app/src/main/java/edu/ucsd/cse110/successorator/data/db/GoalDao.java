package edu.ucsd.cse110.successorator.data.db;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface GoalDao {

    // Only one insert since we only insert one at time
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GoalEntity goal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<GoalEntity> goals);

    @Query("SELECT * FROM Goals WHERE id = :id")
    GoalEntity find(int id);

    @Query("SELECT * FROM Goals ORDER BY sort_order")
    List<GoalEntity> find();

    @Query("SELECT * FROM Goals WHERE id = :id")
    LiveData<GoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM Goals ORDER BY sort_order")
    LiveData<List<GoalEntity>> findAllAsLiveData();

    @Query("SELECT * FROM Goals WHERE isComplete = :isComplete")
    List<GoalEntity> find(boolean isComplete);

    @Query("SELECT COUNT(*) FROM Goals")
    int count();

    @Query("SELECT MIN(sort_order) FROM Goals")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM Goals")
    int getMaxSortOrder();

    @Query("UPDATE goals SET sort_order = sort_order + :by " +
            "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);
    @Query("DELETE from Goals WHERE id = :id")
    void delete(int id);

    @Transaction
    default int append(GoalEntity goal){
        var maxSort = getMaxSortOrder();
        var newGoalEntity = new GoalEntity(goal.id, goal.text, goal.isComplete, maxSort+1);
        return Math.toIntExact(insert(newGoalEntity));
    }
    @Transaction
    default int prepend(GoalEntity goal){
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newGoalEntity = new GoalEntity(goal.id, goal.text, goal.isComplete, getMinSortOrder()-1);
        return Math.toIntExact(insert(newGoalEntity));
    }

}