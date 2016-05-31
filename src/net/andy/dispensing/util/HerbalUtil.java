package net.andy.dispensing.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Guang on 2016/5/27.
 */
public class HerbalUtil {
    public static String transferLongToDate(String dateFormat,Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }
}
