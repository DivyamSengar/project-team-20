package edu.ucsd.cse110.successorator.context.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Context")
public class ContextEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "context")
    public int context;

    public ContextEntity(int context){
        this.context = context;
    }

    public int getContextValue(){
        return this.context;
    }
}
