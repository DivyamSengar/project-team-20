package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Test;
public class TestGoal {
    @Test
    public void testConstructorWithId() {
        int id = 1;
        String text = "Test Goal";
        boolean isComplete = false;

        Goal goal = new Goal(id, text, isComplete, 0);

        assertEquals((int) id, (int) goal.id());
        assertEquals(text, goal.getText());
        assertFalse(goal.isComplete());
    }

    @Test
    public void testConstructorWithNullableId() {
        String text = "Test Goal";
        boolean isComplete = false;

        Goal goal = new Goal(null, text, isComplete, 0);

        assertNull(goal.id());
        assertEquals(text, goal.getText());
        assertFalse(goal.isComplete());
    }

    @Test
    public void testGetText() {
        String text = "Test Goal";
        Goal goal = new Goal(null, text, false, 0);

        assertEquals(text, goal.getText());
    }

    @Test
    public void testUpdateText() {
        String initialText = "Initial Goal";
        String updatedText = "Updated Goal";
        Goal goal = new Goal(null, initialText, false, 0);

        goal.updateText(updatedText);

        assertEquals(updatedText, goal.getText());
    }

    @Test
    public void testGetIsComplete() {
        Goal incompleteGoal = new Goal(null, "Incomplete Goal", false, 0);
        Goal completeGoal = new Goal(null, "Complete Goal", true, 1);

        assertFalse(incompleteGoal.isComplete());
        assertTrue(completeGoal.isComplete());
    }

    @Test
    public void testUpdateStatus() {
        Goal goal = new Goal(null, "Test Goal", false, 0);

        goal.updateStatus();

        assertTrue(goal.isComplete());

        goal.updateStatus(); // Toggle again

        assertFalse(goal.isComplete());
    }

//    @Test
//    public void testMakeComplete() {
//        String originalText = "This is a goal";
//        Goal goal = new Goal(null, originalText, false, 0);
//
//        goal.makeComplete();
//
//        String expectedText = "This is a goal"; // Strikethrough applied
//        assertEquals(expectedText, goal.getText());
//    }

    @Test
    public void testEquals() {
        Goal goal1 = new Goal(1, "Goal 1", true, 0);
        Goal goal2 = new Goal(1, "Goal 1", true, 1);
        Goal goal3 = new Goal(2, "Goal 1", true, 2);
        Goal goal4 = new Goal(1, "Goal 2", true, 3);
        Goal goal5 = new Goal(1, "Goal 1", false, 4);

        assertTrue(goal1.equals(goal2));
        assertFalse(goal1.equals(goal3));
        assertFalse(goal1.equals(goal4));
        assertFalse(goal1.equals(goal5));
    }

    @Test
    public void testHashCode() {
        Goal goal1 = new Goal(1, "Goal 1", true, 0);
        Goal goal2 = new Goal(1, "Goal 1", true, 1);

        assertEquals(goal1.hashCode(), goal2.hashCode());
    }
}
