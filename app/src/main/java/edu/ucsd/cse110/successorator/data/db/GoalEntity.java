package edu.ucsd.cse110.successorator.data.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import edu.ucsd.cse110.successorator.lib.domain.Goal;

/**
 * GoalEntity class to describe the behavior and attributes of goal
 */
@Entity(tableName = "Goals")
public class GoalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "isComplete")
    public boolean isComplete;

    @ColumnInfo(name = "sort_order")
            public int sort_order;

    /**
     * GoalEntity constructor to initialize its fields
     * @param id = id of the goal
     * @param text = text value of the goal
     * @param isComplete = complete status of the goal
     * @param sort_order = int index of the sort_order of the goal
     */
    GoalEntity(@NonNull Integer id, @NonNull String text, boolean isComplete, int sort_order){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
        this.sort_order = sort_order;
    }

    /**
     * Returns a copy of goalEntity given a goal
     * @param goal = the goal that will be used to create a GoalEntity object
     * @return GoalEntity object with the values of goal
     */
    public static GoalEntity fromGoal(@NonNull Goal goal){
        return new GoalEntity(goal.id(), goal.getText(), goal.isComplete(), goal.sortOrder());
    }

    /**
     * Converts the GoalEntity object to a goal
     * @return goal representation of this object
     */
    public @NonNull Goal toGoal(){
        return new Goal(id, text, isComplete, sort_order);
    }
}
