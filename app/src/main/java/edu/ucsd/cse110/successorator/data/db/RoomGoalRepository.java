package edu.ucsd.cse110.successorator.data.db;
import androidx.lifecycle.Transformations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.util.*;

/**
 * RoomGoalRepository class that implements the behavior and attributes of GoalRepository
 */
public class RoomGoalRepository implements GoalRepository {
    private final GoalDao goalDao;

    /**
     * RoomGoalRepository constructor to initialize its fields
     * @param goalDao goalDao object to initialize the field with
     */
    public RoomGoalRepository(GoalDao goalDao){
        this.goalDao = goalDao;
    }

    /**
     * This method returns a subject of a goal given a specific goal id
     * @param id = id of goal to find
     * @return Subject of goal given the parameter goal id
     */
    @Override
    public Subject<Goal> find(int id){
        var entityLiveData = goalDao.findAsLiveData(id);
        var goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }

    /**
     * This methods returns a subject of a list of all the goals
     * @return subject of the list of all the goals
     */
    @Override
    public Subject<List<Goal>> findAll(){
        var entitiesLiveData = goalDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getPendingGoals() {
        var entitiesLiveData = goalDao.getPending();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getRecurringGoals() {
        var entitiesLiveData = goalDao.getRecurring();
        if (entitiesLiveData.getValue() == null || entitiesLiveData.getValue().isEmpty()){
            return new SimpleSubject<List<Goal>>();
        }
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsIncomplete() {
        var entitiesLiveData = goalDao.getRecurringIncomplete();
        if (entitiesLiveData.getValue() == null || entitiesLiveData.getValue().isEmpty()){
            return new SimpleSubject<List<Goal>>();
        }
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsComplete() {
        var entitiesLiveData = goalDao.getRecurringComplete();
        if (entitiesLiveData.getValue() == null || entitiesLiveData.getValue().isEmpty()){
            return new SimpleSubject<List<Goal>>();
        }
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getGoalsByDayIncomplete(int year, int month, int day) {
        var entitiesLiveData = goalDao.getByDayIncomplete(year, month, day);
        if (entitiesLiveData.getValue() == null || entitiesLiveData.getValue().isEmpty()){
            return new SimpleSubject<List<Goal>>();
        }
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getGoalsByDayComplete(int year, int month, int day) {
        var entitiesLiveData = goalDao.getByDayComplete(year, month, day);
        if (entitiesLiveData.getValue() == null || entitiesLiveData.getValue().isEmpty()){
            return new SimpleSubject<List<Goal>>();
        }
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getGoalsByDay(int year, int month, int day) {
        var entitiesLiveData = goalDao.getByDay(year, month, day);
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    /*
    need to use this to display goals for today
     */
    @Override
    public Subject<List<Goal>> getGoalsLessThanOrEqualToDay(int year, int month, int day) {
        var entitiesLiveData = goalDao.getLessThanOrEqualToDay(year, month, day);
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsByDay(int year, int month, int day) {
        var entitiesLiveData = goalDao.getRecurringByDay(year, month, day);
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> getRecurringGoalsByDayComplete(int year, int month, int day) {
        var entitiesLiveData = goalDao.getRecurringByDayComplete(year, month, day);
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }
    /**
     * This method appends a goal to the list of goals
     * @param goal to be appended
     */
    public void append(Goal goal){
        goalDao.append(GoalEntity.fromGoal(goal));
    }

    /**
     * This method prepends a goal to the list of goals
     * @param goal to be prepended
     */
    public void prepend(Goal goal){
        goalDao.prepend(GoalEntity.fromGoal(goal));
    }

    /**
     * This method removes a goal given a specific goal id
     * @param id of goal to remove
     */
    public void remove(int id){
        goalDao.delete(id);
    }

    /**
     * This method clears the entire goal list
     */
    public void deleteCompleted(){
        goalDao.deleteComplete();
    }

    @Override
    public void getContextHome() {
        goalDao.getPending();
    }

    @Override
    public void deleteCompleted(int year, int monthValue, int dayOfMonth) {
        goalDao.deleteComplete();
    }

    @Override
    public void getContextSchool() {

    }

    @Override
    public void InsertWithSortOrder(Goal goal, int sortOrder) {
        goalDao.append(GoalEntity.fromGoal(goal));
    }

    @Override
    public void InsertWithSortOrderAndRecurring(Goal goal, int sortOrder, String recurring) {
        goalDao.append(GoalEntity.fromGoal(goal));
    }

    @Override
    public Subject<Object> findListOfGoalsById(int id) {
        return null;
    }

    public boolean isGoalsEmpty(){ return goalDao.isGoalsEmpty(); }

    public void removeGoalComplete(int id){ goalDao.removeGoalComplete(id); }

    public void removeGoalIncomplete(int id){ goalDao.removeGoalIncomplete(id); }

    public void appendComplete(Goal goal){ goalDao.appendComplete(GoalEntity.fromGoal(goal)); }

    public void appendIncomplete(Goal goal){ goalDao.appendIncomplete(GoalEntity.fromGoal(goal)); }


}