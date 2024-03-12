package edu.ucsd.cse110.successorator;

import android.provider.ContactsContract;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.List;


import edu.ucsd.cse110.successorator.data.db.GoalDao_Impl;
import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModelTest extends TestCase {

    DataSource dataSource = DataSource.fromDefault();
    GoalRepository goalsIncomplete = new SimpleGoalRepository(dataSource);
    GoalRepository goalsComplete = new SimpleGoalRepository(dataSource);
    TimeKeeper timeKeeper;
    MainViewModel mvm = new MainViewModel(goalsComplete, goalsIncomplete, timeKeeper);

    @Before
    public void setup(){
        Goal goal1 = new Goal(1, "do homework", false, 0, true, "monthly", 0, 4, 30, 3, 2024);
        Goal goal2 = new Goal(2, "clean room", false, 1, true, "monthly", 0, 4, 30, 3, 2024);

        goalsIncomplete.append(goal1);
        goalsIncomplete.append(goal2);

    }

    public void testRollover() {
        // insert some tasks
        // ADVCNACEED TGHE DAY or call ROLLOVER
        // what do yuou expect

    }

    public void testIncompleteRecurrentRollover() {
    }

    public void testGetGoals() {

    }

    public void testGetPendingGoals() {
    }

    public void testGetRecurringGoalsIncomplete() {
    }

    public void testGetRecurringGoalsComplete() {
    }

    public void testGetGoalsByDayIncomplete() {
    }

    public void testGetGoalsByDayComplete() {
    }

    public void testGetGoalsLessThanOrEqualToDay() {
    }

    public void testGetRecurringGoalsByDayComplete() {
    }

    public void testIsGoalsEmpty() {
    }

    public void testRemoveGoalComplete() {
    }

    public void testRemoveGoalIncomplete() {
    }

    public void testAppendComplete() {

    }

    public void testAppendIncomplete() {
    }

    public void testPrependIncomplete() {
    }

    public void testDeleteCompleted() {
    }

    public void testAppendTime() {
    }

    public void testDeleteTime() {
    }

    public void testGetFields() {
    }
}