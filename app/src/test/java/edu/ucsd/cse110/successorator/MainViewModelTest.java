package edu.ucsd.cse110.successorator;

import android.provider.ContactsContract;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.ContextRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleContextRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.timedata.db.RoomTimeKeeper;

public class MainViewModelTest {

    DataSource dataSource;

    GoalRepository goals;

    GoalRepository goalsComplete;

    GoalRepository goalsIncomplete;

    GoalRepository goalsRecurring;
    TimeKeeper timeKeeper;

    TimeKeeper mockKeeper;

    ContextRepository context;

    MainViewModel mvm;

    @Before
    public void setup(){
        this.dataSource = DataSource.fromDefault();
        this.timeKeeper = new SimpleTimeKeeper(LocalDateTime.now());
        this.mockKeeper = new SimpleTimeKeeper(LocalDateTime.now());
        this.goals = new SimpleGoalRepository(dataSource);
        this.goalsComplete = new SimpleGoalRepository(dataSource);
        this.goalsIncomplete = new SimpleGoalRepository(dataSource);
        this.goalsRecurring = new SimpleGoalRepository(dataSource);
        this.context = new SimpleContextRepository();
        mvm = new MainViewModel(goalsComplete, goalsIncomplete, goalsRecurring, timeKeeper, mockKeeper, context);
    }

    @Test
    public void testRollover() {
        LocalDateTime now = LocalDateTime.now();
        mvm.timeKeeper.setDateTime(now.plusDays(30));
        mvm.rollover();
        Assert.assertEquals(8, mvm.getGoals().getValue().size());
    }

    @Test
    public void testIncompleteRecurrentRollover() {
        LocalDateTime now = LocalDateTime.now();
        mvm.timeKeeper.setDateTime(now.plusDays(5));
        mvm.rollover();
        Assert.assertEquals(3, mvm.getRecurringGoalsIncomplete().getValue().size());
    }

    @Test
    public void testGetGoals() {
        Subject<List<Goal>> ds = goals.findAll();
        Assert.assertEquals(4, ds.getValue().size());
    }

    @Test
    public void testGetPendingGoals() {
        Subject<List<Goal>> ds = goals.getPendingGoals();
        Assert.assertEquals(3, ds.getValue().size());
    }

    @Test
    public void testGetRecurringGoalsIncomplete() {
        Subject<List<Goal>> ds = goals.getRecurringGoalsIncomplete();
        Assert.assertEquals(2, ds.getValue().size());
    }

    @Test
    public void testGetRecurringGoalsComplete() {
        Subject<List<Goal>> ds = goals.getRecurringGoalsComplete();
        Assert.assertEquals(2, ds.getValue().size());
    }

    @Test
    public void testGetGoalsByDayIncomplete() {
        Subject<List<Goal>> ds = goals.getGoalsByDayIncomplete(2024, 3, 30);
        Assert.assertEquals(2, ds.getValue().size());
    }

    @Test
    public void testGetGoalsByDayComplete() {
        Subject<List<Goal>> ds = goals.getGoalsByDayComplete(2024, 3, 30);
        Assert.assertEquals(1, ds.getValue().size());
    }

    @Test
    public void testGetGoalsLessThanOrEqualToDay() {
        Subject<List<Goal>> ds = goals.getGoalsLessThanOrEqualToDay(2024, 4, 30);
        Assert.assertEquals(4, ds.getValue().size());
    }

    @Test
    public void testGetRecurringGoalsByDayComplete() {
        Subject<List<Goal>> ds = goals.getRecurringGoalsByDayComplete(2024, 3, 30);
        Assert.assertEquals(1, ds.getValue().size());
    }

    @Test
    public void testIsGoalsEmpty() {
        Assert.assertEquals(false, goals.isGoalsEmpty());
    }

    @Test
    public void testRemoveGoalComplete() {
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (!goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
                i--;
            }
        }
        goals.removeGoalComplete(0);
        Assert.assertEquals(1, goals.findAll().getValue().size());
    }

    @Test
    public void testRemoveGoalIncomplete() {
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
                i--;
            }
        }
        goals.removeGoalIncomplete(0);
        Assert.assertEquals(1, goalsComplete.findAll().getValue().size());
    }

    @Test
    public void testAppendComplete() {
        goals.appendComplete(new Goal(null, "workout", true, 0, true, 0, 0, 4, 30, 3, 2024, 0, 0));
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (!goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
            }
        }
        Assert.assertEquals(3, goals.findAll().getValue().size());
    }

    @Test
    public void testAppendIncomplete() {
        goals.appendIncomplete(new Goal(null, "workout", false, 0, true, 0, 0, 4, 30, 3, 2024, 0, 0));
        for (int i = 0; i < goals.findAll().getValue().size(); i++){
            if (goals.findAll().getValue().get(i).isComplete()){
                goals.findAll().getValue().remove(i);
            }
        }
        Assert.assertEquals(4, goals.findAll().getValue().size());
    }

    @Test
    public void testPrependIncomplete() {
        goalsIncomplete.prepend(new Goal(null, "workout", false, 5, true, 0, 0, 4, 30, 3, 2024, 0, 0));
        for (int i = 0; i < goalsIncomplete.findAll().getValue().size(); i++){
            if (goalsIncomplete.findAll().getValue().get(i).isComplete()){
                goalsIncomplete.findAll().getValue().remove(i);
                i--;
            }
        }
        Assert.assertEquals(3, goalsIncomplete.findAll().getValue().size());
    }

    @Test
    public void testDeleteCompleted() {
        goals.deleteCompleted();
        Assert.assertEquals(4, goals.findAll().getValue().size());
    }

    @Test
    public void testAppendTime() {
        timeKeeper.setDateTime(LocalDateTime.now());
        Assert.assertNotNull(timeKeeper);
    }

    @Test
    public void testDeleteTime() {
        timeKeeper.removeDateTime();
        Assert.assertNotEquals(mockKeeper, timeKeeper);
    }

    @Test
    public void testGetFields() {
        Assert.assertNotNull(timeKeeper.getFields());
    }
}