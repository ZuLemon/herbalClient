package net.andy.dispensing.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Guang on 2016/5/27.
 */
public class HerbalUtil {
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
    public static Date getTime(String sign){
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        if("Month".equals(sign)){
            cal_1.set(Calendar.DAY_OF_MONTH, 1);
            return cal_1.getTime();
        }
        return null;
    }
}
