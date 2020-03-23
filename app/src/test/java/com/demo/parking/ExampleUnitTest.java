package com.demo.parking;


import android.icu.util.LocaleData;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.println(LocalDateTime.now().getYear()
        );
        System.out.println(LocalDateTime.now().getMonthValue()
        );
        System.out.println(LocalDateTime.now().getHour()
        );
        System.out.println(LocalDateTime.now().getMinute()
        );
       System.out.println(LocalDateTime.now().getSecond()
        );

    }
}