package edu.ucsd.cse110.successorator.lib.domain;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
public class GoalEntry {
    private String text;
    private boolean isComplete;

    public GoalEntry(String text, boolean isComplete){
        this.text = text;
        this.isComplete = isComplete;
    }

    public boolean getIsComplete(){
        return this.isComplete;
    }

    public String getText(){
        return this.text;
    }

    public void updateText(String text){
        this.text = text;
    }

    public void updateStatus(){
        this.isComplete = !this.isComplete;
    }

    public void makeComplete(){
        AttributedString strike = new AttributedString(this.text);
        strike.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, text.length());

    }

}
