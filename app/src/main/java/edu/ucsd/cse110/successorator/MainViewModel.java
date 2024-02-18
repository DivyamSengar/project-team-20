package edu.ucsd.cse110.successorator;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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
                        return new MainViewModel(app.getGoalRepository());
                    });
    private final GoalRepository goalRepository;
    private MutableSubject<List<Goal>> goals;
    private MutableSubject<Boolean> isGoalsEmpty;
//    private MutableSubject<Boolean> isComplete;

    public MainViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;

        // observables
        this.goals = new SimpleSubject<>();
        this.isGoalsEmpty = new SimpleSubject<>();
        // this.isComplete = new SimpleSubject<>();

        isGoalsEmpty.setValue(true);

//         revert back to this if something goes wrong
//        this.goalRepository.findAll().observe(newGoals -> {
//            goals.setValue(newGoals);
//        });

        this.goalRepository.findAll().observe(newGoals -> {
            if (newGoals == null) return;
            if (newGoals.size() == 0) return;
            var orderedGoals = newGoals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            goals.setValue(orderedGoals);
        });

        // potentially useful for monitoring strikethroughs
        /*
        this.isComplete.observe()
         */

        // listens for if goals is empty
        this.goals.observe(gs -> {
            if (gs == null) return;
            isGoalsEmpty.setValue(gs.isEmpty());
        });


    }

    public Subject<List<Goal>> getGoals() {
        return goals;
    }

    public Subject<Boolean> isGoalsEmpty() {
        return isGoalsEmpty;
    }

    public void addGoal(Goal goal) {
        goalRepository.save(goal);
    }

    // markAsIncomplete
    public void remove(int id){
        goalRepository.remove(id);
    }

    public void append(Goal goal){
        goalRepository.append(goal);
    }
    public void DatabaseComplete(Goal goal){
        goalRepository.markAsComplete(goal);
    }
    public void DatabaseIncomplete(Goal goal){
        goalRepository.markAsIncomplete(goal);
    }
    public void prepend(Goal goal){
        goalRepository.prepend(goal);
    }
}
