package com.etrans.myd2.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Created by Kevin.young  on 2015/9/11.
 * Lately modify by Kevin.young  on 2015/9/11.
 * Copyright @ 1996-2015 Kevin Corporation, All Rights Reserved
 * <p>
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils2 {

    public static SimpleDateFormat FORAMAT_01 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat FORAMAT_011 = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat FORAMAT_02 = new SimpleDateFormat("yyyy年MM月dd");
    public static SimpleDateFormat FORAMAT_03 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat FORAMAT_04 = new SimpleDateFormat("MM-dd HH:mm");
    public static SimpleDateFormat FORAMAT_05 = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat FORAMAT_06 = new SimpleDateFormat("yyyyMMdd_HHmmss");
    public static SimpleDateFormat FORAMAT_07 = new SimpleDateFormat("yyyyMMdd_HHmmss");
    public static SimpleDateFormat FORAMAT_08 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat FORAMAT_09 = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat FORAMAT_10 = new SimpleDateFormat("MM月dd日");
    public static SimpleDateFormat FORAMAT_11 = new SimpleDateFormat("M月d日");
    public static SimpleDateFormat FORAMAT_12 = new SimpleDateFormat("MM-dd");

    /**
     * format date string to date with "yyyy-MM-dd"
     *
     * @param dateStr
     * @return
     */
    public static Date parse_01(String dateStr) {
        Date date = null;
        try {
            date = FORAMAT_01.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * format date string to date with "yyyy年MM月dd"
     *
     * @param dateStr
     * @return
     */
    public static Date parse_02(String dateStr) {
        Date date = null;
        try {
            date = FORAMAT_02.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * format date string to date with yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static Date parse_03(String dateStr) {
        Date date = null;
        try {
            date = FORAMAT_03.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * format date string to date with "MM-dd HH:mm"
     *
     * @param dateStr
     * @return
     */
    public static Date parse_04(String dateStr) {
        Date date = null;
        try {
            date = FORAMAT_04.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * format date string to date with "HH:mm"
     *
     * @param dateStr
     * @return
     */
    public static Date parse_05(String dateStr) {
        Date date = null;
        try {
            date = FORAMAT_05.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * format date string to date with "yyyy-MM-dd HH:mm"
     *
     * @param dateStr
     * @return
     */
    public static Date parse_08(String dateStr) {
        Date date = null;
        try {
            date = FORAMAT_08.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * format date string to date with "yyyy-MM-dd HH:mm"
     *
     * @param dateStr
     * @return
     */
    public static Date parse_09(String dateStr) {
        Date date = null;
        try {
            date = FORAMAT_09.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return yyyy-MM-dd
     */
    public static String format_1(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DATE, dayOfMonth);
        return FORAMAT_01.format(cal.getTime());
    }

    /**
     * @param date
     * @return yyyy-MM-dd
     */
    public static String format_1(Date date) {
        return date == null ? "" : FORAMAT_01.format(date);
    }

    /**
     * @param date
     * @return yyyy年MM月dd
     */
    public static String format_2(Date date) {
        return date == null ? "" : FORAMAT_02.format(date);
    }

    /**
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String format_3(Date date) {
        return date == null ? "" : FORAMAT_03.format(date);
    }

    public static String format_4(Date date) {
        return date == null ? "" : FORAMAT_04.format(date);
    }

    public static String format_5(Date date) {
        return date == null ? "" : FORAMAT_05.format(date);
    }

    public static String format_8(Date date) {
        return date == null ? "" : FORAMAT_08.format(date);
    }

    public static String format_9(Date date) {
        return date == null ? "" : FORAMAT_09.format(date);
    }

    public static String format_10(Date date) {
        return date == null ? "" : FORAMAT_10.format(date);
    }
    public static String format_11(Date date) {
        return date == null ? "" : FORAMAT_11.format(date);
    }
    public static String format_12(Date date) {
        return date == null ? "" : FORAMAT_12.format(date);
    }

    public static Date getPreDay(Date now, int pre) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, pre);
        return calendar.getTime();
    }

    public static Date getPreWeek(Date now, int pre) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.WEEK_OF_MONTH, pre);
        return calendar.getTime();
    }

    //获得月份 i 的值为向前递进 返回1 ,2,3
    public static String getMonth(int i){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        if ((month-i)<=0){
            month=month-i+12;
        }else{
            month=month-i;
        }
        return month+"月";
    }

    //获得月份 i 的值为向前递进 返回一,二，三
    public static String getMonthChina(int i){
        String[] monthString={"一","二","三","四","五","六","七","八","九","十","十一","十二"};
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        if ((month-i)<=0){
            month=month-i+12;
        }else{
            month=month-i;
        }
        return monthString[month]+"月";
    }

    public static int getToday(){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        return day;
    }

    public static Date getPreMonth(Date now, int pre) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, pre);
        return calendar.getTime();
    }

    public static Date getToday(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date[] getYestoday(Date now) {
        Date[] result = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        // calendar.set(Calendar.HOUR_OF_DAY, 0);
        // calendar.set(Calendar.MINUTE, 0);
        // calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        result[0] = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        result[1] = calendar.getTime();
        return result;
    }

    //获取最近一月的天数（今天除外）   年月日 2017-03-02
    public static List<String> getAllTheDateOftheMonts(Date date) {
        //前7天
        List<Date> list = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        for (int i=0;i<31;i++){
            cal.add(cal.DATE, -1);
            list.add(cal.getTime());
        }
        ArrayList<String> datelist=new ArrayList<String>();
        for (Date date1:list){
            datelist.add(format_1(date1));
        }
        Collections.sort(datelist);
        return datelist;
    }

    //获取最近一周的天数（今天除外）   月日 03-02
    public static List<String> getAllTheDateOftheWeek(Date date) {
        //前7天
        List<Date> list = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        for (int i=0;i<7;i++){
            cal.add(cal.DATE, -1);
            list.add(cal.getTime());
        }
        ArrayList<String> datelist=new ArrayList<String>();
        for (Date date1:list){
            datelist.add(format_12(date1));
        }
        Collections.sort(datelist);
        return datelist;
    }

    //获得最近本月天数   年月日 2017-03-02
    public static List<String> getAllTheDateOftheMont(Date date) {
        //前3天
        List<Date> list = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day= Calendar.DATE;
        for (int i=0;i<31;i++){
            cal.add(cal.DATE, -1);
            list.add(cal.getTime());
        }
        ArrayList<String> datelist=new ArrayList<String>();
        for (Date date1:list){
            datelist.add(format_1(date1));
        }
        Collections.sort(datelist);
        return datelist;
    }

    //获得最近六个月日期 不算当月  年月日2017-01
    public static List<String> getMonthDay(int monthNumber) {
        String month = null;
        String dateString;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        dateString = sdf.format(cal.getTime());
        List<String> rqList = new ArrayList<String>();

        for (int i = 1; i <= monthNumber; i++) {
            cal.add(Calendar.MONTH, -1);//不算当月
            dateString = sdf.format(cal.getTime());
            rqList.add(dateString);
        }
        Collections.reverse(rqList);
        return rqList;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = parse_01(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = format_1(today);
            String timeDate = format_1(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * @mss 要转换的秒数, 转换为多少小时
     * @author
     */
    public static Long msToHours(long mss) {
//        long days = mss / (60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (60 * 60);
//        long minutes = (mss % (1000 * 60 * 60)) / 60;
        return hours;
    }

    /**
     * @mss 要转换的秒数，转换为分钟数
     * @author
     */
    public static int msToMinut(long mss) {
//        long days = mss / (60 * 60 * 24);
//        long hours = (mss % (1000 * 60 * 60 * 24)) / (60 * 60);
        Double ms = (Double.valueOf(mss) / (60 * 60));
        int minutes = (int) ((ms * 10) % 10) * 6;
        return minutes;
    }

    /**
     * 获取指定日后 后 dayAddNum 天的 日期
     *
     * @param day       日期，格式为String："2013-9-3";
     * @param dayAddNum 增加天数 格式为int;
     * @return
     */
    public static String getDateStr(String day, int dayAddNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    /**
     * 获取最近指定的日期
     * @return
     */
    public static ArrayList<Date> getLastDays(String date) {
        int count = 90;
        Calendar calendar = Calendar.getInstance();
        String[] dates = date.split("-");
        calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
        Long fTime = calendar.getTimeInMillis();

        String day=getTimeChange(new Date(), CommonUtils.DATE_FORMAT_DATE);
        Calendar calendar2 = Calendar.getInstance();
        String[] calendarday = day.split("-");
        calendar2.set(Calendar.YEAR, Integer.parseInt(calendarday[0]));
        calendar2.set(Calendar.MONTH, Integer.parseInt(calendarday[1]) - 1);
        calendar2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(calendarday[2]));
//        Long fTimeday = calendar2.getTimeInMillis();
        int days=(int)((calendar2.getTime().getTime()-calendar.getTime().getTime())/86400000);
        Date fdate;
        ArrayList<Date> list = new ArrayList<Date>();
        for (int a = count; a >= 0; a--) {
            fdate = beforLongDate(fTime, -a);
            list.add(count - a, fdate);
        }
        Date wdate;
        if (days>0){
            for(int i=1;i<days;i++){
                wdate=willLongDate(fTime,i);
                list.add(count+i, wdate);
            }
        }
        return list;
    }
    public static Date willLongDate(long millis, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        return date;
    }
    public static Date beforLongDate(long millis, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        return date;
    }
    public static String getTimeChange(Date beginDate, SimpleDateFormat dateFormat) {
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE));
        return dateFormat.format(date.getTime());
    }


}