package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.GoalEntry;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class DataSource {
    private Map<Integer, GoalEntry> goalsComplete
            = new HashMap<>();

    private Map<Integer, GoalEntry> goalsInComplete
            = new HashMap<>();

    private List<GoalEntry> goals;
    private Map<Integer, SimpleSubject<GoalEntry>> goalEntrySubjects
            = new HashMap<>();
    private SimpleSubject<List<GoalEntry>> allGoalEntrySubjects
            = new SimpleSubject<>();

    public DataSource() {}

    private void updateGoals() {
        goals.clear();
        goals.addAll(List.copyOf(this.goalsInComplete.values()));
        goals.addAll(List.copyOf(this.goalsComplete.values()));
    }

    public List<GoalEntry> getGoals() {
        updateGoals();
        return goals;
    }

    public GoalEntry getGoalEntry(int id) {
        if(goalsInComplete.containsKey(id)){
            return goalsInComplete.get(id);
        } else {
            return goalsComplete.get(id);
        }
    }

    public SimpleSubject<GoalEntry> getGoalEntrySubject(int id) {
        if (!goalEntrySubjects.containsKey(id)) {
            var subject = new SimpleSubject<GoalEntry>();
            subject.setValue(getGoalEntry(id));
            goalEntrySubjects.put(id, subject);
        }
        return goalEntrySubjects.get(id);
    }

    public SimpleSubject<List<GoalEntry>> getAllGoalEntrySubject() {
        return allGoalEntrySubjects;
    }

    public void putGoalEntry(GoalEntry goal) {
        if(goal.getIsComplete()){
            goalsComplete.put(goal.id(), goal);
        } else {
            goalsInComplete.put(goal.id(), goal);
        }

        if (goalEntrySubjects.containsKey(goal.id())) {
            goalEntrySubjects.get(goal.id()).setValue(goal);
        }
        allGoalEntrySubjects.setValue(getGoals());
    }
}
