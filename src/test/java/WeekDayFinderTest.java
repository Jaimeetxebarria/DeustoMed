import org.deustomed.WeekDayFinder;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;

 public class WeekDayFinderTest {

    @Test
    void testFindNextDay_SameDay() {
        WeekDayFinder finder = new WeekDayFinder();
        String todayDayOfWeek = LocalDate.now().getDayOfWeek().toString();
        LocalDate expected = LocalDate.now();
        LocalDate actual = finder.findNextDay(todayDayOfWeek);
        assertEquals(expected, actual, "Should return today's date when the day of week is today");
    }

    @Test
    void testFindNextDay_NextWeek() {
        WeekDayFinder finder = new WeekDayFinder();
        LocalDate today = LocalDate.now();
        String yesterdayDayOfWeek = today.minusDays(1).getDayOfWeek().toString();
        LocalDate expected = today.plusWeeks(1).minusDays(1);
        LocalDate actual = finder.findNextDay(yesterdayDayOfWeek);
        assertEquals(expected, actual, "Should return the same day next week if yesterday's day of week is given");
    }

    @Test
    void testFindNextDay_InvalidDay() {
        WeekDayFinder finder = new WeekDayFinder();
        assertThrows(IllegalArgumentException.class, () -> finder.findNextDay("InvalidDay"), "Should throw IllegalArgumentException for invalid day of week");
    }

     @Test
     void testFindNextDay_EndOfWeek() {
         WeekDayFinder finder = new WeekDayFinder();
         LocalDate lastDayOfWeek = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
         String nextDayOfWeek = "Monday";
         LocalDate expected = lastDayOfWeek.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
         LocalDate actual = finder.findNextDay(nextDayOfWeek);
         assertEquals(expected, actual, "Should return the correct date for the next Monday after a Sunday");
     }

     @Test
     void testFindNextDay_NearFutureDay() {
         WeekDayFinder finder = new WeekDayFinder();
         LocalDate today = LocalDate.now();
         String nextDayOfWeek = today.plusDays(2).getDayOfWeek().toString();
         LocalDate expected = today.plusDays(2);
         LocalDate actual = finder.findNextDay(nextDayOfWeek);
         assertEquals(expected, actual, "Should return the correct date for a day of the week in the near future");
     }

}
