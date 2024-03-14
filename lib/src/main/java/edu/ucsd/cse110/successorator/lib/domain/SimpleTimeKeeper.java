package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

public class SimpleTimeKeeper implements TimeKeeper {

    public LocalDateTime localdatetime;

    public SimpleTimeKeeper(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            // If localDateTime is null, initialize it to the current date and time
            this.localdatetime = LocalDateTime.now();
        } else {
            this.localdatetime = localDateTime;
        }
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {
        localdatetime = dateTime;
    }

    @Override
    public void removeDateTime() {
        localdatetime = null;
    }

    @Override
    public int[] getFields() {
        return new int[] {
                localdatetime.getYear(),
                localdatetime.getMonthValue(),
                localdatetime.getDayOfMonth(),
                localdatetime.getHour(),
                localdatetime.getMinute(),
                localdatetime.getSecond()
        };
    }

}

