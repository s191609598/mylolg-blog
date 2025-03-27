package com.mylog.common.utils;


import com.mylog.common.exception.MyException;
import com.mylog.common.utils.resultutils.ErrorCode;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author pss
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {

    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";

    /**
     * 日期格式1 yyyy-MM-dd
     */
    public static final String FORMAT_1 = "yyyy-MM-dd";
    /**
     * 日期格式2 yyyy/MM/dd
     */
    public static final String FORMAT_2 = "yyyy/MM/dd";
    /**
     * 日期格式3 yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_3 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式4 yyyy/MM/dd HH:mm:ss
     */
    public static final String FORMAT_4 = "yyyy/MM/dd HH:mm:ss";
    /**
     * 日期格式5 yyyyMMdd
     */
    public static final String FORMAT_5 = "yyyyMMdd";
    /**
     * 日期格式6 yyyyMMddHHmmss
     */
    public static final String FORMAT_6 = "yyyyMMddHHmmss";
    /**
     * 日期格式7 HH:mm:ss
     */
    public static final String FORMAT_7 = "HH:mm:ss";
    /**
     * 日期格式 yyyyMMddHHmmssSSS
     **/
    public static final String FORMAT_8 = "yyyyMMddHHmmssSSS";
    /**
     * 日期格式9 MM-dd
     */
    public static final String FORMAT_9 = "MM-dd";
    /**
     * 日期格式9 MM/dd
     */
    public static final String FORMAT_10 = "MM/dd";
    /**
     * 日期格式4 yyyy.MM.dd HH:mm
     */
    public static final String FORMAT_11 = "yyyy.MM.dd HH:mm";
    public static final String FORMAT_12 = "yyyy年MM月dd日";

    public static final String FORMAT_13 = "yyyy-MM-dd'T'HH:mm:ss";


    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;
    private static final int SECONDS_PER_DAY = (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE);


    static Long[] timeDiffs = {1L, 60L, 3600L, 86400L, 2592000L, 31536000L};


    static char[] stringDesc = {'秒', '分', '时', '天', '月', '年'};


    public static String getIntervalMonth(Calendar now, int interval) {
        now.add(Calendar.MONTH, interval);
        SimpleDateFormat df = new SimpleDateFormat(YEAR_MONTH_FORMAT);
        return df.format(now.getTime());
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static boolean after(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        return c2.after(c1);
    }

    // 获得当天0点时间
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static Date getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        Date data = null;
        try {
            data = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String parseDateStr(Timestamp timestamp) {

        String tsStr = "";
        if (timestamp != null) {
            DateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_3);
            try {
                tsStr = sdf.format(timestamp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tsStr;
    }


    /**
     * 格式化日期成str
     *
     * @param date
     * @param format 日期格式
     * @return
     * @author tanxin
     */
    public static String formatDateStr(Object date, String format) {
        String s;
        try {
            s = formatDateStr(date.toString(), format);
        } catch (Exception e) {
            throw new MyException(ErrorCode.PARAMS_ERROR);
        }
        return s;
    }

    /**
     * 格式化日期成str
     *
     * @param date
     * @param format 日期格式
     * @return
     * @author tanxin
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(date);
        return dateString;
    }


    /**
     * 格式化日期成str
     *
     * @return
     */
    public static String formatDateStr(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date1 = DateUtil.getDate(date, DateUtil.FORMAT_3);
        return sdf.format(date1);
    }


    /**
     * 获取本月开始时间
     *
     * @param date yyyy-MM-dd
     * @return yyyy-MM-dd
     * @author tanxin
     */
    public static String getDateStr(String date) {
        String reg = "^\\d{4}\\D+\\d{2}\\D+\\d{2}$";
        boolean b = Pattern.compile(reg).matcher(date).matches();
        if (b) {
            String newDate = date.substring(0, date.length() - 2);
            return newDate + "01";
        }
        return null;
    }

    /**
     * 获取时间戳
     *
     * @return
     * @author tanxin
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取时间 Date-->String yyyyMMddHHmmss
     *
     * @return
     * @author tanxin
     */
    public static String getDateTimeLong(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_6);
        String dateString = sdf.format(date);
        return dateString;
    }

    /**
     * 获取时间 Date-->String yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeLo(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_3);
        String dateString = sdf.format(date);
        return dateString;
    }

    /**
     * 获取时间 Date-->String yyyy-MM-dd
     */
    public static String getDateStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.FORMAT_1);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 字符串转date
     *
     * @param dateStr 字符串
     * @param format  时间格式
     * @return
     * @author tanxin
     */
    public static Date getDate(String dateStr, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转date
     *
     * @param dateStr 字符串
     * @return
     * @author tanxin
     */
    public static Date getDate(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_1);
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得开始时间
     *
     * @param date
     * @return
     */
    public static Date getDayStartTime(Date date) {
        Date start = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_1);
            SimpleDateFormat formater = new SimpleDateFormat(DateUtil.FORMAT_3);
            start = formater.parse(sdf.format(date) + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return start;
    }

    /**
     * 获得结束时间
     *
     * @param date
     * @return
     */
    public static Date getDayEndTime(Date date) {
        Date end = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_1);
            SimpleDateFormat formater = new SimpleDateFormat(DateUtil.FORMAT_3);
            end = formater.parse(sdf.format(date) + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return end;
    }

    /**
     * 获取当前时间后面几天的时间
     *
     * @param date
     * @param days 后面第几天
     * @return
     * @author tanxin
     */
    public static Date getDaysAfterDate(Date date, Integer days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        Date afterDate = c.getTime();
        return afterDate;
    }

    /**
     * 获取当前时间后几分钟的时间
     *
     * @param date
     * @param mins 后面几分钟
     * @return
     * @author tanxin
     */
    public static Date getMinAfterDate(Date date, Integer mins) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, mins);
        Date afterDate = c.getTime();
        return afterDate;
    }

    /**
     * 获取当前时间后几个月的时间
     *
     * @param date
     * @param month 后面几个月
     * @return
     * @author tanxin
     */
    public static Date getMonthAfterDate(Date date, Integer month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        Date afterDate = c.getTime();
        return afterDate;
    }

    /**
     * 获取当前时间后几年时间
     *
     * @param date
     * @param year 后面几年
     * @return
     * @author tanxin
     */
    public static Date getYearAfterDate(Date date, Integer year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        Date afterDate = c.getTime();
        return afterDate;
    }

    public static String getStartDateStr(String dateStr) {
        return dateStr + " 00:00:00";
    }

    public static String getEndDateStr(String dateStr) {
        return dateStr + " 23:59:59";
    }

    static Map<String, String> dateRegFormat = new HashMap<String, String>();

    static {
        dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd-HH-mm-ss");// 2014年3月12日 13时5分34秒，2014-03-12 //
        // 12:05:34，2014/3/12 12:5:34
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH-mm");// 2014-03-12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH");// 2014-03-12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");// 2014-03-12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}$", "yyyy-MM");// 2014-03
        dateRegFormat.put("^\\d{4}$", "yyyy");// 2014
        dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");// 20140312120534
        dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");// 201403121205
        dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");// 2014031212
        dateRegFormat.put("^\\d{8}$", "yyyyMMdd");// 20140312
        dateRegFormat.put("^\\d{6}$", "yyyyMM");// 201403
        dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm-ss");// 13:05:34
        dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm");// 13:05
        dateRegFormat.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");// 14.10.18(年.月.日)
        dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}$", "yyyy-dd-MM");// 30.12(日.月)
        dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");// 12.21.2013(日.月.年)
    }

    @SuppressWarnings("finally")
    public static String FormatDate(String dateStr) {
        String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        DateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat formatter2;
        String dateReplace;
        String strSuccess = "";
        try {
            for (String key : dateRegFormat.keySet()) {
                if (Pattern.compile(key).matcher(dateStr).matches()) {
                    formatter2 = new SimpleDateFormat(dateRegFormat.get(key));
                    if (key.equals("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$") || key.equals("^\\d{2}\\s*:\\s*\\d{2}$")) {// 13:05:34//
                        // 或
                        // 13:05
                        // 拼接当前日期
                        dateStr = curDate + "-" + dateStr;
                    } else if (key.equals("^\\d{1,2}\\D+\\d{1,2}$")) {// 21.1//
                        // (日.月)
                        // //
                        // 拼接当前年份
                        dateStr = curDate.substring(0, 4) + "-" + dateStr;
                    }
                    dateReplace = dateStr.replaceAll("\\D+", "-");
                    strSuccess = formatter1.format(formatter2.parse(dateReplace));
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("-----------------日期格式无效:" + dateStr);
            throw new Exception("日期格式无效");
        } finally {
            return strSuccess;
        }
    }

    /**
     * jdk 8 获取时间localDate
     *
     * @param dateStr yyyy-MM-dd
     * @return
     * @author tanxin
     */
    public static LocalDate getLocalDate(String dateStr) {
        String str[] = dateStr.split("-");
        int year = Integer.parseInt(str[0].trim());
        int month = Integer.parseInt(str[1].trim());
        int dayOfMonth = Integer.parseInt(str[2].trim());
        LocalDate toDate = LocalDate.of(year, month, dayOfMonth);
        return toDate;
    }

    /**
     * 将毫秒数转换为时间
     *
     * @param time
     * @return
     * @author tanxin
     */
    public static Date Long2Date(long time) {
        Date date = new Date();
        date.setTime(time);
        return date;
    }


    public static String getYear(Date date) {
        // Date date = new Date();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        // System.out.println(yearFormat.format(date));
        return yearFormat.format(date);

    }


    /**
     * 只获取天数
     **/
    public static String getDay(Date date) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        return dayFormat.format(date);

    }

    /**
     * 一天的毫秒数
     **/
    private static final long DAY_MILLISECONDS = SECONDS_PER_DAY * 1000L;

    /**
     * 转换方法
     *
     * @parma numberString 要转换的浮点数
     * @parma format 要获得的格式 例如"hh:mm:ss"
     **/
    public static Date toDate(double numberString) {
        int wholeDays = (int) Math.floor(numberString);
        int millisecondsInday = (int) ((numberString - wholeDays) * DAY_MILLISECONDS + 0.5);
        Calendar calendar = new GregorianCalendar();
        setCalendar(calendar, wholeDays, millisecondsInday, false);
        return calendar.getTime();

    }

    private static void setCalendar(Calendar calendar, int wholeDays, int millisecondsInDay, boolean use1904windowing) {
        int startYear = 1900;
        int dayAdjust = -1; // Excel thinks 2/29/1900 is a valid date, which it
        // isn't
        if (use1904windowing) {
            startYear = 1904;
            dayAdjust = 1; // 1904 date windowing uses 1/2/1904 as the first day
        } else if (wholeDays < 61) {
            dayAdjust = 0;
        }
        calendar.set(startYear, 0, wholeDays + dayAdjust, 0, 0, 0);
        calendar.set(GregorianCalendar.MILLISECOND, millisecondsInDay);
    }

    /**
     * 获得开始时间
     *
     * @param date
     * @return
     */
    public static String getStartTimeStr(Date date) {
        String start = null;
        try {
            SimpleDateFormat formater = new SimpleDateFormat(DateUtil.FORMAT_1);
            start = formater.format(date) + " 00:00:00";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return start;
    }

    /**
     * 获得结束时间
     *
     * @param date
     * @return
     */
    public static String getEndTimeStr(Date date) {
        String end = null;
        try {
            SimpleDateFormat formater = new SimpleDateFormat(DateUtil.FORMAT_1);
            end = formater.format(date) + " 23:59:59";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return end;
    }


    /**
     * 获得开始时间
     *
     * @param dateStr
     * @return Date
     */
    public static Date getStartDate(String dateStr) {

        dateStr += " 00:00:00";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_3);
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获得开始时间
     *
     * @param dateStr
     * @return Date
     */
    public static Date getEndDate(String dateStr) {

        dateStr += " 23:59:59";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_3);
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 跟当前时间比，获取剩余天数
     *
     * @param date
     * @return
     */
    public static long getCountdownDays(Date date, int type) {

        Date now = new Date();
        long l = date.getTime() - now.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (type == 1) {
            return day;
        } else if (type == 2) {
            return hour;
        } else if (type == 3) {
            return min;
        } else if (type == 4) {
            return s;
        }

        return day;

    }


    /**
     * 获取当月的第一天：
     **/
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }


    /**
     * 两个日期之间相差的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static final int daysBetween(Date start, Date end) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(start);
        caled.setTime(end);
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
        return days;
    }

    /**
     * 刚刚
     * 几分钟前
     * 几天天
     * 几小时前
     *
     * @return
     */
    public static String descDate(Date date) {


        String defaultdesc = "刚刚";

        if (date == null) {
            return defaultdesc;
        }


        Date nowDate = new Date();

        Long time1 = date.getTime() / 1000;

        Long time2 = nowDate.getTime() / 1000;


        Long diffTime = time2 - time1;

        int i = timeDiffs.length - 1;
        for (; i >= 0; i--) {

            if (diffTime > timeDiffs[i]) {
                break;
            }
        }

        if (i <= 0) {
            return defaultdesc;
        }


        StringBuilder builder = new StringBuilder();
        builder.append(diffTime / timeDiffs[i]);
        builder.append(stringDesc[i]);
        builder.append("前");


        return builder.toString();
    }

    public static Date getDateByString(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_3);
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * 获取指定时间的 0点
     * 返回时间格式如：2021-12-22 00:00:00
     *
     * @param time
     * @return
     */
    public static String getStartOfDayStr(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    /**
     * 获取指定时间的 0点
     * 返回时间格式如：2021-12-22 00:00:00
     *
     * @param time
     * @return
     */
    public static Date getStartOfDayDate(Date time) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取指定时间的 0点
     * 返回时间格式如：2021-12-22 23:59:59
     *
     * @param time
     * @return
     */
    public static String getEndOfDayStr(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    public static Date getDateFormat(Date date) {
        return getDateFormat(date, FORMAT_3);
    }

    public static Date getDateFormat(Date date, String format) {
        SimpleDateFormat df = null;
        if (StringUtils.isNotBlank(format)) {
            df = new SimpleDateFormat(format);
        } else {
            df = new SimpleDateFormat(FORMAT_3);
        }
        try {
            String format1 = df.format(date);
            return df.parse(format1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定时间的 0点
     * 返回时间格式如：2021-12-22 23:59:59
     *
     * @param time
     * @return
     */
    public static Date getEndOfDayDate(Date time) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static int getYear() {
        LocalDate now = LocalDate.now();
        return now.getYear();
    }

    public static int getMonth() {
        LocalDate now = LocalDate.now();
        return now.getMonthValue();
    }

    public static int getDay() {
        LocalDate now = LocalDate.now();
        return now.getDayOfMonth();
    }

    public static String getMonthName(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "Mar";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEPT";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            default:
                return "DEC";
        }
    }

    public static Date parseStringToDateTime(String time) {
        return parseStringToDateTime(time, FORMAT_3);
    }

    public static Date parseStringToDateTime(String time, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime parse = LocalDateTime.parse(time, df);
        return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date parseStringToDateTime(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime parse = LocalDateTime.ofInstant(date.toInstant(), zoneId);
        return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取指定时间的 0点
     * 返回时间格式如：2021-12-31 23:59:59
     *
     * @param time
     * @return
     */
    public static Date getEndOfMonthDate(Date time) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time.getTime()), ZoneId.systemDefault());
        localDateTime = localDateTime.withMonth(12);
        localDateTime = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getEndTimeOfDay(Date time) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat sdf1 = newSimpleFormat("yyyy-MM-dd");
        String date1Str = sdf1.format(time);
        SimpleDateFormat sdf2 = newSimpleFormat("yyyy-MM-dd HH:mm:ss");
        String date2Str = date1Str + " 23:59:59";
        Date date = null;
        try {
            date = sdf2.parse(date2Str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static Date addThreeHours(Date date, Integer hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addThreeMinute(Date date, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
}
