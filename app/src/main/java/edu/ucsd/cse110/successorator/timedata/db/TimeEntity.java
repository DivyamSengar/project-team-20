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

        @ColumnInfo(name = "localDateTime")
        public String localDateTime;

        public TimeEntity(@NonNull String localDateTime, int id) {
            this.localDateTime = localDateTime;
            this.id = id;
        }

        public static TimeEntity fromLocalDateTime(@NonNull LocalDateTime localDateTime){
            return new TimeEntity(LocalDateTimeToString(localDateTime), 0);
        }

        public @NonNull LocalDateTime toLocalDateTime (){
            return StringToLocalDateTime(localDateTime);
    }
    public static String LocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        return formattedDateTime;
//
    }

    public static LocalDateTime StringToLocalDateTime(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDateTime = LocalDateTime.parse(str, formatter);
        return parsedDateTime;

    }

}
