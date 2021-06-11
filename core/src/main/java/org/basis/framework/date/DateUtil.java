package org.basis.framework.date;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.basis.framework.utils.regexp.RegexpUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;

/**
 * @Description 日期工具类
 * @Author ChenWenJie
 * @Data 2021/5/26 5:34 下午
 **/
public class DateUtil extends DateUtils {
    private static final Log LOG = LogFactory.getLog(DateUtil.class);

    /**
     * 日期驱动接口 针对DateUtil.now()方法获取时间
     */
    private static DateDriver dateDriver = new SimpleDateDriver();

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm:ss";
    private static final List<String> formats = new ArrayList<>(4);
    static {
        formats.add("yyyy-MM");
        formats.add("yyyy-MM-dd");
        formats.add("yyyy-MM-dd HH:mm");
        formats.add("yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 缓存的 SimpleDateFormat
     */
    private static final ConcurrentMap<String, DateFormatCache> DATE_FORMAT_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取昨天的日期时间
     * */
    public static Date getYesterday(){ return offset(new Date(),Calendar.DATE,-1); }

    /**
     * 偏移天数
     * @param date 日期
     * @param offset 偏移天数，正数向未来偏移，负数向历史偏移
     * @return
     */
    public static Date offsetDay(Date date, int offset) {
        return offset(date, Calendar.DAY_OF_YEAR, offset);
    }

    public static Date offset(Date date, int day, int offset){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(day,offset);
        return calendar.getTime();
    }

    /**
     * 根据日期，获取当天开始时间
     * */
    public static Date getDateStartTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        /*
        * Calendar.HOUR_OF_DAY:是指获取24小时制的小时,取值范围:0-23;
        * Calendar.HOUR:是指获取12小时制的小时,取值范围:0-12,凌晨和中午都是0,不是12;
        * 需要配合Calendar.AM_PM使用;
        * */
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /**
     * 根据日期，获取当天结束时间
     * */
    public static Date getDateEndTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        return calendar.getTime();
    }

    /**
     * 获取上个月开始时间
     *
     * @return
     */
    public static Date getLastMonthStartTime(){
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        // 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取上个月结束时间
     *
     * @return
     */
    public static Date getLastMonthEndTime(){
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        // 获取当前月最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    /**
     * 格式化日期部分（不包括时间）<br>
     * 格式 yyyy-MM-dd
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串 yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        if (null == date) {
            return null;
        }
        return  formatDate(date,DATE_PATTERN);
    }

    /**
     * 格式化日期
     * */
    public static String formatDate(Date date,String patten){
        return new SimpleDateFormat(patten).format(date);
    }

    /**
     * 格式化日期 格式 yyyy-MM-dd
     * @param date
     * @return
     */
    public static Date parse(String date) {
        return parse(date, DATE_PATTERN, null);
    }
    /**
     * 以指定格式格式化日期
     *
     * @param date      时间字符串
     * @param format 格式
     * @return data
     */
    public static Date parse(String date, String format) {
        return parse(date, format, null);
    }

    public static Date parse(String s, String format, Locale locale) {
        return parse(s, format, locale, null);
    }

    public static Date parse(String s, String format, Locale locale, TimeZone zone) {
        if (s == null) {
            return null;
        }
        DateFormatCache cache = getDateFormat(format, locale, zone);
        cache.lock();
        try {
            return cache.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            cache.unlock();
        }
    }

    /**
     * 获取 SimpleDateFormat 对象
     *
     * @param format 格式
     * @return DateFormat
     */
    private static DateFormatCache getDateFormat(String format, Locale locale, TimeZone zone) {
        zone = ObjectUtils.defaultIfNull(zone, TimeZone.getDefault());
        locale = ObjectUtils.defaultIfNull(locale, Locale.getDefault());
        String key = zone.getID() + "->" + locale.toString() + "->" + format;
        if (!DATE_FORMAT_CACHE.containsKey(key)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("缓存日期格式:" + key);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
            dateFormat.setTimeZone(zone);
            DATE_FORMAT_CACHE.put(key, new DateFormatCache(new ReentrantLock(), dateFormat));
        }
        return DATE_FORMAT_CACHE.get(key);
    }

    /**
     * 格式化日期
     *
     * @param source String 字符型日期
     * @return Date 日期
     */
    public static Date parseFormat(String source){
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, formats.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formats.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formats.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formats.get(3));
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

    /**
     * 格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 相隔的天数
     *
     * @param big   大的时间
     * @param small 小时间
     * @return double
     */
    public static long dayInterval(Date big, Date small) {
        return interval(big, small, Calendar.DATE);
    }


    /**
     * 获取日期间隔
     *
     * @param big_   大的日期
     * @param small_ 小的日期
     * @param field  比较日期字段
     * @return date
     */
    public static long interval(Date big_, Date small_, int field) {
        Date big = big_;
        Date small = small_;
        boolean positive = big.after(small);
        if (!positive) {
            Date temp = big;
            big = small;
            small = temp;
        }
        long elapsed = 0;

        GregorianCalendar smallCalendar = clear(small,field);
        GregorianCalendar bigCalendar = clear(big,field);

        if (smallCalendar.equals(bigCalendar)) {
            return elapsed;
        }
        //如果循环过多是否有潜在的性能问题
        while (smallCalendar.before(bigCalendar)) {
            smallCalendar.add(field, 1);
            elapsed++;
        }
        return positive ? elapsed : -elapsed;
    }

    private static GregorianCalendar clear(Date date, int field) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int[] fields = new int[]{Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Calendar.HOUR, Calendar.HOUR_OF_DAY, Calendar.AM_PM, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND};
        for (int calendarField : fields) {
            if(calendarField > field){
                calendar.clear(field);
            }
        }
        return calendar;
    }

    /**
     * 获取当前日期的下一天
     *
     * @param date 时间
     * @return date
     */
    public static Date nextDay(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, 1);
        return roundTo(DateType.DAY, gc.getTime());
    }

    public static Date roundTo(DateType dateType, Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return dateType.roundTo(dateType, gc);
    }

    /**
     * 添加时间
     *
     * @param date  原始时间
     * @param field 字段项
     * @param value 字段项值
     * @return date
     */
    public static Date add(Date date, int field, int value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(field, value);
        return gc.getTime();
    }

    /**
     * 设置时间的某个字段
     *
     * @param date  原始时间
     * @param field 字段项
     * @param value 字段项值
     * @return date
     */
    public static Date set(Date date, int field, int value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(field, value);
        return gc.getTime();
    }

    /**
     * 获取时间的最大上限
     *
     * @param date   原始日期
     * @param fields 设置的时间格式字段
     * @return date
     */
    public static Date getActualMaximumTime(Date date, int... fields) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int field : fields) {
            calendar.set(field, calendar.getActualMaximum(field));
        }
        return calendar.getTime();
    }

    /**
     * 获取时间的最小下限
     *
     * @param date   原始日期
     * @param fields 设置的时间格式字段
     * @return date
     */
    public static Date getActualMinimumTime(Date date, int... fields) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int field : fields) {
            calendar.set(field, calendar.getActualMinimum(field));
        }
        return calendar.getTime();
    }

    /**
     * 最高到天
     * dd天HH小时mm分钟
     *
     * @param field   比较字段
     * @param between 间隔
     * @param format  格式
     * @return {String}
     */
    public static String intervalFormat(int field, long between, String format) {
        return intervalFormat(field, between, format, "0[^\\d]{1,}", "");
    }


    private static class DateFormatCache {

        private SimpleDateFormat format;

        private Lock lock;

        public DateFormatCache(Lock lock, SimpleDateFormat format) {
            this.lock = lock;
            this.format = format;
        }

        public void lock() {
            this.lock.lock();
        }

        public String format(Date date) {
            return this.format.format(date);
        }

        public void unlock() {
            this.lock.unlock();
        }

        public Date parse(String s) throws ParseException {
            return this.format.parse(s);
        }
    }

    public static String intervalFormat(int field, long between, String format, String zeroFormat, String repStr) {
        between = Math.abs(between);
        long day = Calendar.DATE == field ? field : 0, hour = (field == Calendar.HOUR_OF_DAY || field == Calendar.HOUR) ? between : 0, minute = (field == Calendar.MINUTE) ? between : 0, second = (field == Calendar.SECOND) ? between : 0;
        switch (field) {
            case Calendar.SECOND:
                minute = second / 60;
                second = second % 60;
                break;
            case Calendar.MINUTE:
                hour = minute / 60;
                minute = minute % 60;
                break;
            case Calendar.HOUR_OF_DAY:
                break;
            case Calendar.HOUR:
                day = hour / 24;
                hour = hour % 24;
                break;
            default://Calendar.DATE or Calendar.MONTH or Calendar.YEAR
                throw new RuntimeException("不支持的时间转换格式:" + field);
        }
        final String days = String.valueOf(day), hours = String.valueOf(hour), minutes = String.valueOf(minute), seconds = String.valueOf(second);
        String retVal = RegexpUtil.replace(format, "dd|HH|mm|ss", new RegexpUtil.AbstractReplaceCallBack() {
            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                if ("dd".equals(text)) {
                    return days;
                }
                if ("HH".equals(text)) {
                    return hours;
                }
                if ("mm".equals(text)) {
                    return minutes;
                }
                if ("ss".equals(text)) {
                    return seconds;
                }
                return text;
            }

        });
        return StringUtils.isBlank(zeroFormat) ? retVal : RegexpUtil.replace(retVal, zeroFormat, repStr);
    }

    /**
     * 对日期添加天数
     *
     * @param date  原始日期
     * @param value 添加的天数
     * @return {Date}
     */
    public static Date addDay(Date date, int value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, value);
        return gc.getTime();
    }

    /**
     * 获取字段项对应的时第一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
    public static Date first(Date date, int field) {
        return getActualMinimumTime(date, field);
    }

    /**
     * 获取字段项对应的最后时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return ${Date}
     */
    public static Date last(Date date, int field) {
        return getActualMaximumTime(date, field);
    }

    /**
     * 获取字段项对应的下一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
    public static Date next(Date date, int field) {
        return add(date, field, 1);
    }

    /**
     * 获取字段项对应的上一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
    public static Date prev(Date date, int field) {
        return add(date, field, -1);
    }

    public static Date getLastDayOfWeek(Date date) {
        while (getTimeField(date, Calendar.DAY_OF_WEEK) != 1) {
            date = nextDay(date);
        }
        return date;
    }

    public static int getTimeField(Date date, int field) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(field);
    }

    public static int getWeekOfYear(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(Calendar.WEEK_OF_YEAR);
    }

    public static Date setTimeField(Date date, int field, int timeNum) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(field, timeNum);
        return gc.getTime();
    }


    /**
     * 比较时间返回最小值
     *
     * @param dates 比较的时间数组
     * @return date
     */
    public static Date min(Date... dates) {
        Date min = null;
        for (Date date : dates) {
            if (min == null) {
                min = date;
            } else {
                min = date.getTime() < min.getTime() ? date : min;
            }
        }
        return min;
    }

    /**
     * 比较时间返回最大值
     *
     * @param dates 比较的时间数组
     * @return date
     */
    public static Date max(Date... dates) {
        Date max = null;
        for (Date date : dates) {
            if (max == null) {
                max = date;
            } else {
                max = date.getTime() > max.getTime() ? date : max;
            }
        }
        return max;
    }

    /**
     * 获取当前时间
     *
     * @return date
     */
    public static Date now() {
        return dateDriver.getTime();
    }

    public static int age(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("出生时间大于当前时间!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;//注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    /**
     * 为DateUtil.now提供的时间启动接口
     */
    public interface DateDriver {
        Date getTime();
    }

    /**
     * DateUtil.now 时间启动接口的默认实现
     */
    public static class SimpleDateDriver implements DateDriver {

        @Override
        public Date getTime() {
            return new Date();
        }

    }


    public enum DateType {
        DAY, HOUR, MINUTE, SECOND;

        public Date roundTo(DateType dateType, GregorianCalendar gc) {
            switch (dateType) {
                case DAY:
                    gc.set(Calendar.HOUR_OF_DAY, 0);
                    return gc.getTime();
                case HOUR:
                    gc.set(Calendar.MINUTE, 0);
                    gc.set(Calendar.SECOND, 0);
                    gc.set(Calendar.MILLISECOND, 0);
                    return gc.getTime();
                case MINUTE:
                    gc.set(Calendar.SECOND, 0);
                    gc.set(Calendar.MILLISECOND, 0);
                    return gc.getTime();
                case SECOND:
                    gc.set(Calendar.MILLISECOND, 0);
                    return gc.getTime();
                default:
                    return gc.getTime();
            }
        }
    }
}
