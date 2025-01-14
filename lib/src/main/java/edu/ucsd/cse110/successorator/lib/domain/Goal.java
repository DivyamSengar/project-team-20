package edu.ucsd.cse110.successorator.lib.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Goal class that describes the behavior and attributes of goal
 */
public class Goal implements Serializable {

    private final Integer id;
    private @NonNull String text;
    private @NonNull boolean isComplete;
    private int sortOrder;
    private boolean pending;
    private int recurring;
    private int minutes;
    private int hour;
    private int day;
    private int month;
    private int year;
    private int context;

    private int goalPair;

    /**
     * Goal constructor to initialize the fields of goal
     * @param id = integer id of a goal
     * @param text = string text value of the goal
     * @param isComplete = boolean status of whether the goal is complete or incomplete
     * @param sortOrder = index of goal in the List according the sorted order
     */
    public Goal(@Nullable Integer id, @NonNull String text, @NonNull boolean isComplete,
                int sortOrder, boolean pending, int recurring, int minutes,
                int hour, int day, int month, int year, int context, int goalPair){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
        this.sortOrder = sortOrder;
        this.pending = pending;
        this.recurring = recurring;
        this.minutes = minutes;
        this.hour = hour;
        this.day=day;
        this.month=month;
        this.year=year;
        this.context = context;
        this.goalPair = goalPair;
    }

    /**
     * Getter method to fetch a goal's id
     * @return integer id of the goal
     */
    public @Nullable Integer id() {
        return id;
    }

    /**
     * Getter method to get the complete status of the goal
     * @return boolean whether goal is complete or not
     */
    public @NonNull boolean isComplete(){
        return this.isComplete;
    }

    /**
     * Getter method to get the string text value of the goal
     * @return string value of the goal
     */
    public @NonNull String getText(){
        return this.text;
    }

    public int getGoalPair() {return this.goalPair;}

    /**
     * Getter method to get the index of the goal in sortOrder
     * @return integer value of the sortOrder of the goal
     */
    public int sortOrder(){
        return this.sortOrder;
    }

    /**
     * Method changes the status of the current goal to complete
     */
    public boolean isPending(){ return this.pending;}

    public int getRecurring(){return this.recurring;}

    public int getMinutes() {
        return minutes;
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getContext() {return context;}

    public void makeComplete(){

        this.isComplete = true;
    }

    /**
     * Method changes the status of the current goal to incomplete
     */
    public void makeInComplete(){
        this.isComplete = false;
    }
    public void changePending() {this.pending = !this.pending;}
    public void setRecurring(int recurring){this.recurring = recurring;}
    public void setDate(int minutes, int hour, int day, int month, int year){
        this.minutes=minutes;
        this.day = day;
        this.hour = hour;
        this.month = month;
        this.year=year;
    }

    /* returns the boundary/recurring date if it is a recurring goal,
    else it returns the same date to indicate that it is a one-time goal
    */
    public LocalDateTime getBoundaryRecurringDate(){
        LocalDateTime goal_Time = LocalDateTime.of(this.getYear(),
                this.getMonth(), this.getDay(),  0, 0);
        if(this.getRecurring() == 1){
            return goal_Time.plusDays(1);
        } else if(this.getRecurring() == 2) {
            return goal_Time.plusWeeks(1);
            /* this monthly is likely incorrect given that the monthly option is supposed to be for
             recurring days of the week, not just going to the same date in the next month
            */
        } else if(this.getRecurring() == 3) {
            return getNextDayOfWeekInMonth(goal_Time);
        } else if(this.getRecurring() == 4) {
            return goal_Time.plusYears(1);
        }
        else return goal_Time;
    }
    /*updates  recurring goals to their next recurring date

     */
    public Goal updateRecurring(LocalDateTime currentTime, LocalDateTime previousTime){
        LocalDateTime goal_Time = LocalDateTime.of(this.getYear(),
                this.getMonth(), this.getDay(), this.getHour(), this.getMinutes());

        Period period = Period.between(previousTime.toLocalDate(), currentTime.toLocalDate());
        LocalDate baseDate = goal_Time.toLocalDate();

        // Add the period to the LocalDate
        LocalDate newDate = baseDate.plus(period);

        // Create a new LocalDateTime with the new date and original time
        LocalDateTime NewDateTime = LocalDateTime.of(newDate, goal_Time.toLocalTime());

        if(this.getRecurring() == 1){
            //
            goal_Time = NewDateTime;
        }
        NewDateTime = currentTime;
        if(this.getRecurring() == 2) {
            if (NewDateTime.isBefore(this.getBoundaryRecurringDate()) ||
                    NewDateTime.isEqual(this.getBoundaryRecurringDate())) goal_Time = goal_Time.plusWeeks(1);
            else if (NewDateTime.isAfter(goal_Time)) {
                while(goal_Time.plusWeeks(1).isBefore(NewDateTime)
                        || goal_Time.plusWeeks(1).isEqual(NewDateTime)){
                    goal_Time = goal_Time.plusWeeks(1);
                }
            }
            /* this monthly is likely incorrect given that the monthly option is supposed to be for
             recurring days of the week, not just going to the same date in the next month
            */
        } else if(this.getRecurring() == 3) {
            if (NewDateTime.isBefore(this.getBoundaryRecurringDate()) ||
                    NewDateTime.isEqual(this.getBoundaryRecurringDate()) ) goal_Time = this.getNextDayOfWeekInMonth(goal_Time);
            else if (NewDateTime.isAfter(goal_Time)) {
                while(this.getNextDayOfWeekInMonth(goal_Time).isBefore(NewDateTime)
                        || this.getNextDayOfWeekInMonth(goal_Time).isEqual(NewDateTime)){
                    goal_Time = this.getNextDayOfWeekInMonth(goal_Time);
                }
            }
//            goal_Time = goal_Time.plusMonths(1);
        } else if(this.getRecurring() == 4) {
            if (NewDateTime.isBefore(this.getBoundaryRecurringDate()) ||
                    NewDateTime.isEqual(this.getBoundaryRecurringDate())) goal_Time = goal_Time.plusYears(1);
            else if (NewDateTime.isAfter(goal_Time)) {
                while(goal_Time.plusYears(1).isBefore(NewDateTime)
                        || goal_Time.plusYears(1).isEqual(NewDateTime)){
                    goal_Time = goal_Time.plusYears(1);
                }
            }

        }
        this.setDate(goal_Time.getMinute(), goal_Time.getHour(), goal_Time.getDayOfMonth(),
                goal_Time.getMonthValue(), goal_Time.getYear());
        return this;
    }

    public static LocalDateTime getNextDayOfWeekInMonth(LocalDateTime current) {
        int currentOrdinal = (current.getDayOfMonth() - 1) / 7 + 1; // Calculate current ordinal position
        LocalDateTime temp = current.plusWeeks(1);
        int tempOrdinal = (temp.getDayOfMonth()-1)/7 + 1;
        if (currentOrdinal == 5 && tempOrdinal == 1) return temp;
        while (tempOrdinal != currentOrdinal){
            temp = temp.plusWeeks(1);
            tempOrdinal = (temp.getDayOfMonth()-1)/7 + 1;
        }
        return temp;

    }

    /**
     * Methods overrides the equals method in order to compare two goal objects
     * @param o Object to compare this object with
     * @return boolean value of whether the two goals are the same or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(this.id, goal.id) && Objects.equals(this.text, goal.text) && Objects.equals(this.isComplete, goal.isComplete);
    }

    /**
     * Method overrides the hashCode function to hash a goal object
     * @return integer hash value of a goal object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, text, isComplete);
    }

}
