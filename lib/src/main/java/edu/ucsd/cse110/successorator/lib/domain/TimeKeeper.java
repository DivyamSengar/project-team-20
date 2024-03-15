package edu.ucsd.cse110.successorator.lib.domain;
import java.time.LocalDateTime;
import edu.ucsd.cse110.successorator.lib.util.Subject;
/**
 * Interface to describe the attributes and behavior of timekeeper
 */
public interface TimeKeeper {
    /**
     * Update the LocalDateTime object
     * @param dateTime updated value
     */
    void setDateTime(LocalDateTime dateTime);
    /**
     * delete the current LocalDateTime object
     */
    void removeDateTime();
    /**
     * Get fields of all the LocalDateTime object
     * @return integer array of the fields
     */
    int[] getFields();
    int count();
}