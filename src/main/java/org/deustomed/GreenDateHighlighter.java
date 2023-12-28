package org.deustomed;


import com.toedter.calendar.IDateEvaluator;
import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GreenDateHighlighter implements IDateEvaluator {
    private final Set<Calendar> specialDates = new HashSet<>();

    public void addDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        clearTime(cal);
        specialDates.add(cal);
    }

    public void clearDates() {
        specialDates.clear();
    }

    private void clearTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public boolean isSpecial(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        clearTime(cal);
        return specialDates.contains(cal);
    }

    @Override
    public Color getSpecialForegroundColor() {
        return Color.black;
    }

    @Override
    public Color getSpecialBackroundColor() {
        return Color.green;
    }

    @Override
    public String getSpecialTooltip() {
        return "Highlighted date.";
    }

    @Override
    public boolean isInvalid(Date date) {
        return false;
    }

    @Override
    public Color getInvalidForegroundColor() {
        return null;
    }

    @Override
    public Color getInvalidBackroundColor() {
        return null;
    }

    @Override
    public String getInvalidTooltip() {
        return null;
    }
}


