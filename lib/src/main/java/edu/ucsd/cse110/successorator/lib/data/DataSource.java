package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.GoalEntry;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class DataSource {
    private Map<Integer, GoalEntry> goals
            = new HashMap<>();
    private Map<Integer, SimpleSubject<GoalEntry>> goalEntrySubjects
            = new HashMap<>();
    private SimpleSubject<List<GoalEntry>> allGoalEntrySubjects
            = new SimpleSubject<>();

    public DataSource() {
    }

    public List<GoalEntry> getGoals() {
        return List.copyOf(goals.values());
    }

    public GoalEntry getGoalEntry(int id) {
        return goals.get(id);
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
        goals.put(goal.id(), goal);
        if (goalEntrySubjects.containsKey(goal.id())) {
            goalEntrySubjects.get(goal.id()).setValue(goal);
        }
        allGoalEntrySubjects.setValue(getGoals());
    }
}
