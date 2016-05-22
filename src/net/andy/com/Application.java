package net.andy.com;

import android.content.Context;
import android.content.Intent;
import net.andy.boiling.domain.Users;

/**
 * Created by Administrator on 2014-11-10.
 * 全局应用对象
 */
public class Application extends android.app.Application {
    private static Context context;
    private static Users users;
    private static int msgCount=0;
    private static int intervalTime=3;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new UnCatchException());
        /** 捕获全局异常   **/
//        CrashHandler catchHandler = CrashHandler.getInstance();
//        catchHandler.init(context);
    }

    public static Context getContext() {
        return context;
    }
    public static Users getUsers() {
        return users;
    }
    public static void setUsers(Users users) {
        Application.users = users;
    }
    public static int getMsgCount() {
        return msgCount;
    }
    public static void setMsgCount(int msgCount) {
        Application.msgCount = msgCount;
    }
    public static int getIntervalTime() {
        return intervalTime;
    }
    public static void setIntervalTime(int intervalTime) {
        Application.intervalTime = intervalTime;
    }
}
