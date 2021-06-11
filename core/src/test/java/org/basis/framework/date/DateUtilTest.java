package org.basis.framework.date;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    public void getYesterday() {
        System.out.println(DateUtil.formatDate(DateUtil.getYesterday()));
        System.out.println("toDayStart："+DateUtil.getDateStartTime(DateUtil.getYesterday()));
    }
}