package org.basis.framework.utils;


import org.basis.framework.utils.DateUtil;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.basis.framework.utils.DateUtil.getLastMonthStartTime;

public class DateUtilTest {

    @Test
    public void getYesterday() {
        System.out.println(DateUtil.formatDate(DateUtil.getYesterday()));
        System.out.println("toDayStart："+DateUtil.getDateStartTime(DateUtil.getYesterday()));
    }

    @Test
    public void offset(){
        System.out.println(DateUtil.age(DateUtil.parse("1997-07-17")));
    }

    @Test
    public void interval(){
        System.out.println(DateUtil.dayInterval(new Date(),DateUtil.parse("2022-09-20")));
    }

    @Test
    public void isChinese(){
        System.out.println(StringUtil.isChinese("zzzz13/.A11从前"));
    }
}