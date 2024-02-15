package edu.ucsd.cse110.successorator.lib.domain;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import org.junit.Test;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import java.util.List;

import static org.junit.Assert.*;

public class TestGoalRepository {

    @Test
    public void testConstructor() {
        DataSource dataSource = new DataSource();
        GoalRepository goalRepository = new GoalRepository(dataSource);

        assertSame(dataSource, goalRepository.getDataSource());
    }

    @Test
    public void testCount() {
        DataSource dataSource = new DataSource();
        GoalRepository goalRepository = new GoalRepository(dataSource);

        // Initial count should be 0
        assertEquals((int)0, (int) goalRepository.count());

        // Add goals and check count
        goalRepository.save(new Goal(1, "Goal 1", false, 0));
        assertEquals((int)1, (int) goalRepository.count());
        goalRepository.save(new Goal(2, "Goal 2", true, 1));
        assertEquals((int)2, (int) goalRepository.count());
    }

    @Test
    public void testFind() {
        DataSource dataSource = new DataSource();
        GoalRepository goalRepository = new GoalRepository(dataSource);

        // Add goals
        Goal goal1 = new Goal(1, "Goal 1", false, 0);
        Goal goal2 = new Goal(2, "Goal 2", true, 1);
        goalRepository.save(goal1);
        goalRepository.save(goal2);

        // Find by ID
        SimpleSubject<Goal> subject1 = goalRepository.find(1);
        assertEquals(goal1, subject1.getValue());
        SimpleSubject<Goal> subject2 = goalRepository.find(2);
        assertEquals(goal2, subject2.getValue());

        // Find non-existent ID
        assertNotNull(goalRepository.find(3));
    }

    @Test
    public void testFindAll() {
        DataSource dataSource = new DataSource();
        GoalRepository goalRepository = new GoalRepository(dataSource);

        // Add goals
        Goal goal1 = new Goal(1, "Goal 1", false, 0);
        Goal goal2 = new Goal(2, "Goal 2", true, 1);
        goalRepository.save(goal1);
        goalRepository.save(goal2);

        // Find all
        List<Goal> allGoals = goalRepository.findAll().getValue();
        assertEquals(List.of(goal1, goal2), allGoals);
    }

    @Test
    public void testSave() {
        DataSource dataSource = new DataSource();
        GoalRepository goalRepository = new GoalRepository(dataSource);

        // Add goals
        Goal goal1 = new Goal(1, "Goal 1", false, 0);
        Goal goal2 = new Goal(2, "Goal 2", true, 1);
        goalRepository.save(goal1);
        goalRepository.save(goal2);

        // Check dataSource and subjects
        assertEquals(List.of(goal1, goal2), dataSource.getGoals());
        assertEquals(goal1, goalRepository.find(goal1.id()).getValue());
        assertEquals(goal2, goalRepository.find(goal2.id()).getValue());
        assertEquals(List.of(goal1, goal2), goalRepository.findAll().getValue());
    }
}
