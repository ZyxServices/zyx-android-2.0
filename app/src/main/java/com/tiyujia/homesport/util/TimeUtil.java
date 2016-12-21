package com.tiyujia.homesport.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zzqybyb19860112 on 2016/9/7.
 */
public class TimeUtil {
    private final static long minute = 60 * 1000;// 1分钟
          private final static long hour = 60 * minute;// 1小时
          private final static long day = 24 * hour;// 1天
          private final static long month = 31 * day;// 月
          private final static long year = 12 * month;// 年


    public static String getDetailTime(long timeMillions){
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日HH时mm分");
        String temptime=sdf.format(new Date(timeMillions));
        String month=temptime.substring(0, 2);
        String day=temptime.substring(3, 5);
        String hour=temptime.substring(6, 8);
        String minute=temptime.substring(9,11);
        month= NumberUtil.handleNumber(month);
        day=NumberUtil.handleNumber(day);
        hour=NumberUtil.handleNumber(hour);
        minute=NumberUtil.handleNumber(minute);
        return month+"月"+day+"日"+hour+"时"+minute+"分";
    }
    public static String getDetailTimeNumber(long timeMillions){
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日HH时mm分");
        String temptime=sdf.format(new Date(timeMillions));
        String month=temptime.substring(0, 2);
        String day=temptime.substring(3, 5);
        String hour=temptime.substring(6, 8);
        String minute=temptime.substring(9,11);
        month=NumberUtil.handleNumber(month);
        day=NumberUtil.handleNumber(day);
        return month+"月"+day+"日  "+hour+":"+minute;
    }
    public static String getMonthDayTime(long timeMillions){
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日");
        String temptime=sdf.format(new Date(timeMillions));
        String month=temptime.substring(0, 2);
        String day=temptime.substring(3, 5);
        month=NumberUtil.handleNumber(month);
        day=NumberUtil.handleNumber(day);
        return month+"月"+day+"日";
    }
    public static String getNumberTime(long timeMillions){
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日HH时mm分");
        String temptime=sdf.format(new Date(timeMillions));
        String month=temptime.substring(0, 2);
        String day=temptime.substring(3, 5);
        String hour=temptime.substring(6, 8);
        String minute=temptime.substring(9,11);
        month=NumberUtil.handleNumber(month);
        day=NumberUtil.handleNumber(day);
        return month+"月"+day+"日  "+hour+":"+minute;
    }
    public static String getDisTime(long nowTime,long beginTime){
        long disTime=beginTime-nowTime;
        String result="";
        if (disTime<86400L*1000L){
            long hour= disTime/3600000L;
            long minute=(disTime-hour*3600000L)/60000L;
            result=hour+"小时"+minute+"分";
        }else {
            long day=  (disTime/86400000L);
            long hour= (disTime-day*86400000L)/3600000L;
            long minute=(disTime-day*86400000L-hour*3600000L)/60000L;
            result=day+"天"+hour+"小时"+minute+"分";
        }
        return result;
    }
    public static String checkTime(long timeInMillions){
        String time="";
        Calendar calendarBegin=Calendar.getInstance(Locale.CHINESE);
        calendarBegin.setTimeInMillis(timeInMillions);
        Calendar calendarNow=Calendar.getInstance(Locale.CHINESE);
        long nowTimeInMillions=System.currentTimeMillis();
        calendarNow.setTimeInMillis(nowTimeInMillions);
        long disTime=timeInMillions-nowTimeInMillions;
        int beginDay= calendarBegin.get(Calendar.DAY_OF_WEEK);
        int realDay= calendarNow.get(Calendar.DAY_OF_WEEK);
        int realWeek= calendarNow.get(Calendar.WEEK_OF_YEAR);
        int beginWeek= calendarBegin.get(Calendar.WEEK_OF_YEAR);
        if(disTime<0){
            SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:ss");
            time=sdf.format(new Date(timeInMillions));
        }else {
        if (beginDay==realDay&&disTime<=1000L*60L*60L*24L*7L){
            time="今天"+setStartTime(timeInMillions);
        }else if (beginWeek==realWeek){
            time="本周"+setNumberToChinese(beginDay)+setStartTime(timeInMillions);
        }else if(beginWeek>realWeek){
            time="下周"+setNumberToChinese(beginDay)+setStartTime(timeInMillions);
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:ss");
            time=sdf.format(new Date(timeInMillions));
        }}
        return time;
    }
    private static String setStartTime(long timeInMillions){
        Date date=new Date();
        date.setTime(timeInMillions);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String time=sdf.format(date);
        String hour=time.substring(0,2);
        if (Integer.valueOf(hour)<10){
            time=time.substring(1);
            return time;
        }
        return time;
    }
    private static String setNumberToChinese(int beginDay) {
        String dayInCN="";
        switch (beginDay){
            case 1:dayInCN="日";break;
            case 2:dayInCN="一";break;
            case 3:dayInCN="二";break;
            case 4:dayInCN="三";break;
            case 5:dayInCN="四";break;
            case 6:dayInCN="五";break;
            case 7:dayInCN="六";break;
        }
        return dayInCN;
    }
    public static String setLoveNum(int number){
        String favNumber="";
        if (number<1000){
            favNumber=number+"";
        }else if (number<10000){
            favNumber=number/1000+"K";
        }else{
            favNumber=number/10000+"万";
        }
        return favNumber;
    }
    /**
     51      * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     52      *
     53      * @param date
     54      * @return
     55      */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 毫秒转时分秒
     * @param l
     * @return
     */
    public static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = l.intValue() / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute)  + ":"  + getTwoLength(second));
    }

    private static String getTwoLength(final int data) {
        if(data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }


}
