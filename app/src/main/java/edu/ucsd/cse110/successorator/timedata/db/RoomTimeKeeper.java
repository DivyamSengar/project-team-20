package edu.ucsd.cse110.successorator.timedata.db;

import androidx.lifecycle.Transformations;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.data.db.GoalDao;
import edu.ucsd.cse110.successorator.data.db.GoalEntity;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomTimeKeeper implements TimeKeeper {
    private final TimeDao timeDao;
    public RoomTimeKeeper(TimeDao timeDao){
        this.timeDao = timeDao;
    }
    public Subject<LocalDateTime> getDateTime(){
        var entityLiveData = timeDao.getTimeAsLiveData();
        var TimeLiveData = Transformations.map(entityLiveData, TimeEntity::toLocalDateTime);
        return new LiveDataSubjectAdapter<>(TimeLiveData);
    }
    public void setDateTime(LocalDateTime dateTime){
        timeDao.insertTime(TimeEntity.fromLocalDateTime(dateTime));
    }
    public void removeDateTime(){
        timeDao.deleteTime();
    }
}
