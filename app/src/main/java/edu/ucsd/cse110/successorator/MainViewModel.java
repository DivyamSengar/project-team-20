package edu.ucsd.cse110.successorator;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
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
                        return new MainViewModel(app.getGoalRepositoryComplete(), app.getGoalRepositoryIncomplete());
                    });
    private final GoalRepository goalRepositoryComplete;
    private final GoalRepository goalRepositoryIncomplete;
    private MutableSubject<List<Goal>> goals;
    private MutableSubject<Boolean> isGoalsEmpty;

    private MutableSubject<List<Goal>> goalsCompleted;

    private MutableSubject<List<Goal>> goalsIncompleted;

    private MutableSubject<Boolean> isCompletedGoalsEmpty;

    private MutableSubject<Boolean> isIncompletedGoalsEmpty;

    private List<Goal> current;
//    private MutableSubject<Boolean> isComplete;

    public MainViewModel(GoalRepository goalRepositoryComplete, GoalRepository goalRepositoryIncomplete) {
        this.goalRepositoryComplete = goalRepositoryComplete;
        this.goalRepositoryIncomplete = goalRepositoryIncomplete;
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

//         revert back to this if something goes wrong
//        this.goalRepository.findAll().observe(newGoals -> {
//            goals.setValue(newGoals);
//        });

        this.goalRepositoryComplete.findAll().observe(newGoals -> {
            List<Goal> orderedGoals = List.of();
//            if (newGoals == null) return;
//            if (newGoals.size() == 0) return;
            if (newGoals == null) ;
            else if (newGoals.size() == 0);
            else {
                orderedGoals = newGoals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }

//            current = orderedGoals;
            goalsCompleted.setValue(orderedGoals);
        });
//        if (current == null) current = List.of();
        this.goalRepositoryIncomplete.findAll().observe(newGoals -> {
            List<Goal> orderedGoals = List.of();
            if (newGoals == null) ;
            else if (newGoals.size() == 0);
            else {
                orderedGoals = newGoals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            var temp = Stream.concat(current.stream(), orderedGoals.stream());
//            var temp2 = temp.collect(Collectors.toList());
            goalsIncompleted.setValue(orderedGoals);
//            goals.setValue(temp2);
        });

        // potentially useful for monitoring strikethroughs
        /*
        this.isComplete.observe()
         */

        // listens for if goals is empty
        this.goals.observe(gs -> {
            if (gs == null) return;
//            if (gs.size() == 0 ){
//                isGoalsEmpty.setValue(true);
//                return;
//            }
            isGoalsEmpty.setValue(gs.isEmpty());
        });

//        this.goalRepositoryIncomplete.observe(gs)

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

    public Subject<List<Goal>> getCompletedGoals() {
        return goalsCompleted;
    }

    public Subject<List<Goal>> getIncompletedGoals() {
        return goalsIncompleted;
    }


    public Subject<Boolean> isGoalsEmpty() {
        return isGoalsEmpty;
    }

    public Subject<Boolean> isCompletedGoalsEmpty() {
        return isCompletedGoalsEmpty;
    }

    public Subject<Boolean> isIncompletedGoalsEmpty() {
        return isIncompletedGoalsEmpty;
    }

    public void addGoalComplete (Goal goal) {
        goalRepositoryComplete.save(goal);
    }

    public void addGoalIncomplete (Goal goal) {
        goalRepositoryIncomplete.save(goal);
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

    public void prependComplete(Goal goal){
        goalRepositoryComplete.prepend(goal);
    }

    public void prependIncomplete(Goal goal){
        goalRepositoryIncomplete.prepend(goal);
    }

//    public void DatabaseComplete(Goal goal){
//        goalRepository.markAsComplete(goal);
//    }
//    public void DatabaseIncomplete(Goal goal){
//        goalRepository.markAsIncomplete(goal);
//    }
    public void deleteCompleted(){
        goalRepositoryComplete.deleteCompleted();
    }
}
