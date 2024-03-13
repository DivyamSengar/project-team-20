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
    public Integer id;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "isComplete")
    public boolean isComplete;

    @ColumnInfo(name = "sort_order")
            public int sort_order;

    // recurring: 0 if not and then rest of fields referring to different recurring categories
    // pending: whether pending or not
    // date: the date to distinguish today/tomorrow and better distinguish recurrent nature
    // for monthly, figure out what the first day of the month was and then determine it
    @ColumnInfo(name = "pending")
            public boolean pending;
    @ColumnInfo(name = "recurring")
            public String recurring;
    // use an int array to represent the distinct counts of minutes, hours, day, month, and year
    @ColumnInfo(name = "hour")
    public int hour;

    @ColumnInfo(name = "day")
    public int day;

    @ColumnInfo(name = "minutes")
    public int minutes;

    @ColumnInfo(name = "month")
    public int month;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "context")
    public int context;

    /**
     * GoalEntity constructor to initialize its fields
     * @param id = id of the goal
     * @param text = text value of the goal
     * @param isComplete = complete status of the goal
     * @param sort_order = int index of the sort_order of the goal
     */
    GoalEntity(@NonNull Integer id, @NonNull String text, boolean isComplete,
               int sort_order, boolean pending, String recurring, int minutes,
               int hour, int day, int month, int year, int context){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
        this.sort_order = sort_order;
        this.pending = pending;
        this.recurring = recurring;
        this.minutes = minutes;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
        this.context = context;
    }

    /**
     * Returns a copy of goalEntity given a goal
     * @param goal = the goal that will be used to create a GoalEntity object
     * @return GoalEntity object with the values of goal
     */
    public static GoalEntity fromGoal(@NonNull Goal goal){
        return new GoalEntity(goal.id(), goal.getText(), goal.isComplete(), goal.sortOrder(),
                goal.isPending(), goal.getRecurring(), goal.getMinutes(), goal.getHour(),
                goal.getDay(), goal.getMonth(), goal.getYear(), goal.getContext());
    }

    /**
     * Converts the GoalEntity object to a goal
     * @return goal representation of this object
     */
    public @NonNull Goal toGoal(){
        return new Goal(id, text, isComplete, sort_order, pending, recurring, minutes, hour, day, month, year, context);
    }
}
