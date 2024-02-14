package edu.ucsd.cse110.successorator.lib.domain;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Goal {

    private final @Nullable Integer id;
    private @NonNull String text;
    private @NonNull boolean isComplete;

    // Goal class with base constructor
    public Goal(@Nullable Integer id, @NonNull String text, @NonNull boolean isComplete){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
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

    // cse
    public void makeComplete(){

        /* Convert the String to an AttributeString, strike it through,
         convert it to StringBuilder and return it as a String
         */
        AttributedString strike = new AttributedString(this.text);
        strike.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, text.length());
        AttributedCharacterIterator iterator = strike.getIterator();
        StringBuilder stringBuilder = new StringBuilder();

        // Iterate over the characters in the iterator and append them to the StringBuilder
        for (char c = iterator.first(); c != CharacterIterator.DONE; c = iterator.next()) {
            stringBuilder.append(c);
        }

        this.text = stringBuilder.toString();
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
