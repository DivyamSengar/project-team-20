//package edu.ucsd.cse110.successorator.lib.data;
//import edu.ucsd.cse110.successorator.lib.domain.Goal;
//import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
//
//import org.junit.Test;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class TestDataSource {
//
////    @Test
////    public void testConstructor() {
////        DataSource dataSource = new DataSource();
////
////        assertNotNull(dataSource.goalsComplete);
////        assertNotNull(dataSource.goalsInComplete);
////        assertNotNull(dataSource.goals);
////        assertNotNull(dataSource.goalEntrySubjects);
////        assertNotNull(dataSource.allGoalEntrySubjects);
////        assertTrue(dataSource.goalsComplete.isEmpty());
////        assertTrue(dataSource.goalsInComplete.isEmpty());
////        assertTrue(dataSource.goals.isEmpty());
////        assertTrue(dataSource.goalEntrySubjects.isEmpty());
////    }
//
//    // tests getGoals as well
//    @Test
//    public void testUpdateGoals() {
//        DataSource dataSource = new DataSource();
//
//        // Add incomplete goals
//        dataSource.putGoalEntry(new Goal(1, "Incomplete Goal 1", false, 0));
//        dataSource.putGoalEntry(new Goal(2, "Incomplete Goal 2", false, 1));
//
//        // Add complete goals
//        dataSource.putGoalEntry(new Goal(3, "Complete Goal 1", true, 2));
//        dataSource.putGoalEntry(new Goal(4, "Complete Goal 2", true, 3));
//
//
//        List<Goal> goals = dataSource.getGoals();
//        assertEquals(4, goals.size());
//        assertEquals("Incomplete Goal 1", goals.get(0).getText());
//        assertEquals("Incomplete Goal 2", goals.get(1).getText());
//        assertEquals("Complete Goal 1", goals.get(2).getText());
//        assertEquals("Complete Goal 2", goals.get(3).getText());
//    }
//
//    @Test
//    public void testGetGoals() {
//        // Covered in testUpdateGoals()
//    }
//
//    @Test
//    public void testGetGoalEntry() {
//        DataSource dataSource = new DataSource();
//
//        // Add incomplete goal
//        Goal incompleteGoal = new Goal(1, "Incomplete Goal", false, 0);
//        dataSource.putGoalEntry(incompleteGoal);
//
//        // Add complete goal
//        Goal completeGoal = new Goal(2, "Complete Goal", true, 1);
//        dataSource.putGoalEntry(completeGoal);
//
//        assertEquals(incompleteGoal, dataSource.getGoalEntry(1));
//        assertEquals(completeGoal, dataSource.getGoalEntry(2));
//        assertNull(dataSource.getGoalEntry(3)); // Non-existent ID
//    }
//
//    @Test
//    public void testGetGoalEntrySubject() {
//        DataSource dataSource = new DataSource();
//
//        // Add goal
//        Goal goal = new Goal(1, "Test Goal", false, 0);
//        dataSource.putGoalEntry(goal);
//
//        SimpleSubject<Goal> subject = dataSource.getGoalEntrySubject(1);
//        assertEquals(goal, subject.getValue());
//    }
//
//    @Test
//    public void testGetAllGoalEntrySubject() {
//        // Covered in testPutGoalEntry()
//    }
//
//    @Test
//    public void testPutGoalEntry() {
//        DataSource dataSource = new DataSource();
//
//        Goal goal1 = new Goal(1, "Goal 1", false, 0);
//        Goal goal3 = new Goal(3, "Goal 3", false, 1);
//        Goal goal2 = new Goal(2, "Goal 2", true, 2);
//        Goal goal4 = new Goal(4, "Goal 4", true, 3);
//        Goal goal5 = new Goal(5, "Goal 5", true, 4);
//
//
//
//        dataSource.putGoalEntry(goal1);
//        dataSource.putGoalEntry(goal2);
//        dataSource.putGoalEntry(goal3);
//        dataSource.putGoalEntry(goal4);
//        dataSource.putGoalEntry(goal5);
//
//        // Check goals storage
//        boolean inc = false;
//        boolean com = true;
//        int numInc = 0; int numCom = 0;
//        var goals = dataSource.getGoals();
//        for (Goal goal : goals){
//            if (goal.isComplete()) {
//                numCom++;
//                com = com && goal.isComplete();
//            }
//            else {
//                numInc++;
//                inc = inc || goal.isComplete();
//            }
//        }
//        assertEquals(true, com);
//        assertEquals(false, inc);
//        assertEquals(3, numCom);
//        assertEquals(2, numInc);
//
//        // Check subjects
//        assertEquals(goal1, dataSource.getGoalEntrySubject(1).getValue());
//        assertEquals(goal2, dataSource.getGoalEntrySubject(2).getValue());
//        List<Goal> allGoals = dataSource.getAllGoalEntrySubject().getValue();
//        assertEquals(5, allGoals.size());
//    }
//}
