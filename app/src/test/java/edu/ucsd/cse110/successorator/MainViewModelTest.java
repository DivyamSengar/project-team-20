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

public class MainViewModelTest extends TestCase {

    DataSource dataSource = DataSource.fromDefault();
    GoalRepository goals = new SimpleGoalRepository(dataSource);


    @Before
    public void setup(){

    }

    public void testRollover() {
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