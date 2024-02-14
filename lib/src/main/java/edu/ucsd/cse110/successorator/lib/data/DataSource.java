package edu.ucsd.cse110.successorator.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class DataSource {

    // list of complete Goals
    private Map<Integer, Goal> goalsComplete
            = new HashMap<>();

    // list of incomplete Goals
    private Map<Integer, Goal> goalsInComplete
            = new HashMap<>();

    // List of all goals (complete and incomplete)
    private List<Goal> goals;

    // map to SimpleSubjects of Goals from ints
    private Map<Integer, SimpleSubject<Goal>> goalEntrySubjects
            = new HashMap<>();
    // subject of all goal entries
    private SimpleSubject<List<Goal>> allGoalEntrySubjects
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
    public List<Goal> getGoals() {
        updateGoals();
        return goals;

    }

    // return goal from id
    public Goal getGoalEntry(int id) {
        if(goalsInComplete.containsKey(id)){
            return goalsInComplete.get(id);
        } else {
            return goalsComplete.get(id);
        }
    }

    // return goalSubject from id
    public SimpleSubject<Goal> getGoalEntrySubject(int id) {
        if (!goalEntrySubjects.containsKey(id)) {
            var subject = new SimpleSubject<Goal>();
            subject.setValue(getGoalEntry(id));
            goalEntrySubjects.put(id, subject);
        }
        return goalEntrySubjects.get(id);
    }

    // getter for allGoalEntrySubjects
    public SimpleSubject<List<Goal>> getAllGoalEntrySubject() {
        return allGoalEntrySubjects;
    }

    /* modifier to add a goal, adding it into the complete/incomplete list,
    then making a subject for it, and adding it to the list of all subejcts
     */
    public void putGoalEntry(Goal goal) {
        if(goal.isComplete()){
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

    public final static List<Goal> DEFAULT_GOALS = List.of(
            new Goal(1, "do homework", false),
            new Goal(2, "clean room", false),
            new Goal(3, "play basketball", false),
            new Goal(4, "do smn", false)
    );

    public static DataSource fromDefault() {
        var data = new DataSource();
        for (int i = 0; i < 4; i++) {
            data.putGoalEntry(DEFAULT_GOALS.get(i));
        }
        return data;
    }
}
