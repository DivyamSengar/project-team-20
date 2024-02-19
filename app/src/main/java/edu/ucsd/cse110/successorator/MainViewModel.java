package edu.ucsd.cse110.successorator;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.time.LocalDateTime;
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

/**
 * The main view model for the application
 */
public class MainViewModel extends ViewModel {

    // An initializer for the MainViewModel
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

    /**
     * Constructor for MainViewModel
     *
     * @param goalRepositoryComplete -
     * @param goalRepositoryIncomplete -
     * @param timeKeeper -
     */
    public MainViewModel(GoalRepository goalRepositoryComplete,
                         GoalRepository goalRepositoryIncomplete, TimeKeeper timeKeeper) {

        this.goalRepositoryComplete = goalRepositoryComplete;
        this.goalRepositoryIncomplete = goalRepositoryIncomplete;
        this.timeKeeper = timeKeeper;

        // Observables
        this.goals = new SimpleSubject<>();
        this.isGoalsEmpty = new SimpleSubject<>();
        this.goalsCompleted = new SimpleSubject<>();
        this.goalsIncompleted = new SimpleSubject<>();
        this.isCompletedGoalsEmpty = new SimpleSubject<>();
        this.isIncompletedGoalsEmpty = new SimpleSubject<>();

        // Setting empty booleans to true upon initialization
        isGoalsEmpty.setValue(true);
        isCompletedGoalsEmpty.setValue(true);
        isIncompletedGoalsEmpty.setValue(true);

        // When the repository of completed goals changes, get the completed goals
        this.goalRepositoryComplete.findAll().observe(newGoals -> {
            // Gets all of the goals in the completed goals repository
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

        // When the repository of incomplete goals changes, get the incomplete goals
        this.goalRepositoryIncomplete.findAll().observe(newGoals -> {
            // Gets all of the goals in the incomplete goals repository
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

        // Listens for if goals is empty
        this.goals.observe(gs -> {
            if (gs == null) return;
            isGoalsEmpty.setValue(gs.isEmpty());
        });

        // When the list of incomplete goals change, update the goals to reflect this change
        this.goalsIncompleted.observe(gs -> {
            if (gs == null) return;
            isIncompletedGoalsEmpty.setValue(gs.isEmpty());

            if (goalsCompleted.getValue() == null){
                goalsCompleted.setValue(List.of());
            }

            goals.setValue(Stream.concat(gs.stream(), goalsCompleted.getValue().stream())
                    .collect(Collectors.toList()));
        });

        // When the list of complete goals change, update the goals to reflect this change
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

    /**
     * Getter for the Subject of all goals, both incomplete and complete
     *
     * @return Subject with a value containing a list of all goals
     */
    public Subject<List<Goal>> getGoals() {
        return goals;
    }

    /**
     * Checks if list of goals is empty
     *
     * @return Subject with a value indicating whether or not list of goals is empty
     */
    public Subject<Boolean> isGoalsEmpty() {
        return isGoalsEmpty;
    }

    /**
     * Removes a specified goal from the repository of completed goals
     *
     * @param id - id of the goal to be removed
     */
    public void removeGoalComplete (int id){
        goalRepositoryComplete.remove(id);
    }

    /**
     * Removes a specified goal from the repository of incomplete goals
     *
     * @param id - id of the goal to be removed
     */
    public void removeGoalIncomplete (int id){
        goalRepositoryIncomplete.remove(id);
    }

    /**
     * Appends a goal to the repository of completed goals
     *
     * @param goal - the goal to be appended
     */
    public void appendComplete(Goal goal){
        goalRepositoryComplete.append(goal);
        this.goalsCompleted.setValue(goalRepositoryComplete.findAll().getValue());
    }

    /**
     * Appends a goal to the repository of incomplete goals
     *
     * @param goal - the goal to be appended
     */
    public void appendIncomplete(Goal goal){
        goalRepositoryIncomplete.append(goal);
        this.goalsIncompleted.setValue(goalRepositoryIncomplete.findAll().getValue());
    }

    /**
     * Prepends a goal to the repository of completed goals
     *
     * @param goal - the goal to be prepended
     */
    public void prependIncomplete(Goal goal){
        goalRepositoryIncomplete.prepend(goal);
    }

    /**
     * Deletes all of the completed goals
     */
    public void deleteCompleted(){
        goalRepositoryComplete.deleteCompleted();
    }

    /**
     * Appends a time to the time database
     *
     * @param localDateTime - the time/date of when last previously opened app
     */
    public void appendTime(LocalDateTime localDateTime){
        timeKeeper.setDateTime(localDateTime);
    }

    /**
     * Deletes time from the time keeper database
     */
    public void deleteTime(){
        timeKeeper.removeDateTime();
    }

    /**
     * Getter for the fields of the time the app was last opened in the database
     *
     * @return Array of fields such as hours, minutes, etc. for the time
     */
    public int[] getFields() {
        return timeKeeper.getFields();
    }

}
