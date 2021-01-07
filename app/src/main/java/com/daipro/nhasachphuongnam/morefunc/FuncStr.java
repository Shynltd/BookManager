package com.daipro.nhasachphuongnam.morefunc;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class FuncStr {
    public static String getCharFirstName(String name){
        String arr[]=name.split(" ");
        String justName=arr[arr.length-1];
        return (justName.charAt(0)+"").toUpperCase();
    }
    public static String getKW(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "").replaceAll("đ", "d").replaceAll("Đ", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static long timeToMilies(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date= simpleDateFormat.parse(time);
            return date.getTime();
        }catch (Exception e){

        }
        return 0;
    }
    public static String miliesToTime(long time){
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formater.format(calendar.getTime());
    }
    public static String toDateString(int year,int month,int day){
        String monthStr,dayStr;
        if (month<10){
            monthStr="0"+month;
        } else  {
            monthStr=month+"";
        }
        if (day<10){
            dayStr="0"+day;
        } else {
            dayStr=day+"";
        }
        return dayStr+"/"+monthStr+"/"+year;
    }

    public static String timeAgo(long time) {
        long currentMilis = System.currentTimeMillis();
        long second = (currentMilis - time) / 1000;
        String agoReuslt = "";
        if (second < 60)
            agoReuslt = (int) second + " giây trước";
        else if (second / 60 < 60)
            agoReuslt = (int) (second / 60) + " phút trước";
        else if (second / 3600 < 24)
            agoReuslt = (int) (second / 3600) + " giờ trước";
        else if (second / 86400 < 30)
            agoReuslt = (int) (second / 86400) + " ngày trước";
        else if (second / 2592000 < 13)
            agoReuslt = (int) (second / 2592000) + " tháng trước";
        else
            agoReuslt = (int) (second / 31536000) + " năm trước";
        return agoReuslt;
    }
    public static String reWriteMoney(Double money){
        String kq="";
        String ms=String.format("%.2f", money);
        String[]arr=ms.split(",",2);
        String chan=arr[0];
        if (arr[0].length()<4){
            kq=chan;
        } else {
            while (chan.length()>=4){
                int length=chan.length();
                String need=chan.substring(length-3, length);
                kq=","+need+kq;
                chan= chan.substring(0,length-3);
            }
            kq=chan+kq+" VNĐ";
        }
        return kq;
    }
}
