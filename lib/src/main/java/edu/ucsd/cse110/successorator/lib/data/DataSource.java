package edu.ucsd.cse110.successorator.lib.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.GoalEntry;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class DataSource {

    // list of complete Goals
    private Map<Integer, GoalEntry> goalsComplete
            = new HashMap<>();

    // list of incomplete Goals
    private Map<Integer, GoalEntry> goalsInComplete
            = new HashMap<>();

    // List of all goals (complete and incomplete)
    private List<GoalEntry> goals;

    // map to SimpleSubjects of Goals from ints
    private Map<Integer, SimpleSubject<GoalEntry>> goalEntrySubjects
            = new HashMap<>();
    // subject of all goal entries
    private SimpleSubject<List<GoalEntry>> allGoalEntrySubjects
            = new SimpleSubject<>();

    // empty constructor
    public DataSource() {}

    // updater to make goals contain current incomplete goals followed by complete goals
    protected void updateGoals() {
        if (goals != null) {
            goals.clear();
        }
        else goals = new ArrayList<>();
        goals.addAll(List.copyOf(this.goalsInComplete.values()));
        goals.addAll(List.copyOf(this.goalsComplete.values()));
    }

    // return goals after updating them
    public List<GoalEntry> getGoals() {
        updateGoals();
        return goals;

    }

    // return goal from id
    public GoalEntry getGoalEntry(int id) {
        if(goalsInComplete.containsKey(id)){
            return goalsInComplete.get(id);
        } else {
            return goalsComplete.get(id);
        }
    }

    // return goalSubject from id
    public SimpleSubject<GoalEntry> getGoalEntrySubject(int id) {
        if (!goalEntrySubjects.containsKey(id)) {
            var subject = new SimpleSubject<GoalEntry>();
            subject.setValue(getGoalEntry(id));
            goalEntrySubjects.put(id, subject);
        }
        return goalEntrySubjects.get(id);
    }

    // getter for allGoalEntrySubjects
    public SimpleSubject<List<GoalEntry>> getAllGoalEntrySubject() {
        return allGoalEntrySubjects;
    }

    /* modifier to add a goal, adding it into the complete/incomplete list,
    then making a subject for it, and adding it to the list of all subejcts
     */
    public void putGoalEntry(GoalEntry goal) {
        if(goal.getIsComplete()){
            goalsComplete.put(goal.id(), goal);
        } else {
            goalsInComplete.put(goal.id(), goal);
        }

        if (goalEntrySubjects.containsKey(goal.id())) {
            goalEntrySubjects.get(goal.id()).setValue(goal);
        }
        else {
            getGoalEntrySubject(goal.id()).setValue(goal);
        }
        allGoalEntrySubjects.setValue(getGoals());
    }
}
