package edu.ucsd.cse110.successorator;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepositoryComplete(),
                                app.getGoalRepositoryIncomplete(), app.getTimeKeeper());
                    });
    private final GoalRepository goalRepositoryComplete;
    private final GoalRepository goalRepositoryIncomplete;

    public final TimeKeeper timeKeeper;
    private MutableSubject<List<Goal>> goals;
    private MutableSubject<Boolean> isGoalsEmpty;

    private MutableSubject<List<Goal>> goalsCompleted;

    private MutableSubject<List<Goal>> goalsIncompleted;

    private MutableSubject<Boolean> isCompletedGoalsEmpty;

    private MutableSubject<Boolean> isIncompletedGoalsEmpty;

    public MainViewModel(GoalRepository goalRepositoryComplete,
                         GoalRepository goalRepositoryIncomplete, TimeKeeper timeKeeper) {
        this.goalRepositoryComplete = goalRepositoryComplete;
        this.goalRepositoryIncomplete = goalRepositoryIncomplete;
        this.timeKeeper = timeKeeper;
        // observables
        this.goals = new SimpleSubject<>();
        this.isGoalsEmpty = new SimpleSubject<>();
        this.goalsCompleted = new SimpleSubject<>();
        this.goalsIncompleted = new SimpleSubject<>();
        this.isCompletedGoalsEmpty = new SimpleSubject<>();
        this.isIncompletedGoalsEmpty = new SimpleSubject<>();

        isGoalsEmpty.setValue(true);
        isCompletedGoalsEmpty.setValue(true);
        isIncompletedGoalsEmpty.setValue(true);

        this.goalRepositoryComplete.findAll().observe(newGoals -> {
            List<Goal> orderedGoals = List.of();
            if (newGoals == null) ;
            else if (newGoals.size() == 0);
            else {
                orderedGoals = newGoals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }

            goalsCompleted.setValue(orderedGoals);
        });
        this.goalRepositoryIncomplete.findAll().observe(newGoals -> {
            List<Goal> orderedGoals = List.of();
            if (newGoals == null) ;
            else if (newGoals.size() == 0);
            else {
                orderedGoals = newGoals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
            goalsIncompleted.setValue(orderedGoals);
        });


        // listens for if goals is empty
        this.goals.observe(gs -> {
            if (gs == null) return;
            isGoalsEmpty.setValue(gs.isEmpty());
        });


        this.goalsIncompleted.observe(gs -> {
            System.out.println(gs + "RAHHHHHH");
            if (gs == null) return;
            System.out.println("ADDED BROOOO");
            isIncompletedGoalsEmpty.setValue(gs.isEmpty());

            if (goalsCompleted.getValue() == null){
                goalsCompleted.setValue(List.of());
            }

            goals.setValue(Stream.concat(gs.stream(), goalsCompleted.getValue().stream())
                    .collect(Collectors.toList()));
        });

        this.goalsCompleted.observe(gs -> {
            if (gs == null) return;
            isCompletedGoalsEmpty.setValue(gs.isEmpty());

            if (goalsIncompleted.getValue() == null){
                goalsIncompleted.setValue(List.of());
            }

            goals.setValue(Stream.concat(goalsIncompleted.getValue().stream(), gs.stream())
                    .collect(Collectors.toList()));
        });


    }

    public Subject<List<Goal>> getGoals() {
        return goals;
    }

    public Subject<Boolean> isGoalsEmpty() {
        return isGoalsEmpty;
    }

    // markAsIncomplete
    public void removeGoalComplete (int id){
        goalRepositoryComplete.remove(id);
    }

    public void removeGoalIncomplete (int id){
        goalRepositoryIncomplete.remove(id);
    }

    public void appendComplete(Goal goal){
        goalRepositoryComplete.append(goal);
        this.goalsCompleted.setValue(goalRepositoryComplete.findAll().getValue());
    }

    public void appendIncomplete(Goal goal){
        System.out.println("GOALLLLL" + goal);
        System.out.println("AGGGGGGG" + goalRepositoryIncomplete.findAll().getValue());
        goalRepositoryIncomplete.append(goal);
        this.goalsIncompleted.setValue(goalRepositoryIncomplete.findAll().getValue());
    }

    public void prependIncomplete(Goal goal){
        goalRepositoryIncomplete.prepend(goal);
    }


    public void deleteCompleted(){
        goalRepositoryComplete.deleteCompleted();
    }

    public void appendTime(LocalDateTime localDateTime){
        timeKeeper.setDateTime(localDateTime);
    }

    public void deleteTime(){
        timeKeeper.removeDateTime();
    }

    public int[] getFields() {
        return timeKeeper.getFields();
    }



}
