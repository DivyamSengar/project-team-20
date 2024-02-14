package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Test;
public class TestGoal {
    @Test
    public void testConstructorWithId() {
        int id = 1;
        String text = "Test Goal";
        boolean isComplete = false;

        Goal goal = new Goal(id, text, isComplete);

        assertEquals((int) id, (int) goal.id());
        assertEquals(text, goal.getText());
        assertFalse(goal.isComplete());
    }

    @Test
    public void testConstructorWithNullableId() {
        String text = "Test Goal";
        boolean isComplete = false;

        Goal goal = new Goal(null, text, isComplete);

        assertNull(goal.id());
        assertEquals(text, goal.getText());
        assertFalse(goal.isComplete());
    }

    @Test
    public void testGetText() {
        String text = "Test Goal";
        Goal goal = new Goal(null, text, false);

        assertEquals(text, goal.getText());
    }

    @Test
    public void testUpdateText() {
        String initialText = "Initial Goal";
        String updatedText = "Updated Goal";
        Goal goal = new Goal(null, initialText, false);

        goal.updateText(updatedText);

        assertEquals(updatedText, goal.getText());
    }

    @Test
    public void testGetIsComplete() {
        Goal incompleteGoal = new Goal(null, "Incomplete Goal", false);
        Goal completeGoal = new Goal(null, "Complete Goal", true);

        assertFalse(incompleteGoal.isComplete());
        assertTrue(completeGoal.isComplete());
    }

    @Test
    public void testUpdateStatus() {
        Goal goal = new Goal(null, "Test Goal", false);

        goal.updateStatus();

        assertTrue(goal.isComplete());

        goal.updateStatus(); // Toggle again

        assertFalse(goal.isComplete());
    }

    @Test
    public void testMakeComplete() {
        String originalText = "This is a goal";
        Goal goal = new Goal(null, originalText, false);

        goal.makeComplete();

        String expectedText = "This is a goal"; // Strikethrough applied
        assertEquals(expectedText, goal.getText());
    }

    @Test
    public void testEquals() {
        Goal goal1 = new Goal(1, "Goal 1", true);
        Goal goal2 = new Goal(1, "Goal 1", true);
        Goal goal3 = new Goal(2, "Goal 1", true);
        Goal goal4 = new Goal(1, "Goal 2", true);
        Goal goal5 = new Goal(1, "Goal 1", false);

        assertTrue(goal1.equals(goal2));
        assertFalse(goal1.equals(goal3));
        assertFalse(goal1.equals(goal4));
        assertFalse(goal1.equals(goal5));
    }

    @Test
    public void testHashCode() {
        Goal goal1 = new Goal(1, "Goal 1", true);
        Goal goal2 = new Goal(1, "Goal 1", true);

        assertEquals(goal1.hashCode(), goal2.hashCode());
    }
}
