package net.andy.boiling.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import net.andy.boiling.R;
import net.andy.boiling.ui.TestUI;


/**
 * 使用轮询请求服务
 * @author RongGuang
 */
public class PollingService extends Service {
    private String TAG="PollingService";
    public static final String ACTION = "net.andy.boiling.service.PollingService";
    private NotificationManager mManager;
    private static final int HELLO_ID = 1;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.i(TAG,"服务启动");
        initNotifiManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new PollingThread().start();
        return super.onStartCommand(intent, flags, startId);
    }
    //初始化通知栏配置
    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    //浸泡完成通知
    private void showNotification(int preId, String color,String tagNo, String date) {
        //Navigator to the new activity when click the notification title
        Intent i = new Intent(this, TestUI.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,0);
        i.putExtra("preId",preId);
        Context context = getApplicationContext();
        Notification.Builder mBuilder=  new Notification.Builder(context);
        mBuilder.setContentTitle("浸泡完成")//设置通知栏标题
                .setContentText(color+tagNo+"号处方于"+date+"浸泡完成，点击进行煎制。") //设置通知栏显示内容
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setTicker(color+tagNo+"号处方于"+date+"浸泡完成。") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.drawable.ic_launcher)//设置通知小ICON
                .setAutoCancel(true)//打开后自动关闭
                .setDefaults(Notification.DEFAULT_ALL);
        mManager.notify(preId, mBuilder.build());
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     */
    int count = 0;
    class PollingThread extends Thread {
        @Override
        public void run() {
//            Log.i(TAG,">>->Polling...");
//            count ++;
            //当计数能被5整除时弹出通知
//            if (count % 5 == 0) {
                Log.i(TAG,">>->New message!");
//                showNotification(new Random().nextInt(10),"红色","sdfdsfsd89","14:40");
//                showNotification();
//            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Service:onDestroy");
    }
}