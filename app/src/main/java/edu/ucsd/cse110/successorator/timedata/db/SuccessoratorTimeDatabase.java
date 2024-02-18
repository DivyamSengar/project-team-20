package edu.ucsd.cse110.successorator.timedata.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TimeEntity.class}, version = 1)
public abstract class SuccessoratorTimeDatabase extends RoomDatabase {
    public abstract TimeDao timeDao();
}
