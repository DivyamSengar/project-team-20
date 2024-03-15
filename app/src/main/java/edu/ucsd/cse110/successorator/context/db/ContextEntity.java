package edu.ucsd.cse110.successorator.context.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Context")
public class ContextEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "context")
    public int context;

    @ColumnInfo(name = "update")
    public boolean update;

    public ContextEntity(int id, int context, boolean update){
        this.id = id;
        this.context = context;
        this.update=update;
    }

    public int getContextValue(){
        return this.context;
    }
}
