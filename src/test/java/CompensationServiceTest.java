import com.model.EmployeeDayRecord;
import com.service.EveningCompensationService;
import com.service.OverTimeCompensationService;
import com.service.RegularCompensationService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CompensationServiceTest {

    EmployeeDayRecord employeeDailyLog;

    @InjectMocks
    private OverTimeCompensationService overTimeCompensationService;

    @InjectMocks
    private RegularCompensationService regularCompensationService;

    @InjectMocks
    private EveningCompensationService eveningCompensationService;

    @Before
    public void setup() {
        employeeDailyLog = mock(EmployeeDayRecord.class);
        DateTime dateTime = DateTime.now();
        when(employeeDailyLog.getShiftStartDateTime()).thenReturn(dateTime);
        dateTime = dateTime.plusHours(12);
        when(employeeDailyLog.getShiftEndDateTime()).thenReturn(dateTime);
    }

    @Test
    public void testRegularTimeService(){
        BigDecimal actualWage = regularCompensationService.getCompensation(employeeDailyLog);
        BigDecimal expectedWage = new BigDecimal(51.00).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        assertEquals(actualWage, expectedWage);
    }

    @Test
    public void testOverTimeService() {
        BigDecimal actualWage = overTimeCompensationService.getCompensation(employeeDailyLog);
        BigDecimal expectedWage = new BigDecimal(5.31).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        assertEquals(expectedWage,actualWage);
    }

    @Test
    public void testEveningService(){
        DateTime dateTime = DateTime.now().withHourOfDay(6).withMinuteOfHour(00);
        when(employeeDailyLog.getShiftStartDateTime()).thenReturn(dateTime);
        dateTime = dateTime.withHourOfDay(23).withMinuteOfHour(00);
        when(employeeDailyLog.getShiftEndDateTime()).thenReturn(dateTime);
        BigDecimal actualWage = eveningCompensationService.getCompensation(employeeDailyLog);
        BigDecimal expectedWage = new BigDecimal(3.75).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        assertEquals(expectedWage,actualWage);
    }


}
