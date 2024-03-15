package edu.ucsd.cse110.successorator.context.db;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import edu.ucsd.cse110.successorator.timedata.db.TimeEntity;

/**
 * Interface for TimeDao describing the sql queries and logic to handle inserting/deleting time
 */
@Dao
public interface ContextDao {

    @Query("SELECT * FROM Context")
    ContextEntity getContext();

    @Query("SELECT COUNT(*) FROM Context")
    int count();

    @Query("SELECT `update` FROM Context")
    boolean getUpdateValue();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertContext(ContextEntity context);

    /**
     * Clears all the values in the Time database
     */
    @Query("DELETE from Context")
    void deleteContext();
}
