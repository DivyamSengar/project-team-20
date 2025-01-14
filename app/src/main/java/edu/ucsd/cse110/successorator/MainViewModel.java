package edu.ucsd.cse110.successorator;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import edu.ucsd.cse110.successorator.lib.domain.ContextRepository;
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
                                app.getGoalRepositoryIncomplete(), app.getGoalRepositoryRecurring(),
                                app.getTimeKeeper(), app.getActualTimeKeeper(), app.getContextRepository());
                    });
    private final GoalRepository goalRepositoryComplete;


    private  LocalDateTime todayTime;
    private final GoalRepository goalRepositoryIncomplete;

    private final GoalRepository goalRepositoryRecurring;

    public final TimeKeeper timeKeeper;

    public final TimeKeeper ActualTimeKeeper;

    private final ContextRepository contextRepository;
    private MutableSubject<List<Goal>> goals;
    private MutableSubject<Boolean> isGoalsEmpty;

    private MutableSubject<List<Goal>> goalsCompleted;

    private MutableSubject<List<Goal>> goalsIncompleted;

    private MutableSubject<Boolean> isCompletedGoalsEmpty;

    private MutableSubject<Boolean> isIncompletedGoalsEmpty;

    private MutableSubject<List<Goal>> recurringGoals;

    private MutableSubject<List<Goal>> FocusList;
    /**
     * Constructor for MainViewModel
     *
     * @param goalRepositoryComplete -
     * @param goalRepositoryIncomplete -
     * @param timeKeeper -
     */
    public MainViewModel(GoalRepository goalRepositoryComplete,
                         GoalRepository goalRepositoryIncomplete, GoalRepository goalRepositoryRecurring,
                         TimeKeeper timeKeeper, TimeKeeper ActualTimeKeeper, ContextRepository contextRepository) {

        this.goalRepositoryComplete = goalRepositoryComplete;
        this.goalRepositoryIncomplete = goalRepositoryIncomplete;
        this.goalRepositoryRecurring = goalRepositoryRecurring;
        this.timeKeeper = timeKeeper;
        this.ActualTimeKeeper = ActualTimeKeeper;
        this.contextRepository = contextRepository;

        // Observables
        this.goals = new SimpleSubject<>();
        this.isGoalsEmpty = new SimpleSubject<>();
        this.goalsCompleted = new SimpleSubject<>();
        this.goalsIncompleted = new SimpleSubject<>();
        this.isCompletedGoalsEmpty = new SimpleSubject<>();
        this.isIncompletedGoalsEmpty = new SimpleSubject<>();
        this.recurringGoals = new SimpleSubject<>();

        // Setting empty booleans to true upon initialization
        isGoalsEmpty.setValue(true);
        isCompletedGoalsEmpty.setValue(true);
        isIncompletedGoalsEmpty.setValue(true);

        this.goalRepositoryRecurring.findAll().observe(newGoals -> {
            List<Goal> orderedGoals = List.of();
            if (newGoals == null) return;
            else if (newGoals.size() == 0);
            else {
                orderedGoals = newGoals.stream()
                        .sorted(Comparator.comparingInt(Goal::getYear).thenComparing(Goal::getMonth)
                                .thenComparing(Goal::getDay).thenComparing(Goal::getHour).thenComparing(Goal::getMinutes))
                        .collect(Collectors.toList());
                ArrayList<Goal> copyList = new ArrayList<>();
                copyList.addAll(orderedGoals);
                int con = getCurrentContextValue();
                if (con != 0){
                    for (var otherGoal : copyList){
                        if (otherGoal.getContext() != con )orderedGoals.remove(otherGoal);
                    }
                }
            }
            recurringGoals.setValue(orderedGoals);
        });

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
                ArrayList<Goal> copyList = new ArrayList<>();
                copyList.addAll(orderedGoals);
                int con = getCurrentContextValue();
                if (con != 0){
                    for (var otherGoal : copyList){
                        if (otherGoal.getContext() != con )orderedGoals.remove(otherGoal);
                    }
                }
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
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
                ArrayList<Goal> copyList = new ArrayList<>();
                copyList.addAll(orderedGoals);
                int con = getCurrentContextValue();
                if (con != 0){
                    for (var otherGoal : copyList){
                        if (otherGoal.getContext() != con )orderedGoals.remove(otherGoal);
                    }
                }
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
    public void rollover(LocalDateTime currentTime, LocalDateTime previousTime) {
        System.out.println("Rollover starts here, blud");
        // Current time and time that the app was last opened
        currentTime = getTodayTime();
        int lastOpenedHour = this.getFieldsForLastDate()[3];
        int lastOpenedMinute = this.getFieldsForLastDate()[4];
        int lastDay = this.getFieldsForLastDate()[2];
        int lastMonth =this.getFieldsForLastDate()[1];
        int lastYear = this.getFieldsForLastDate()[0];

        LocalDateTime previous = LocalDateTime.of(lastYear, lastMonth,
                lastDay, lastOpenedHour, lastOpenedMinute);

        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int currDay = currentTime.getDayOfMonth();
        int currMonth = currentTime.getMonthValue();
        int currYear = currentTime.getYear();

        // If current time is at least 24 hours ahead, perform completed goals deletion
        var minus24 = currentTime.minusHours(24);
        if(minus24.isAfter(previous)){
            this.deleteCompleted(currentTime, previousTime);
            IncompleteRecurrentRollover(currentTime, previousTime);
            System.out.println("Actual rollover ran");
        }
        else if (minus24.isEqual(previous)){
            this.deleteCompleted(currentTime, previousTime);
            IncompleteRecurrentRollover(currentTime, previousTime);
            System.out.println("Actual rollover ran");
        }
        else if (currentTime.isBefore(previous));
        else if (currDay > lastDay) {
            if ((lastDay + 1) < currDay) {
                this.deleteCompleted(currentTime, previousTime);
                IncompleteRecurrentRollover(currentTime, previousTime);
                System.out.println("Actual rollover ran");
            } else {
                if (hour >= 2) {
                    this.deleteCompleted(currentTime, previousTime);
                    IncompleteRecurrentRollover(currentTime, previousTime);
                    System.out.println("Actual rollover ran");
                }
            }
        }
        else {
            if ((hour >= 2)
                    && (lastOpenedHour <= 2)) {
                this.deleteCompleted(currentTime, previousTime);
                IncompleteRecurrentRollover(currentTime, previousTime);
                System.out.println("Actual rollover ran");
            }
        }
        this.deleteTime();
        this.appendTime(currentTime);
    }
    // updates dates of boundary recurring incomplete goals if necessary
    public void IncompleteRecurrentRollover(LocalDateTime currentTime, LocalDateTime previousTime){
        var list = getRecurringGoalsIncomplete();
        if (list == null || list.isEmpty()){
            System.out.println("incomplete recurring list empty");
            return;
        }
        LocalDateTime today = getTodayTime();
        for (var goal : list){
            System.out.println("This is the incomplete recurring goal" + goal.getText());
            if (goal.getRecurring() == 1){
                continue;
//                var pairedList = goalRepositoryIncomplete.getGoalPairVals(goal.getGoalPair());
//                if
//                goalRepositoryIncomplete.remove(goal.id());
//                goal.updateRecurring();
//                goalRepositoryIncomplete.append(goal);
            }
            else if (today.isBefore(goal.getBoundaryRecurringDate())){
            }
            else {
                System.out.println("incomplete recurring goal should have been updated");
                goalRepositoryIncomplete.remove(goal.id());
                goal.updateRecurring(currentTime, previousTime);
                goalRepositoryIncomplete.append(goal);
            }
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

    public Subject<List<Goal>> getPendingGoals() {
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> pendingGoals = new SimpleSubject<>();

        goalRepositoryIncomplete.getPendingGoals().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
                ArrayList<Goal> copyList = new ArrayList<>();
                copyList.addAll(goalList);
                int con = getCurrentContextValue();
                if (con != 0){
                    for (var otherGoal : copyList){
                        if (otherGoal.getContext() != con )goalList.remove(otherGoal);
                    }
                }
            }
            incomplete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;
            List<Goal> goalList = List.of();

            pendingGoals.setValue(Stream.concat(goals.stream(), goalList.stream())
                    .collect(Collectors.toList())
            );
        });

        return pendingGoals;
    }

    public List<Goal> getRecurringGoalsIncomplete() {
        return goalRepositoryIncomplete.getRecurringGoals();
    }

    public List<Goal> getRecurringGoalsComplete() {
        return goalRepositoryComplete.getRecurringGoals();
    }

    public Subject<List<Goal>> getGoalsByDayIncomplete(int year, int month, int day) {return goalRepositoryIncomplete.getGoalsByDay(year, month, day);}

    public Subject<List<Goal>> getGoalsByDayComplete(int year, int month, int day) {return goalRepositoryComplete.getGoalsByDay(year, month, day);}
    public Subject<List<Goal>> getGoalsByDay(int year, int month, int day){
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> goalsForDay = new SimpleSubject<>();

        goalRepositoryIncomplete.getGoalsByDay(year, month, day).observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
                ArrayList<Goal> copyList = new ArrayList<>();
                copyList.addAll(goalList);
                int con = getCurrentContextValue();
                if (con != 0){
                    for (var otherGoal : copyList){
                        if (otherGoal.getContext() != con )goalList.remove(otherGoal);
                    }
                }
            }
//            System.out.println("Incomplete size" + goalList.size());
            incomplete.setValue(goalList);
        });

        goalRepositoryComplete.getGoalsByDay(year, month, day).observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
//                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
                ArrayList<Goal> copyList = new ArrayList<>();
                copyList.addAll(goalList);
                int con = getCurrentContextValue();
                if (con != 0){
                    for (var otherGoal : copyList){
                        if (otherGoal.getContext() != con )goalList.remove(otherGoal);
                    }
                }
            }
//            System.out.println("Incomplete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            goalsForDay.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(goalsForDay.getValue().size());
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            goalsForDay.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(goalsForDay.getValue().size());
        });

        return goalsForDay;

    }
//    public Subject<List<Goal>> getRecurringGoals(){
//        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
//        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
//        MutableSubject<List<Goal>> recurring = new SimpleSubject<>();
//
//        goalRepositoryIncomplete.getRecurringGoals().observe(goals -> {
//            List<Goal> goalList = List.of();
//            if (goals == null){}
//            else if (goals.size() == 0){} else {
//                goalList = goals.stream()
//                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
//                        .collect(Collectors.toList());
//            }
//            incomplete.setValue(goalList);
////            System.out.println("incomp recurring size" + incomplete.getValue().size());
//        });
//
//        goalRepositoryComplete.getRecurringGoals().observe(goals -> {
//            List<Goal> goalList = List.of();
//            if (goals == null){}
//            else if (goals.size() == 0){} else {
//                goalList = goals.stream()
//                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
//                        .collect(Collectors.toList());
//            }
////            System.out.println("Complete size" + goalList.size());
//            complete.setValue(goalList);
//        });
//
//        incomplete.observe(goals -> {
//            if (goals == null) return;
//
//            if (complete.getValue() == null){
//                complete.setValue(List.of());
//            }
//
//            recurring.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
//                    .collect(Collectors.toList())
//            );
//        });
//
//        complete.observe(goals -> {
//            if (goals == null) return;
//
//            if (incomplete.getValue() == null){
//                incomplete.setValue(List.of());
//            }
//
//            recurring.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
//                    .collect(Collectors.toList())
//            );
//
////            System.out.println("Recurring size" + recurring.getValue().size());
//        });
//
//        return recurring;
//    }

    public Subject<List<Goal>> getGoalsLessThanOrEqualToDay(int year, int month, int day) {
//        var first = goalRepositoryIncomplete.getGoalsLessThanOrEqualToDay(year, month, day);
//        var second = goalRepositoryComplete.getGoalsLessThanOrEqualToDay(year, month, day);

        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> goalsForDay = new SimpleSubject<>();

        goalRepositoryIncomplete.getGoalsLessThanOrEqualToDay(year, month, day).observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
                ArrayList<Goal> copyList = new ArrayList<>();
                copyList.addAll(goalList);
                int con = getCurrentContextValue();
                if (con != 0){
                    for (var otherGoal : copyList){
                        if (otherGoal.getContext() != con )goalList.remove(otherGoal);
                    }
                }
            }
//            System.out.println("Incomplete size" + goalList.size());
            incomplete.setValue(goalList);
        });

        goalRepositoryComplete.getGoalsLessThanOrEqualToDay(year, month, day).observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
//                        .sorted(Comparator.comparingInt(Goal::getContext).thenComparing(Goal::sortOrder))
                        .collect(Collectors.toList());
                ArrayList<Goal> copyList = new ArrayList<>();
                copyList.addAll(goalList);
                int con = getCurrentContextValue();
                if (con != 0){
                    for (var otherGoal : copyList){
                        if (otherGoal.getContext() != con )goalList.remove(otherGoal);
                    }
                }
            }
//            System.out.println("Complete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            goalsForDay.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(goalsForDay.getValue().size());
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            goalsForDay.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(goalsForDay.getValue().size());
        });

        return goalsForDay;
    }

    public Subject<List<Goal>> getRecurringGoalsByDayComplete(int year, int month, int day) {return goalRepositoryComplete.getRecurringGoalsByDay(year, month, day);}

    public Subject<List<Goal>> getContext(Subject<List<Goal>> listOfGoals, int context){
        // if there is no context set in focus mode or the cancel button has been hit, then don't sort by anything
        if (context == 0) return listOfGoals;
        MutableSubject<List<Goal>> contextGoals = new SimpleSubject<List<Goal>>();
        contextGoals.setValue(List.of());
        listOfGoals.observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .filter(goal -> goal.getContext() == context)
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }

            contextGoals.setValue(goalList);

        });
        return contextGoals;
    }


    public Subject<List<Goal>> getContextHome() {
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> contextHome = new SimpleSubject<>();

        goalRepositoryIncomplete.getContextHome().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Incomplete size" + goalList.size());
            incomplete.setValue(goalList);
        });

        goalRepositoryComplete.getContextHome().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Complete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            contextHome.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(contextHome.getValue().size());
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            contextHome.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(contextHome.getValue().size());
        });

        return contextHome;
    }

    public Subject<List<Goal>> getContextWork() {
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> contextWork = new SimpleSubject<>();

        goalRepositoryIncomplete.getContextWork().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Incomplete size" + goalList.size());
            incomplete.setValue(goalList);
        });

        goalRepositoryComplete.getContextWork().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Complete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            contextWork.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(contextWork.getValue().size());
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            contextWork.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(contextWork.getValue().size());
        });

        return contextWork;
    }

    public Subject<List<Goal>> getContextSchool() {
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> contextSchool = new SimpleSubject<>();

        goalRepositoryIncomplete.getContextSchool().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Incomplete size" + goalList.size());
            incomplete.setValue(goalList);
        });

        goalRepositoryComplete.getContextSchool().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Complete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            contextSchool.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(contextSchool.getValue().size());
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            contextSchool.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(contextSchool.getValue().size());
        });

        return contextSchool;
    }

    public Subject<List<Goal>> getContextErrands() {
        MutableSubject<List<Goal>> incomplete = new SimpleSubject<>();
        MutableSubject<List<Goal>> complete = new SimpleSubject<>();
        MutableSubject<List<Goal>> contextErrands = new SimpleSubject<>();

        goalRepositoryIncomplete.getContextErrands().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Incomplete size" + goalList.size());
            incomplete.setValue(goalList);
        });

        goalRepositoryComplete.getContextErrands().observe(goals -> {
            List<Goal> goalList = List.of();
            if (goals == null){}
            else if (goals.size() == 0){} else {
                goalList = goals.stream()
                        .sorted(Comparator.comparingInt(Goal::sortOrder))
                        .collect(Collectors.toList());
            }
//            System.out.println("Complete size" + goalList.size());
            complete.setValue(goalList);
        });

        incomplete.observe(goals -> {
            if (goals == null) return;

            if (complete.getValue() == null){
                complete.setValue(List.of());
            }

            contextErrands.setValue(Stream.concat(goals.stream(), complete.getValue().stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(contextErrands.getValue().size());
        });

        complete.observe(goals -> {
            if (goals == null) return;

            if (incomplete.getValue() == null){
                incomplete.setValue(List.of());
            }

            contextErrands.setValue(Stream.concat(incomplete.getValue().stream(), goals.stream())
                    .collect(Collectors.toList())
            );

//            System.out.println(contextErrands.getValue().size());
        });

        return contextErrands;
    }

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
        var arg1 = goalRepositoryComplete.findAll().getValue();
//        if (arg1 == null) {
//            this.goalsCompleted.setValue(List.of());
//            return;
//        }
//        var list1 = new ArrayList<>(goalRepositoryComplete.findAll().getValue());
//        if (getCurrentContextValue() != 0) {
//            var copy = new ArrayList<Goal>();
//            for (var goal : list1) {
//                copy.add(goal);
//            }
//            for (var goal : copy) {
//                if (goal.getContext() != getCurrentContextValue()) list1.remove(goal);
//            }
//        }
//        MutableSubject<List<Goal>> subj1 = new SimpleSubject<List<Goal>>();
//        subj1.setValue(list1);
//        this.goalsIncompleted = getContext(subj1, getCurrentContextValue());
        this.goalsCompleted.setValue(arg1);

    }

    /**
     * Removes a specified goal from the repository of incomplete goals
     *
     * @param id - id of the goal to be removed
     */
    public void removeGoalIncomplete (int id){
        goalRepositoryIncomplete.remove(id);
        var arg1 = goalRepositoryIncomplete.findAll().getValue();
//        if (arg1 == null) {
//            this.goalsIncompleted.setValue(List.of());
//            return;
//        }
//        var list1 = new ArrayList<>(goalRepositoryIncomplete.findAll().getValue());
//        if (getCurrentContextValue() != 0) {
//            var copy = new ArrayList<Goal>();
//            for (var goal : list1) {
//                copy.add(goal);
//            }
//            for (var goal : copy) {
//                if (goal.getContext() != getCurrentContextValue()) list1.remove(goal);
//            }
//        }
//        MutableSubject<List<Goal>> subj1 = new SimpleSubject<List<Goal>>();
//        subj1.setValue(list1);
//        this.goalsIncompleted = getContext(subj1, getCurrentContextValue());
        this.goalsIncompleted.setValue(arg1);

    }

    /**
     * Appends a goal to the repository of completed goals
     *
     * @param goal - the goal to be appended
     */
    public void appendComplete(Goal goal){
        goalRepositoryComplete.append(goal);
        var arg1 = goalRepositoryComplete.findAll().getValue();
//        if (arg1 == null) {
//            this.goalsCompleted.setValue(List.of());
//            return;
//        }
//        var list1 = new ArrayList<>(goalRepositoryComplete.findAll().getValue());
//        if (getCurrentContextValue() != 0) {
//            var copy = new ArrayList<Goal>();
//            for (var goal2 : list1) {
//                copy.add(goal2);
//            }
//            for (var goal2 : copy) {
//                if (goal2.getContext() != getCurrentContextValue()) list1.remove(goal2);
//            }
//        }
        this.goalsCompleted.setValue(arg1);
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
        var arg1 = goalRepositoryIncomplete.findAll().getValue();
//        if (arg1 == null) {
//            this.goalsIncompleted.setValue(List.of());
//            return;
//        }
//        var list1 = new ArrayList<>(goalRepositoryIncomplete.findAll().getValue());
//        if (getCurrentContextValue() != 0) {
//            var copy = new ArrayList<Goal>();
//            for (var goal2 : list1) {
//                copy.add(goal2);
//            }
//            for (var goal2 : copy) {
//                if (goal2.getContext() != getCurrentContextValue()) list1.remove(goal2);
//            }
//        }
        this.goalsIncompleted.setValue(arg1);

    }

    /**
     * Deletes all of the one-time completed goals and updates any
     * recurring completed goals to their next recurring date as incomplete goals
     *
     * NEW ASSUMPTION (IMPORTANT): When doing the rollover, should the recurring goals get added
     * first or should the incompleted goals get rolld over first? The order of the goals changes based on this
     */
    public void deleteCompleted(LocalDateTime currentTime, LocalDateTime previousTime){
        LocalDateTime valueToUse = getTodayTime().minusDays(1);
        var recGoals = goalRepositoryComplete.getRecurringGoals();
        ArrayList<Goal> toAdd = new ArrayList<>();
        for (var goal : recGoals){
            System.out.println("this is the rec goal in del completed" + goal.getText());
            toAdd.add(goal);
        }
        var incompleteDailyGoals = goalRepositoryIncomplete.getRecurringGoals();
        for (var g : incompleteDailyGoals){
            if (g.getRecurring() == 1) toAdd.add(g);
        }
        if (toAdd == null || toAdd.isEmpty()){

            System.out.println("delete completed recurring goals list null");
            goalRepositoryComplete.deleteCompletedNonDaily(valueToUse.getYear(), valueToUse.getMonthValue(), valueToUse.getDayOfMonth());
//            System.out.println(recGoals.getValue().toString());
            return;
        }
        goalRepositoryComplete.deleteCompletedNonDaily(valueToUse.getYear(), valueToUse.getMonthValue(), valueToUse.getDayOfMonth());
        goalsCompleted.setValue(goalRepositoryComplete.findAll().getValue());
        for (var goal : toAdd){
            if (goal.getRecurring() == 1){
                System.out.println("ran this amount of times");
                var pairedGoals = getGoalPairValyes(goal.getGoalPair());
                System.out.println(pairedGoals.size() + "paired goals size");
                if (pairedGoals.size() <= 1) continue;
                Goal today = pairedGoals.get(0);
                Goal tomorrow = pairedGoals.get(1);

                if (today.getDay() > tomorrow.getDay()){
                    var temp = tomorrow;
                    tomorrow = today;
                    today = temp;
                }
                if (today.getDay() >= getTodayTime().getDayOfMonth() &&
                        tomorrow.getDay() >= getTodayTime().getDayOfMonth()){
                    System.out.println("skipped because greater>=");
                    continue;
                }
                if (today.isComplete() && tomorrow.isComplete()){
                    System.out.println("This one! Variation 1");
                    today.updateRecurring(currentTime, previousTime);
                    goalRepositoryComplete.remove(today.id());
                    goalRepositoryComplete.append(today);
                    tomorrow.makeInComplete();
                    System.out.println(tomorrow.getDay() + tomorrow.getText());
                    tomorrow.updateRecurring(currentTime, previousTime);
                    goalRepositoryComplete.remove(tomorrow.id());
                    appendIncomplete(tomorrow);

                }
                else if (today.isComplete() && !tomorrow.isComplete()){
                    System.out.println("This one! Variation 2");
                    today.makeInComplete();
                    today.updateRecurring(currentTime, previousTime);
                    goalRepositoryComplete.remove(today.id());
                    goalRepositoryIncomplete.append(today);
                    tomorrow.updateRecurring(currentTime, previousTime);
                    goalRepositoryIncomplete.remove(tomorrow.id());
                    goalRepositoryIncomplete.append(tomorrow);;
                }
                else if (!today.isComplete() && tomorrow.isComplete()){
                    System.out.println("This one! Variation 3");
                    goalRepositoryIncomplete.remove(today.id());
                    today.makeComplete();
                    today.updateRecurring(currentTime, previousTime);
                    goalRepositoryComplete.append(today);
                    goalRepositoryComplete.remove(tomorrow.id());
                    tomorrow.makeInComplete();
                    tomorrow.updateRecurring(currentTime, previousTime);
                    goalRepositoryIncomplete.append(tomorrow);
                }
                else {
                    System.out.println("This one! Variation 4");
                    goalRepositoryIncomplete.remove(today.id());
                    goalRepositoryIncomplete.remove(tomorrow.id());
                    today.updateRecurring(currentTime, previousTime);
                    tomorrow.updateRecurring(currentTime, previousTime);
                    goalRepositoryIncomplete.append(today);
                    goalRepositoryComplete.append(tomorrow);
                }
                // today complete and tomorrow complete -> next today complete and next tomorrow incomplete
                // today incompltete and tomorrow incomplete -> next today incomplete and next tomorrow incomplete
                // today incomplete and tomorrow complete -> next today complete and next tomorrow incomplete
                // today complete and tomorrow incomplete -> next today incomplete and next tomorrow incomplete


                // if not equal to this one, then remove it
                // from complete/incomplete and add it to the other with the updated date

            }
            else {
                goal.makeInComplete();
                goal.updateRecurring(currentTime, previousTime);
                goalRepositoryIncomplete.append(goal);
            }
            System.out.println("added in del completed" + goal.getText());
//            goalRepositoryIncomplete.append(goal);
            this.goalsIncompleted.setValue(goalRepositoryIncomplete.findAll().getValue());
            this.goalsCompleted.setValue(goalRepositoryComplete.findAll().getValue());
        }

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
    public int[] getFieldsForLastDate() {
        return timeKeeper.getFields();
    }


    public void removeGoalFromRecurringList (int id){
        goalRepositoryRecurring.findListOfGoalsById(id).observe(goals -> {
            if (goals == null) {
                System.out.println("Null Value");
                return;
            }
            // from the recurring list goal, get its pairID
            // use the pairID to get its related recurring goals (int hte other views)
            // when I have them, delete them
            goals.forEach(goal -> {
                int pairID = goal.getGoalPair();
                var subjListOfGoalPairs = getGoalPairValyes(pairID);
                if (subjListOfGoalPairs == null){
                    System.out.println("it happened ono again bruh");
                    return;
                }
                subjListOfGoalPairs.forEach(goal1 -> {
                    int newSortOrder = goal1.sortOrder();
                    if (goal1.isComplete()) {
                        removeGoalComplete(goal1.id());
                        InsertWithSortOrderAndRecurringToRecurringListComplete(goal1, newSortOrder, 0);
                    } else {
                        removeGoalIncomplete(goal1.id());
                        InsertWithSortOrderAndRecurringToRecurringListIncomplete(goal1, newSortOrder, 0);
                    }
                });
            });
            goalRepositoryRecurring.remove(id);
        });
    }

    public void appendToRecurringList(Goal goal){
        goalRepositoryRecurring.append(goal);
    }

    public Subject<List<Goal>> getGoalsFromRecurringList(){
//        System.out.println("it printed bruh");
//        var goalSubject = goalRepositoryRecurring.findAll();
//        var goalList = goalSubject.getValue();
//        if (goalList == null) {
//            System.out.println("early return");
//            var toReturn = new SimpleSubject<List<Goal>>();
//            toReturn.setValue(List.of());
//            return toReturn;
//        }
//        goalList.stream()
//                .sorted(Comparator.comparingInt(Goal::getYear).thenComparing(Goal::getMonth)
//                        .thenComparing(Goal::getDay).thenComparing(Goal::getHour).thenComparing(Goal::getMinutes))
//                .collect(Collectors.toList());
//        for (var goal: goalSubject.getValue()){
//            System.out.println(goal.getText());
//        }
//        System.out.println("It's empty");
//        return goalSubject;
        return recurringGoals;
    }


    public void InsertWithSortOrderToRecurringListComplete(Goal goal, int sortOrder){
        goalRepositoryComplete.InsertWithSortOrder(goal, sortOrder);
    }

    public void InsertWithSortOrderToRecurringListIncomplete(Goal goal, int sortOrder){
        goalRepositoryIncomplete.InsertWithSortOrder(goal, sortOrder);
    }

    public void InsertWithSortOrderAndRecurringToRecurringListComplete(Goal goal, int sortOrder, int recurring){
        goalRepositoryComplete.InsertWithSortOrderAndRecurring(goal, sortOrder, recurring);
    }
    public void InsertWithSortOrderAndRecurringToRecurringListIncomplete(Goal goal, int sortOrder, int recurring){
        goalRepositoryIncomplete.InsertWithSortOrderAndRecurring(goal, sortOrder, recurring);
    }
    public LocalDateTime getTodayTime() {
        int[] timeFields = ActualTimeKeeper.getFields();
        return LocalDateTime.of(timeFields[0], timeFields[1], timeFields[2],
                timeFields[3], timeFields[4]);
    }

    public void updateTodayTime(LocalDateTime localDateTime){
        this.ActualTimeKeeper.removeDateTime();
        this.ActualTimeKeeper.setDateTime(localDateTime);
    }

    public void setContext(int context){
        contextRepository.setContext(context);
    }

    public void setContextWithBoolean(int context, boolean update){
        contextRepository.setContextWithBoolean(context, update);
    }

    public boolean getCurrUpdateValue(){
        return contextRepository.getCurrentUpdateValue();
    }

    public void removeContext(){
        contextRepository.removeContext();
    }

    public int getCurrentContextValue(){
        return contextRepository.getContext();
    }

    public int getMaxGoalPair(){
        int max1 =  goalRepositoryComplete.getMaxGoalPair();
        int max2 = goalRepositoryIncomplete.getMaxGoalPair();
        return Math.max(max1, max2);
    }
    List<Goal> getGoalPairValyes(int goalPair){
        var subj1 = goalRepositoryIncomplete.getGoalPairVals(goalPair);
        var subj2 = goalRepositoryComplete.getGoalPairVals(goalPair);
        System.out.println(subj2.toString() + "getGoalPairValsStrings" +subj1.toString());
        if (subj1 != null && subj2 != null) {
            subj1.addAll(subj2);
            return subj1;

        }
        else if (subj1== null && subj2 != null){
            return subj2;
        }
        else if (subj1 != null && subj2 == null){
            return subj1;
        }
        else {
            System.out.println("ran ???? manoon");
            return List.of();
        }
    }
}