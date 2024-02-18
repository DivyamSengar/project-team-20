package edu.ucsd.cse110.successorator.lib.domain;

import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Goal implements Serializable {

    private final @Nullable Integer id;
    private @NonNull String text;
    private @NonNull boolean isComplete;
    private int sortOrder;

    // Goal class with base constructor
    public Goal(@Nullable Integer id, @NonNull String text, @NonNull boolean isComplete, int sortOrder){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
        this.sortOrder = sortOrder;
    }

    // id getter
    public @Nullable Integer id() {
        return id;
    }

    // gets whether it isComplete
    public @NonNull boolean isComplete(){
        return this.isComplete;
    }

    // getter for the text for the Goal
    public @NonNull String getText(){
        return this.text;
    }

    // Although no usages yet, may prove useful later
    public void updateText(String text){
        this.text = text;
    }

    // updates the complete status of the goal
    public void updateStatus(){
        this.isComplete = !this.isComplete;
    }

    public int sortOrder(){
        return this.sortOrder;
    }

    public Goal withId(int id){
        return new Goal(id, this.text, this.isComplete, this.sortOrder);
    }

    public Goal withSortOrder(int sortOrder){
        return new Goal(this.id, this.text, this.isComplete, sortOrder);
    }

    // cse
    public void makeComplete(){

        this.isComplete = true;
    }

    public Goal switchGoal(Goal newgoal){
        return new Goal(newgoal.id, newgoal.text, newgoal.isComplete, newgoal.sortOrder);
    }

    public void makeInComplete(){
        this.isComplete = false;
    }
    // overridden equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(this.id, goal.id) && Objects.equals(this.text, goal.text) && Objects.equals(this.isComplete, goal.isComplete);
    }

    // overridden hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, text, isComplete);
    }

}
