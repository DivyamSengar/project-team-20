package edu.ucsd.cse110.successorator.lib.domain;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import org.junit.Test;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import java.util.List;

import static org.junit.Assert.*;

public class TestGoalList {

    @Test
    public void testConstructor() {
        DataSource dataSource = new DataSource();
        GoalList goalList = new GoalList(dataSource);

        assertSame(dataSource, goalList.getDataSource());
    }

    @Test
    public void testCount() {
        DataSource dataSource = new DataSource();
        GoalList goalList = new GoalList(dataSource);

        // Initial count should be 0
        assertEquals((int)0, (int)goalList.count());

        // Add goals and check count
        goalList.save(new GoalEntry(1, "Goal 1", false));
        assertEquals((int)1, (int)goalList.count());
        goalList.save(new GoalEntry(2, "Goal 2", true));
        assertEquals((int)2, (int)goalList.count());
    }

    @Test
    public void testFind() {
        DataSource dataSource = new DataSource();
        GoalList goalList = new GoalList(dataSource);

        // Add goals
        GoalEntry goal1 = new GoalEntry(1, "Goal 1", false);
        GoalEntry goal2 = new GoalEntry(2, "Goal 2", true);
        goalList.save(goal1);
        goalList.save(goal2);

        // Find by ID
        SimpleSubject<GoalEntry> subject1 = goalList.find(1);
        assertEquals(goal1, subject1.getValue());
        SimpleSubject<GoalEntry> subject2 = goalList.find(2);
        assertEquals(goal2, subject2.getValue());

        // Find non-existent ID
        assertNotNull(goalList.find(3));
    }

    @Test
    public void testFindAll() {
        DataSource dataSource = new DataSource();
        GoalList goalList = new GoalList(dataSource);

        // Add goals
        GoalEntry goal1 = new GoalEntry(1, "Goal 1", false);
        GoalEntry goal2 = new GoalEntry(2, "Goal 2", true);
        goalList.save(goal1);
        goalList.save(goal2);

        // Find all
        List<GoalEntry> allGoals = goalList.findAll().getValue();
        assertEquals(List.of(goal1, goal2), allGoals);
    }

    @Test
    public void testSave() {
        DataSource dataSource = new DataSource();
        GoalList goalList = new GoalList(dataSource);

        // Add goals
        GoalEntry goal1 = new GoalEntry(1, "Goal 1", false);
        GoalEntry goal2 = new GoalEntry(2, "Goal 2", true);
        goalList.save(goal1);
        goalList.save(goal2);

        // Check dataSource and subjects
        assertEquals(List.of(goal1, goal2), dataSource.getGoals());
        assertEquals(goal1, goalList.find(goal1.id()).getValue());
        assertEquals(goal2, goalList.find(goal2.id()).getValue());
        assertEquals(List.of(goal1, goal2), goalList.findAll().getValue());
    }
}
