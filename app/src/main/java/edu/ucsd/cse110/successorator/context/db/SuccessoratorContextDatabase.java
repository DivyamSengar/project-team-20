package edu.ucsd.cse110.successorator.context.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.ucsd.cse110.successorator.context.db.ContextDao;
import edu.ucsd.cse110.successorator.context.db.ContextEntity;

/**
 * The SuccessoratorTimeDatabase abstractClass defines a timeDao object
 */
@Database(entities = {ContextEntity.class}, version = 1)
public abstract class SuccessoratorContextDatabase extends RoomDatabase {

    /**
     * This method returns a timeDao object
     * @return a timeDao object
     */
    public abstract ContextDao contextDao();
}