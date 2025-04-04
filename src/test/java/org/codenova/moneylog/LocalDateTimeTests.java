package org.codenova.moneylog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class LocalDateTimeTests {

    @Test
    public void test01() {
        LocalDate today = LocalDate.now();

        DayOfWeek dow = today.getDayOfWeek();  //요일
        System.out.println(dow);
        int value = today.getDayOfWeek().getValue();  //요일마다 숫자(금:5
        System.out.println(value);
        System.out.println(today.plusDays(1).getDayOfWeek().getValue());
        System.out.println(today.plusDays(2).getDayOfWeek().getValue());
        System.out.println(today.minusDays(5).getDayOfWeek().getValue());  //7

        /*
            월 ~ 일 (1,2,3,4,5,6,7)
         */

        LocalDate d = LocalDate.of(2025,3,18);
        // 우리에게 특정 LocalDate 가 있다고 가정 그 날짜 포함된 주의 시작일과 끝일을 구하려면..?

        System.out.println(d.getDayOfWeek());

        LocalDate d2 = LocalDate.of(2025, 4,04);
        LocalDate firstDayOfWeek = d2.minusDays(d.getDayOfWeek().getValue() -1);
        LocalDate lastDayOfWeek = d2.plusDays( 7-d.getDayOfWeek().getValue());
        System.out.println(firstDayOfWeek);
        System.out.println(lastDayOfWeek);
    }
    @Test
    public void test02() {
        LocalDate today = LocalDate.now();

        System.out.println(today.minusDays(today.getDayOfMonth()));
        LocalDate day = today.minusDays(today.getDayOfMonth());
        System.out.println(day.plusMonths(1));
        //System.out.println(today.plusMonths(0));

//        today.plusDays(1);
//        today.plusWeeks(1);
//        today.plusMonths(1);
//        today.plusYears(1);

    }
}
