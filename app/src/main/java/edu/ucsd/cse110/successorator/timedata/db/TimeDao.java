package edu.ucsd.cse110.successorator.timedata.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import edu.ucsd.cse110.successorator.data.db.GoalEntity;

@Dao
public interface TimeDao {
    @Query("SELECT * FROM Time")
    TimeEntity getTime();

    @Query("SELECT COUNT(*) FROM Time")
    int count();
    @Query("SELECT * FROM Time")
    LiveData<TimeEntity> getTimeAsLiveData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTime(TimeEntity time);

    @Query("DELETE from Time")
    void deleteTime();
}
