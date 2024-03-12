package edu.ucsd.cse110.successorator;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModelTest extends TestCase {

    DataSource dataSource = DataSource.fromDefault();

    GoalRepository goals = new SimpleGoalRepository(dataSource);

    GoalRepository goalsComplete = new SimpleGoalRepository(dataSource);

    GoalRepository goalsIncomplete = new SimpleGoalRepository(dataSource);
    TimeKeeper timeKeeper;

    MainViewModel mvm = new MainViewModel(goalsComplete, goalsIncomplete, timeKeeper);

    @Before
    public void setup(){
        //all goals
        for (int i = 0; i < 4; i++){
            goals.append(dataSource.getGoalEntry(i));
        }
        //complete goals
        for (int i = 0; i < 4; i++){
            if (goals.find(i).getValue().isComplete()){
                goalsComplete.append(goals.find(i).getValue());
            }
        }
        //incomplete goals
        for (int i = 0; i < 4; i++){
            if (!goals.find(i).getValue().isComplete()){
                goalsIncomplete.append(goals.find(i).getValue());
            }
        }
    }

    public void testRollover() {
    }

    public void testIncompleteRecurrentRollover() {
    }

    public void testGetGoals() {
        Subject<List<Goal>> ds = goals.findAll();
        assertEquals(4, ds.getValue().size());
    }

    public void testGetPendingGoals() {
        Subject<List<Goal>> ds = goals.getPendingGoals();
        assertEquals(3, ds.getValue().size());
    }

    public void testGetRecurringGoalsIncomplete() {
        Subject<List<Goal>> ds = goals.getRecurringGoalsIncomplete();
        assertEquals(2, ds.getValue().size());
    }

    public void testGetRecurringGoalsComplete() {
        Subject<List<Goal>> ds = goals.getRecurringGoalsComplete();
        assertEquals(2, ds.getValue().size());
    }

    public void testGetGoalsByDayIncomplete() {
        Subject<List<Goal>> ds = goals.getGoalsByDayIncomplete(2024, 3, 30);
        assertEquals(2, ds.getValue().size());
    }

    public void testGetGoalsByDayComplete() {
        Subject<List<Goal>> ds = goals.getGoalsByDayComplete(2024, 3, 30);
        assertEquals(1, ds.getValue().size());
    }

    public void testGetGoalsLessThanOrEqualToDay() {
        Subject<List<Goal>> ds = goals.getGoalsLessThanOrEqualToDay(2024, 3, 30);
        assertEquals(4, ds.getValue().size());
    }

    public void testGetRecurringGoalsByDayComplete() {
        Subject<List<Goal>> ds = goals.getRecurringGoalsByDayComplete(2024, 3, 30);
        assertEquals(1, ds.getValue().size());
    }

    public void testIsGoalsEmpty() {
        assertEquals(false, goals.isGoalsEmpty());
    }

    public void testRemoveGoalComplete() {
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (!goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
                i--;
            }
        }
        goals.removeGoalComplete(0);
        assertEquals(1, goals.findAll().getValue().size());
    }

    public void testRemoveGoalIncomplete() {
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
                i--;
            }
        }
        goals.removeGoalIncomplete(0);
        assertEquals(1, goalsComplete.findAll().getValue().size());
    }

    public void testAppendComplete() {
        goals.appendComplete(new Goal(null, "workout", true, 0, true, "null", 0, 4, 30, 3, 2024));
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (!goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
                i--;
            }
        }
        assertEquals(3, goals.findAll().getValue().size());
    }

    public void testAppendIncomplete() {
        goals.appendIncomplete(new Goal(null, "workout", false, 0, true, "null", 0, 4, 30, 3, 2024));
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
                i--;
            }
        }
        assertEquals(2, goals.findAll().getValue().size());
    }

    public void testPrependIncomplete() {
        goals.prepend(new Goal(null, "workout", false, 5, true, "null", 0, 4, 30, 3, 2024));
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
                i--;
            }
        }
        assertEquals(3, goals.findAll().getValue().size());
    }

    public void testDeleteCompleted() {
        goals.deleteCompleted();
        assertEquals(2, goals.findAll().getValue().size());
    }

    public void testAppendTime() {
    }

    public void testDeleteTime() {
    }

    public void testGetFields() {
    }
}