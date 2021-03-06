package net.andy.com;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.R;
import net.andy.dispensing.ui.ReplenishUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import static net.andy.com.Application.getContext;

/**
 * Created by mac on 16-四月-1.
 */
public class MqttNotification {
    private static Context context = getContext();
    private static NotificationManager manager;
    private static Integer id = 1000;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void notification(String message) {
        Map map = JSON.parseObject(message,Map.class);
        Log.e("通知",map.toString());
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if("replenish".equals(map.get("type"))){
            Intent resultIntent = new Intent(context, ReplenishUI.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notifyBuilder = null;
            try {
                notifyBuilder = new Notification.Builder(context)
                        .setContentTitle((CharSequence) map.get("sender"))
                        .setContentText((CharSequence) map.get("message"))
                        .setWhen(format.parse(String.valueOf(map.get("sendDate"))).getTime())
                        .setTicker((CharSequence) map.get("type"))
                        .setSmallIcon(R.drawable.ic_launcher2_1)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true).build();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            manager.notify(id++, notifyBuilder);
        }else if("newPres".equals(map.get("type"))){
            Intent intent = new Intent();//创建Intent对象
            intent.setAction("net.andy.com.MqttNotification");
            intent.putExtra("bindId", String.valueOf(map.get("bindId")));
            intent.putExtra("message", String.valueOf(map.get("message")));
            getContext().sendBroadcast(intent);//发送广播
            Log.e("广播发送成功",map.toString());
        }

    }

}
