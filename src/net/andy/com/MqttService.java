package net.andy.com;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.alibaba.fastjson.JSONArray;
import net.andy.boiling.domain.ReturnDomain;
import org.apache.http.NameValuePair;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 16-三月-24.
 * 推送服务
 */
public class MqttService extends Service {
    private static MqttClient mqttClient;
    private static MqttConnectOptions options = new MqttConnectOptions();
    private static String url[] = new String[]{};
    private static String user = "";
    private static String password = "";
    private static boolean hold = false;
    private static int idx = 0;

    public static void getServer() {
        new Thread() {
            @Override
            public void run() {
                ReturnDomain returnDomain = new ReturnDomain();
                List<NameValuePair> pairs = new ArrayList<>();
                try {
                    returnDomain = (ReturnDomain) (new Http().post("mqtt/getServer.do", pairs, ReturnDomain.class));
                    if (returnDomain.getSuccess()) {
                        Map map = (Map) returnDomain.getObject();
                        url = JSONArray.parseObject(map.get("broker").toString(), String[].class);
                        user = String.valueOf(map.get("user"));
                        password = String.valueOf(map.get("password"));
                        connect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getServer();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!hold) reconnect();
                    try {
                        sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static class Callback implements MqttCallback {
        @Override
        public void connectionLost(Throwable throwable) {
            Log.e("error", throwable.getMessage());
            reconnect();
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            String mes = new String(mqttMessage.getPayload(), "utf-8");
            Log.e("mes",mes);
            new MqttNotification().notification(mes);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }
    }

    public static synchronized void connect() {
        try {
            String topic = new AppOption().getOption(AppOption.APP_OPTION_USER);
            mqttClient = new MqttClient(url[idx], topic, new MemoryPersistence());
            options.setUserName(user);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            options.setCleanSession(true);
            mqttClient.connect(options);
            mqttClient.subscribe(topic, 1);
            mqttClient.setCallback(new Callback());
            Log.e("connect", "success//" + url[idx]);
            hold = true;
        } catch (Exception e) {
            e.printStackTrace();
            hold = false;
        }
    }

    public static void reconnect() {
        if (idx < url.length - 1) {
            idx++;
        } else {
            idx = 0;
        }
        connect();
    }

    public static void disconnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
