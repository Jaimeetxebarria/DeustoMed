package org.deustomed;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Locale;

public class WeekDayFinder {

    /**
     * Find the next day of the week from the current date
     * @param dayOfWeekStr the day of the week to find (e.g. "Monday")
     * @return the next day of the week
     */
    public LocalDate findNextDay(String dayOfWeekStr) {
        LocalDate currentDate = LocalDate.now();
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase(Locale.ROOT));
        int daysUntilNext = (dayOfWeek.getValue() - currentDate.getDayOfWeek().getValue() + 7) % 7;
        return daysUntilNext == 0 ? currentDate : currentDate.plusDays(daysUntilNext);
    }


}

