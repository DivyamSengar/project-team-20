package edu.ucsd.cse110.successorator.lib.domain;

import java.util.ArrayList;

public class GoalList {
    private ArrayList<GoalEntry> goalsInComplete;

    private ArrayList<GoalEntry> goalsComplete;

    public GoalList(){
        this.goalsInComplete = new ArrayList<>();
        this.goalsComplete = new ArrayList<>();
    }

    public void addNewGoal(GoalEntry goal){
        goalsInComplete.add(goal);
    }

    public ArrayList<GoalEntry> displayGoals(){
        ArrayList<GoalEntry> combinedList = new ArrayList<>();
        combinedList.addAll(this.goalsInComplete);
        combinedList.addAll(this.goalsComplete);

        return combinedList;
    }

    public void makeComplete(){

    }
}
