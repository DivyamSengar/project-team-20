package edu.ucsd.cse110.successorator.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class DataSource {

    private int nextId = 0;

    private int minSortOrder = Integer.MIN_VALUE;
    private int maxSortOrder = Integer.MAX_VALUE;

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

    public final static List<Goal> DEFAULT_GOALS = List.of(
            new Goal(1, "do homework", false, 0),
            new Goal(2, "clean room", false, 1),
            new Goal(3, "play basketball", false, 2),
            new Goal(4, "do smn", false, 3)
    );

    public static DataSource fromDefault() {
        var data = new DataSource();
        for (int i = 0; i < 4; i++) {
            data.putGoalEntry(DEFAULT_GOALS.get(i));
        }
        return data;
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

    // updater to make goals contain current incomplete goals followed by complete goals
    protected void updateGoals() {
        if (goals != null) {
            goals.clear();
        }
        else goals = new ArrayList<>();
        goals.addAll(List.copyOf(this.goalsInComplete.values()));
        goals.addAll(List.copyOf(this.goalsComplete.values()));
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

    public int getMaxSortOrder() {
        return maxSortOrder;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    /* modifier to add a goal, adding it into the complete/incomplete list,
            then making a subject for it, and adding it to the list of all subejcts
             */
    public void putGoalEntry(Goal goal) {
        var fixedGoal = preInsert(goal);

        if(fixedGoal.isComplete()){
            goalsComplete.put(fixedGoal.id(), fixedGoal);
        } else {
            goalsInComplete.put(fixedGoal.id(), fixedGoal);
        }
        postInsert();

        if (goalEntrySubjects.containsKey(fixedGoal.id())) {
            goalEntrySubjects.get(fixedGoal.id()).setValue(fixedGoal);
        }
        // I have no idea what this does but the null pointer exception makes me scared
        else {
            getGoalEntrySubject(fixedGoal.id()).setValue(fixedGoal);
        }
        allGoalEntrySubjects.setValue(getGoals());
    }

    public void putGoalEntries(List<Goal> goals){
        var fixedGoals = goals.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedGoals.forEach(goal -> {
            if (goal.isComplete()){
                goalsComplete.put(goal.id(), goal);
            } else {
                goalsInComplete.put(goal.id(), goal);
            }
        });
        postInsert();
        fixedGoals.forEach(goal -> {
            if (goalEntrySubjects.containsKey(goal.id())) {
                goalEntrySubjects.get(goal.id()).setValue(goal);
            }
            // I have no idea what this does but the null pointer exception makes me scared
            else {
                getGoalEntrySubject(goal.id()).setValue(goal);
            }
        });
        allGoalEntrySubjects.setValue(getGoals());
    }

    // markAsIncomplete
    public void removeGoal(int id){
        var goal = getGoals().get(id);
        var sortOrder = goal.sortOrder();

//        // remove from complete
//        if (goal.isComplete()){
//
//        } else {
//            goal.updateStatus();
//        }

        // mark as incomplete
        goal.updateStatus();

        shiftSortOrders(sortOrder, maxSortOrder, -1);

        allGoalEntrySubjects.setValue(getGoals());

    }

    public void shiftSortOrders(int from, int to, int by){
         var allGoals = goals.stream()
                .filter(goal -> goal.sortOrder() >= from && goal.sortOrder() <= to)
                .map(goal -> goal.withSortOrder(goal.sortOrder() + by))
                .collect(Collectors.toList());
         putGoalEntries(allGoals);
    }

    private Goal preInsert(Goal goal){
        var id = goal.id();
        if (id == null){
            goal = goal.withId(nextId++);
        } else if (id > nextId) {
            nextId = id + 1;
        }
        return goal;
    }

    private void postInsert(){
        minSortOrder = getGoals().stream()
                .map(Goal::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = getGoals().stream()
                .map(Goal::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }
}
