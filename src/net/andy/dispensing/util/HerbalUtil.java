package net.andy.dispensing.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Guang on 2016/5/27.
 */
public class HerbalUtil {
    static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String transferLongToDate(String dateFormat,Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    /**
     * 时间格式化
     * @param date 精确到秒的字符串
     * @param format
     * @return
     */
    public static String formatDate(Date date,String format) {
        if(date == null  || date.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    public static String formatDate(String date,String format) {
        if(date == null  || date.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str,String format){
        try {
            if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static Date String2Date(String date_str,String format){
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        try {
            return sdf.parse(date_str);
        } catch (ParseException e) {
            e.printStackTrace();
           return null;
        }
    }
    public static Date getTime(String sign){
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        if("Month".equals(sign)){
            cal_1.set(Calendar.DAY_OF_MONTH, 1);
            return cal_1.getTime();
        }
        return null;
    }
    public static void getNameValuePair(Object object, List<NameValuePair> pairs) throws Exception {
        if (object != null) {
            // 拿到该类
//            Class<?> clz = object.getClass();
            // 获取实体类的所有属性，返回Field数组
            Field[] fields=object.getClass().getDeclaredFields();
            String name;
            Method method;
            Object val;
            //遍历
            for(Field field:fields){
                field.setAccessible(true);
                //获取一个属性
                 name= field.getName();
                   if (field.getGenericType().toString().equals(
                        "class java.util.Date")) {
//                    method = (Method) object.getClass().getMethod(
//                            "get" + getMethodName(field.getName()));
//                    val = (Date) method.invoke(object);
//                    if (val != null) {
//                        pairs.add(new BasicNameValuePair(name, simpleDateFormat.format(val)));
//                    }
                }else {
                       //获取该属性的值，可能为空
                       method = (Method) object.getClass().getMethod(
                               "get" + getMethodName(field.getName()));
                       val = method.invoke(object);// 调用getter方法获取属性值
                       if (val != null) {
                           pairs.add(new BasicNameValuePair(name, String.valueOf(val)));
                       }
                   }
            }
        }
    }
    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fildeName) throws Exception{
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}
