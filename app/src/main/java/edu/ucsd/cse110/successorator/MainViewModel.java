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

    //private MutableSubject<> whichView;

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
     * Things to Implement:
     *
     * As is stated by the assumptions doc, if a recurring goals is “rolled over” when incomplete,
     * then that is fine (it should rollover into the following days), but if it passes its repeat
     * period (for example, 7 days later for a weekly goal), then one should not see the goal twice,
     * but rather just see it once. Based on this, for recurring goals that are being rolled over, we
     * need to check if they are passing their boundary/repeating date and if so, just add/change the
     * date to adjust to the boundary. For example, if I Made a weekly goal on mar 1st and it rolled
     * over because of incompletion until mar 8th, then we should adjust the date of that goal in the
     * database to be mar 8th, instead of keeping it as mar 1st as planned.
     *
     * We need to adjust the rollover as well, so that if goals are incomplete, they are simply rolled
     * over to the next day, but you don’t add to the date until they are completed and rolled over if
     * they are recurrent goals (or until they pass their boundary period as described above). We don’t add to t
     *
     * The date for one time goals, because they don’t repeat. So, for the rollover, for one-time goals,
     * they are eventually just deleted, for recurrent goals, they are deleted and then readded with new dates,
     * where they are added either after they have been completed/deleted with the requisite new recurrent date
     * or if they pass their critical boundary/repeat stage, then they are deleted and readded with their new date
     * (with the new date simply being the previously stored date + the recurrent period/length). So, for recurrent
     * goals, if they are incomplete and rollover, we just store them with their date of instantiation/what date
     * they had based on their schedule and update it later to match the next recurrent date based on whether they
     * passed that date boundary or whether they completed before it.
     *
     * For Rollover/in general, we show date less than or equal to today for the today view (based on the rollover logic above)
     */
    public void rollover() {
        // Current time and time that the app was last opened
        LocalDateTime currentTime = LocalDateTime.now();
        int lastOpenedHour = getFields()[3];
        int lastOpenedMinute = getFields()[4];
        int lastDay = getFields()[2];
        int lastMonth = getFields()[1];
        int lastYear = getFields()[0];

        LocalDateTime previous = LocalDateTime.of(lastYear, lastMonth,
                lastDay, lastOpenedHour, lastOpenedMinute);

        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int currDay = currentTime.getDayOfMonth();
        int currMonth = currentTime.getMonthValue();
        int currYear = currentTime.getYear();

        //Handling Rollover for Recurring
        List<Goal> recurringIncompleteGoals = getRecurringGoalsIncomplete().getValue();  // Get list
        for (Goal goal : recurringIncompleteGoals) {
            LocalDateTime boundary = getBoundaryRecurringDate(goal);
            if(boundary.isBefore(currentTime)) {
                goal.setDate(boundary.getMinute(), boundary.getHour(), boundary.getDayOfMonth(), boundary.getMonthValue(), boundary.getYear());
            } else {
                if(goal.isComplete()){
                    goal.makeInComplete();
                    goal.setDate(boundary.getMinute(), boundary.getHour(), boundary.getDayOfMonth(), boundary.getMonthValue(), boundary.getYear());
                } else {
                    //do nothing because goal will automatically rollover as it is incomplete
                }
            }
            if(goal.getRecurring().equals("daily")){

            } else if(goal.getRecurring().equals("weekly")) {

            } else if(goal.getRecurring().equals("monthly")) {

            } else if(goal.getRecurring().equals("yearly")) {

            }

        }


        // If current time is at least 24 hours ahead, perform completed goals deletion
        var minus24 = currentTime.minusHours(24);
        if(minus24.isAfter(previous)){
            deleteCompleted();
        }
        else if (minus24.isEqual(previous)){
            deleteCompleted();
        }
        else if (currentTime.isBefore(previous));
        else if (currDay > lastDay) {
            if ((lastDay + 1) < currDay) {
                deleteCompleted();
            } else {
                if (hour >= 2) {
                    deleteCompleted();
                }
            }
        }
        else {
            if ((hour >= 2)
                    && (lastOpenedHour <= 2)) {
                deleteCompleted();
            }
        }
        deleteTime();
        appendTime(currentTime);
    }

    private LocalDateTime getBoundaryRecurringDate(Goal goal){
        LocalDateTime goal_Time = LocalDateTime.of(goal.getYear(),
                goal.getMonth(), goal.getDay(), goal.getHour(), goal.getMinutes());
        if(goal.getRecurring() == "daily"){
            return goal_Time.plusDays(1);
        } else if(goal.getRecurring() == "weekly") {
            return goal_Time.plusDays(7);
        } else if(goal.getRecurring() == "monthly") {
            return goal_Time.plusMonths(1);
        } else if(goal.getRecurring() == "yearly") {
            return goal_Time.plusYears(1);
        }
    }

    /**
     * Getter for the Subject of all goals, both incomplete and complete
     *
     * @return Subject with a value containing a list of all goals
     */
    public Subject<List<Goal>> getGoals() {
        return goals;
    }

    public Subject<List<Goal>> getPendingGoals() {return goalRepositoryIncomplete.getPendingGoals();}

    public Subject<List<Goal>> getRecurringGoalsIncomplete() {
        return goalRepositoryIncomplete.getRecurringGoals();
    }

    public Subject<List<Goal>> getRecurringGoalsComplete() {
        return goalRepositoryComplete.getRecurringGoals();
    }

    public Subject<List<Goal>> getGoalsByDayIncomplete(int year, int month, int day) {return goalRepositoryIncomplete.getGoalsByDay(year, month, day);}

    public Subject<List<Goal>> getGoalsByDayComplete(int year, int month, int day) {return goalRepositoryComplete.getGoalsByDay(year, month, day);}

    public Subject<List<Goal>> getGoalsLessThanOrEqualToDayIncomplete(int year, int month, int day) {return goalRepositoryIncomplete.getGoalsLessThanOrEqualToDay(year, month, day);}

    public Subject<List<Goal>> getRecurringGoalsByDayComplete(int year, int month, int day) {return goalRepositoryComplete.getRecurringGoalsByDay(year, month, day);}


    /**
     * Checks if list of goals is empty
     *
     * @return Subject with a value indicating whether or not list of goals is empty
     */
    public Subject<Boolean> isGoalsEmpty() {
        return isGoalsEmpty;
    }

    /*public Subject<Boolean> whichView() {
        return whichView;
    }
    */

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
