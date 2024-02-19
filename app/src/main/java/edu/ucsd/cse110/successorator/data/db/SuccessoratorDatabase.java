package edu.ucsd.cse110.successorator.data.db;
import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * The SuccessoratorDatabase abstractClass defines a goalDao object
 */
@Database(entities = {GoalEntity.class}, version = 1)
public abstract class SuccessoratorDatabase extends RoomDatabase{

    /**
     * This method returns a goalDao object
     * @return a goalDao object
     */
    public abstract GoalDao goalDao();
}