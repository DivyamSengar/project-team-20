package edu.ucsd.cse110.successorator.lib.domain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import edu.ucsd.cse110.successorator.lib.domain.GoalEntry;

public class Goals {
    private ArrayList<GoalEntry> goalsInComplete;

    private ArrayList<GoalEntry> goalsComplete;

    public Goals(){
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

}
