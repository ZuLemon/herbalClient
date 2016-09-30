import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Guang on 2016/4/21.
 */
public class TimeTest {
    public static void main(String args[]){
        System.out.println( );

    }
    public static long getMinSub(String beginDateStr,String endDateStr)
    {
        long min=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date beginDate;
        java.util.Date endDate;
        try
        {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
            System.out.println(">"+endDate.getTime());
            min=(endDate.getTime()-beginDate.getTime())/(60*1000);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return min;
    }
}
