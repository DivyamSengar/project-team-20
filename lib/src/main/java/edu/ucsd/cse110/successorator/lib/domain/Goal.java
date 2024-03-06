package edu.ucsd.cse110.successorator.lib.domain;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Goal class that describes the behavior and attributes of goal
 */
public class Goal implements Serializable {

    private final @Nullable Integer id;
    private @NonNull String text;
    private @NonNull boolean isComplete;
    private int sortOrder;
    private boolean pending;
    private String recurring;
    private int[] date;

    /**
     * Goal constructor to initialize the fields of goal
     * @param id = integer id of a goal
     * @param text = string text value of the goal
     * @param isComplete = boolean status of whether the goal is complete or incomplete
     * @param sortOrder = index of goal in the List according the sorted order
     */
    public Goal(@Nullable Integer id, @NonNull String text, @NonNull boolean isComplete,
                int sortOrder, boolean pending, String recurring, int[] date){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
        this.sortOrder = sortOrder;
        this.pending = pending;
        this.recurring = recurring;
        this.date = date;
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

    public @NonNull String getRecurring(){return this.recurring;}
    public int[] getDate(){return this.date;}
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
    public void setRecurring(String recurring){this.recurring = recurring;}
    public void setDate(int[] date){this.date=date;}

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
