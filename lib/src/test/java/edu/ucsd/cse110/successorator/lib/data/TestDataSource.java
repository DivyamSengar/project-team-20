package edu.ucsd.cse110.successorator.lib.data;
import edu.ucsd.cse110.successorator.lib.domain.GoalEntry;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestDataSource {

//    @Test
//    public void testConstructor() {
//        DataSource dataSource = new DataSource();
//
//        assertNotNull(dataSource.goalsComplete);
//        assertNotNull(dataSource.goalsInComplete);
//        assertNotNull(dataSource.goals);
//        assertNotNull(dataSource.goalEntrySubjects);
//        assertNotNull(dataSource.allGoalEntrySubjects);
//        assertTrue(dataSource.goalsComplete.isEmpty());
//        assertTrue(dataSource.goalsInComplete.isEmpty());
//        assertTrue(dataSource.goals.isEmpty());
//        assertTrue(dataSource.goalEntrySubjects.isEmpty());
//    }

    // tests getGoals as well
    @Test
    public void testUpdateGoals() {
        DataSource dataSource = new DataSource();

        // Add incomplete goals
        dataSource.putGoalEntry(new GoalEntry(1, "Incomplete Goal 1", false));
        dataSource.putGoalEntry(new GoalEntry(2, "Incomplete Goal 2", false));

        // Add complete goals
        dataSource.putGoalEntry(new GoalEntry(3, "Complete Goal 1", true));
        dataSource.putGoalEntry(new GoalEntry(4, "Complete Goal 2", true));


        List<GoalEntry> goals = dataSource.getGoals();
        assertEquals(4, goals.size());
        assertEquals("Incomplete Goal 1", goals.get(0).getText());
        assertEquals("Incomplete Goal 2", goals.get(1).getText());
        assertEquals("Complete Goal 1", goals.get(2).getText());
        assertEquals("Complete Goal 2", goals.get(3).getText());
    }

    @Test
    public void testGetGoals() {
        // Covered in testUpdateGoals()
    }

    @Test
    public void testGetGoalEntry() {
        DataSource dataSource = new DataSource();

        // Add incomplete goal
        GoalEntry incompleteGoal = new GoalEntry(1, "Incomplete Goal", false);
        dataSource.putGoalEntry(incompleteGoal);

        // Add complete goal
        GoalEntry completeGoal = new GoalEntry(2, "Complete Goal", true);
        dataSource.putGoalEntry(completeGoal);

        assertEquals(incompleteGoal, dataSource.getGoalEntry(1));
        assertEquals(completeGoal, dataSource.getGoalEntry(2));
        assertNull(dataSource.getGoalEntry(3)); // Non-existent ID
    }

    @Test
    public void testGetGoalEntrySubject() {
        DataSource dataSource = new DataSource();

        // Add goal
        GoalEntry goal = new GoalEntry(1, "Test Goal", false);
        dataSource.putGoalEntry(goal);

        SimpleSubject<GoalEntry> subject = dataSource.getGoalEntrySubject(1);
        assertEquals(goal, subject.getValue());
    }

    @Test
    public void testGetAllGoalEntrySubject() {
        // Covered in testPutGoalEntry()
    }

    @Test
    public void testPutGoalEntry() {
        DataSource dataSource = new DataSource();

        GoalEntry goal1 = new GoalEntry(1, "Goal 1", false);
        GoalEntry goal3 = new GoalEntry(3, "Goal 3", false);
        GoalEntry goal2 = new GoalEntry(2, "Goal 2", true);
        GoalEntry goal4 = new GoalEntry(4, "Goal 4", true);
        GoalEntry goal5 = new GoalEntry(5, "Goal 5", true);



        dataSource.putGoalEntry(goal1);
        dataSource.putGoalEntry(goal2);
        dataSource.putGoalEntry(goal3);
        dataSource.putGoalEntry(goal4);
        dataSource.putGoalEntry(goal5);

        // Check goals storage
        boolean inc = false;
        boolean com = true;
        int numInc = 0; int numCom = 0;
        var goals = dataSource.getGoals();
        for (GoalEntry goal : goals){
            if (goal.getIsComplete()) {
                numCom++;
                com = com && goal.getIsComplete();
            }
            else {
                numInc++;
                inc = inc || goal.getIsComplete();
            }
        }
        assertEquals(true, com);
        assertEquals(false, inc);
        assertEquals(3, numCom);
        assertEquals(2, numInc);

        // Check subjects
        assertEquals(goal1, dataSource.getGoalEntrySubject(1).getValue());
        assertEquals(goal2, dataSource.getGoalEntrySubject(2).getValue());
        List<GoalEntry> allGoals = dataSource.getAllGoalEntrySubject().getValue();
        assertEquals(5, allGoals.size());
    }
}
