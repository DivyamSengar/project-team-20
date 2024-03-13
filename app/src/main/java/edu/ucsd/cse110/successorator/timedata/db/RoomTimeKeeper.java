package edu.ucsd.cse110.successorator.timedata.db;

import java.time.LocalDateTime;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;

/**
 * RoomTimeKeeper class that implements the behavior and attributes of TimeKeeper
 */
public class RoomTimeKeeper implements TimeKeeper {
    private final TimeDao timeDao;

    /**
     * RoomTimeKeeper constructor to initialize the fields of TimeKeeper
     * @param timeDao timeDao object to initialize the field with
     */
    public RoomTimeKeeper(TimeDao timeDao){
        this.timeDao = timeDao;
    }

    /**
     * This method returns an int array of the fields of the time
     * @return int array of all the fields of the TimeEntity object
     */
    public int[] getFields(){
        var liveDataTemp = timeDao.getTime();
        assert liveDataTemp != null;
        return liveDataTemp.Fields();
    }

    /**
     * Updates the time database with a new localDateTime object
     * @param dateTime updated value to be inserted into the database as a TimeEntity object
     */
    public void setDateTime(LocalDateTime dateTime){
        timeDao.insertTime(TimeEntity.fromLocalDateTime(dateTime));
    }

    /**
     * Clears the time database and removes the localdatetime object from the table
     */
    public void removeDateTime(){
        timeDao.deleteTime();
    }

    public void appendTime(LocalDateTime dateTime){
        timeDao.insertTime(TimeEntity.fromLocalDateTime(dateTime));
    }
}
