package edu.ucsd.cse110.successorator.lib.domain;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.Objects;

import androidx.annotation.NonNull;

public class GoalEntry {
    private @NonNull String text;
    private @NonNull boolean isComplete;

    public GoalEntry(@NonNull String text, @NonNull boolean isComplete){
        this.text = text;
        this.isComplete = isComplete;
    }

    public @NonNull boolean getIsComplete(){
        return this.isComplete;
    }

    public @NonNull String getText(){
        return this.text;
    }

    public void updateText(String text){
        this.text = text;
    }

    public void updateStatus(){
        this.isComplete = !this.isComplete;
    }

    public void makeComplete(){

        //Code to strike through texts provided by ChatGpt
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalEntry goalEntry = (GoalEntry) o;
        return Objects.equals(text, goalEntry.text) && Objects.equals(isComplete, goalEntry.isComplete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, isComplete);
    }

}
