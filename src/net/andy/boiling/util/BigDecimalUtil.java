package net.andy.boiling.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-29
 * Time: 上午11:01
 * BigDecimal工具类
 */
public class BigDecimalUtil {
    /**
     * BigDecimal格式化
     *
     * @param value 需要格式化的对象
     * @return 格式化结果
     */
    public static String formatValue(Object value) {
        String content;
        if (value == null) {
            content = "";
        } else {
            if (value instanceof BigDecimal) {
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(2);
                nf.setMaximumFractionDigits(2);
                content = nf.format(value);
            } else {
                content = String.valueOf(value);
            }
        }
        return content;
    }

    /**
     * 两个BigDecimal类型加法
     *
     * @param bd1 金额1
     * @param bd2 金额2
     * @return 结果
     */
    public static BigDecimal add(BigDecimal bd1, BigDecimal bd2) {
        BigDecimal bd;
        bd = bd1.add(bd2);
        return bd;
    }

    /**
     * 两个BigDecimal类型减法
     *
     * @param bd1 金额1
     * @param bd2 金额2
     * @return 结果
     */
    public static BigDecimal subtract(BigDecimal bd1, BigDecimal bd2) {
        BigDecimal bd;
        bd = bd1.subtract(bd2);
        return bd;
    }

    /**
     * 两个BigDecimal类型乘法
     *
     * @param bd1 金额1
     * @param bd2 金额2
     * @return 结果
     */
    public static BigDecimal multiply(BigDecimal bd1, BigDecimal bd2) {
        BigDecimal bd;
        bd = bd1.multiply(bd2);
        return bd;
    }

    /**
     * 两个BigDecimal类型除法
     *
     * @param bd1 金额1
     * @param bd2 金额2
     * @param scale 保留小数位数
     * @return 结果
     */
    public static BigDecimal divide(BigDecimal bd1, BigDecimal bd2, int scale) {
        BigDecimal bd;
        bd = bd1.divide(bd2,scale, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static void main(String[] arg) {
        Long time = System.currentTimeMillis();

        String seq = String.valueOf(System.currentTimeMillis());


        System.out.println("格式化-->"+seq);
        System.out.println("格式化-->"+seq.substring(seq.length() - 10,seq.length()));
    }
}
