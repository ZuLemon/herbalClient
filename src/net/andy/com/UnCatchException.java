package net.andy.com;

import android.widget.Toast;

/**
 * UnCatchException
 *
 * @author RongGuang
 * @date 2015/11/30
 */
public class UnCatchException implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
//        Toast.makeText(mContext, "很抱歉,程序出现异常", Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }
}
