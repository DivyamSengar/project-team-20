package edu.ucsd.cse110.successorator;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.util.List;


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

    public MainViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
//        isGoalsEmpty.setValue(true);
        this.goals = new SimpleSubject<>();
        this.isGoalsEmpty = new SimpleSubject<>();

        isGoalsEmpty.setValue(true);

        this.goalRepository.findAll().observe(newGoals -> {
            goals.setValue(newGoals);
        });

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
}
