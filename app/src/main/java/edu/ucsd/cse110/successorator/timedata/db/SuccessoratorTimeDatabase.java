package edu.ucsd.cse110.successorator.timedata.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * The SuccessoratorTimeDatabase abstractClass defines a timeDao object
 */
@Database(entities = {TimeEntity.class}, version = 1)
public abstract class SuccessoratorTimeDatabase extends RoomDatabase {

    /**
     * This method returns a timeDao object
     * @return a timeDao object
     */
    public abstract TimeDao timeDao();
}
