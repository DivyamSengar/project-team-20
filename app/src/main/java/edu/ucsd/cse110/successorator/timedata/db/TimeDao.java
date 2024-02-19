package edu.ucsd.cse110.successorator.timedata.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Interface for TimeDao describing the sql queries and logic to handle inserting/deleting time
 */
@Dao
public interface TimeDao {

    /**
     * Fetches all values in Time
     * @return TimeEntity object stored in the database
     */
    @Query("SELECT * FROM Time")
    TimeEntity getTime();

    /**
     * Counts the number of elements in the Time database
     * @return integer count of size of the database
     */
    @Query("SELECT COUNT(*) FROM Time")
    int count();

    /**
     * Fetches all the values from the database
     * @return a LiveData of a TimeEntity object that is stored in the Time database
     */
    @Query("SELECT * FROM Time")
    LiveData<TimeEntity> getTimeAsLiveData();

    /**
     * Inserting a TimeEntity object
     * @param time = TimeEntity object to be inserted
     * @return Long value after inserting object
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTime(TimeEntity time);

    /**
     * Clears all the values in the Time database
     */
    @Query("DELETE from Time")
    void deleteTime();
}
