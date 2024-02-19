package edu.ucsd.cse110.successorator.timedata.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.ucsd.cse110.successorator.data.db.GoalEntity;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

@Entity(tableName = "Time")
public class TimeEntity {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        public int id;

//        @ColumnInfo(name = "localDateTime")
//        public String localDateTime;

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

        public TimeEntity(int id, int minutes, int hour, int day, int month, int year) {
//            this.localDateTime = localDateTime;
            this.id = id;
            this.day = day;
            this. hour = hour;
            this.minutes = minutes;
            this.year = year;
            this.month = month;
        }

        public static TimeEntity fromLocalDateTime(@NonNull LocalDateTime localDateTime){
            return new TimeEntity(0, localDateTime.getMinute(),
                    localDateTime.getHour(), localDateTime.getDayOfMonth(),
                    localDateTime.getMonthValue(),localDateTime.getYear());
        }

        public @NonNull LocalDateTime toLocalDateTime (){
            return LocalDateTime.of(year, month, day, hour, minutes);
    }

    public int[] Fields(){
            int[] arr = {year, month, day, hour, minutes};
            return arr;
    }
//    public static String LocalDateTimeToString(LocalDateTime localDateTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDateTime = localDateTime.format(formatter);
//        return formattedDateTime;
////
//    }
//
//    public static LocalDateTime StringToLocalDateTime(String str){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime parsedDateTime = LocalDateTime.parse(str, formatter);
//        return parsedDateTime;
//
//    }

}
