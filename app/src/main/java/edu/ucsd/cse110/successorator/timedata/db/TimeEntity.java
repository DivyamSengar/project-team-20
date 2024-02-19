package edu.ucsd.cse110.successorator.timedata.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

/**
 * TimeEntity class to describe its behavior and attributes
 */
@Entity(tableName = "Time")
public class TimeEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "hour")
    public int hour;

    @ColumnInfo(name = "day")
    public int day;

    @ColumnInfo(name = "minutes")
    public int minutes;

    @ColumnInfo(name = "month")
    public int month;

    @ColumnInfo(name = "year")
    public int year;

    /**
     * TimeEntity constructor to initialize its fields
     * @param id = id of the TimeEntity object
     * @param minutes = number of minutes
     * @param hour = number of horus
     * @param day = day of the month
     * @param month = month
     * @param year = year
     */
    public TimeEntity(int id, int minutes, int hour, int day, int month, int year) {
        this.id = id;
        this.day = day;
        this. hour = hour;
        this.minutes = minutes;
        this.year = year;
        this.month = month;
    }

    /**
     * This methods creates a new TimeEntity object from a localDateTime object
     * @param localDateTime = localDateTime object that will be used to create a TimeEntity object
     * @return a TimeEntity object with the values of the localDateTime object parameter
     */
    public static TimeEntity fromLocalDateTime(@NonNull LocalDateTime localDateTime){
        return new TimeEntity(0, localDateTime.getMinute(),
                localDateTime.getHour(), localDateTime.getDayOfMonth(),
                localDateTime.getMonthValue(),localDateTime.getYear());
    }

    /**
     * Converts the TimeEntity object to a localDateTime object
     * @return localDateTime object of the TimeEntity object
     */
    public @NonNull LocalDateTime toLocalDateTime (){
        return LocalDateTime.of(year, month, day, hour, minutes);
    }

    /**
     * Returns int array of all the fields of the TimeEntity object
     */
    public int[] Fields(){
            int[] arr = {year, month, day, hour, minutes};
            return arr;
    }
}
