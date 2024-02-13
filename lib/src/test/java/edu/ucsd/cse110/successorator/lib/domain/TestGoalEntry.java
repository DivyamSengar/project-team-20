package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Test;
public class TestGoalEntry {
    @Test
    public void testConstructorWithId() {
        int id = 1;
        String text = "Test Goal";
        boolean isComplete = false;

        GoalEntry goalEntry = new GoalEntry(id, text, isComplete);

        assertEquals((int) id, (int) goalEntry.id());
        assertEquals(text, goalEntry.getText());
        assertFalse(goalEntry.getIsComplete());
    }

    @Test
    public void testConstructorWithNullableId() {
        String text = "Test Goal";
        boolean isComplete = false;

        GoalEntry goalEntry = new GoalEntry(null, text, isComplete);

        assertNull(goalEntry.id());
        assertEquals(text, goalEntry.getText());
        assertFalse(goalEntry.getIsComplete());
    }

    @Test
    public void testGetText() {
        String text = "Test Goal";
        GoalEntry goalEntry = new GoalEntry(null, text, false);

        assertEquals(text, goalEntry.getText());
    }

    @Test
    public void testUpdateText() {
        String initialText = "Initial Goal";
        String updatedText = "Updated Goal";
        GoalEntry goalEntry = new GoalEntry(null, initialText, false);

        goalEntry.updateText(updatedText);

        assertEquals(updatedText, goalEntry.getText());
    }

    @Test
    public void testGetIsComplete() {
        GoalEntry incompleteGoal = new GoalEntry(null, "Incomplete Goal", false);
        GoalEntry completeGoal = new GoalEntry(null, "Complete Goal", true);

        assertFalse(incompleteGoal.getIsComplete());
        assertTrue(completeGoal.getIsComplete());
    }

    @Test
    public void testUpdateStatus() {
        GoalEntry goalEntry = new GoalEntry(null, "Test Goal", false);

        goalEntry.updateStatus();

        assertTrue(goalEntry.getIsComplete());

        goalEntry.updateStatus(); // Toggle again

        assertFalse(goalEntry.getIsComplete());
    }

    @Test
    public void testMakeComplete() {
        String originalText = "This is a goal";
        GoalEntry goalEntry = new GoalEntry(null, originalText, false);

        goalEntry.makeComplete();

        String expectedText = "This is a goal"; // Strikethrough applied
        assertEquals(expectedText, goalEntry.getText());
    }

    @Test
    public void testEquals() {
        GoalEntry goalEntry1 = new GoalEntry(1, "Goal 1", true);
        GoalEntry goalEntry2 = new GoalEntry(1, "Goal 1", true);
        GoalEntry goalEntry3 = new GoalEntry(2, "Goal 1", true);
        GoalEntry goalEntry4 = new GoalEntry(1, "Goal 2", true);
        GoalEntry goalEntry5 = new GoalEntry(1, "Goal 1", false);

        assertTrue(goalEntry1.equals(goalEntry2));
        assertFalse(goalEntry1.equals(goalEntry3));
        assertFalse(goalEntry1.equals(goalEntry4));
        assertFalse(goalEntry1.equals(goalEntry5));
    }

    @Test
    public void testHashCode() {
        GoalEntry goalEntry1 = new GoalEntry(1, "Goal 1", true);
        GoalEntry goalEntry2 = new GoalEntry(1, "Goal 1", true);

        assertEquals(goalEntry1.hashCode(), goalEntry2.hashCode());
    }
}
