package org.deustomed;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Locale;

public class WeekDayFinder {

    /**
     * Find the next day of the week from a given date
     * @param dayOfWeekStr the day of the week to find (e.g. "Monday")
     * @param date
     * @return the next day of the week
     */
    public LocalDate findNextDay(String dayOfWeekStr, LocalDate date) {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase(Locale.ROOT));
        int daysUntilNext = (dayOfWeek.getValue() - date.getDayOfWeek().getValue() + 7) % 7;
        return daysUntilNext == 0 ? date : date.plusDays(daysUntilNext);
    }


}

