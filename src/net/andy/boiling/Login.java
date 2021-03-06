package net.andy.boiling;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import net.andy.boiling.ui.UpdateUI;
import net.andy.boiling.util.UserUtil;
import net.andy.com.*;
import net.andy.dispensing.domain.RulesDomain;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.ui.LoadingUI;
import net.andy.dispensing.util.DeviceUtil;
import net.andy.dispensing.util.HerbalUtil;
import net.andy.dispensing.util.RuleUtil;
import net.andy.dispensing.util.StationUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Build.MANUFACTURER;

/**
 * 登陆窗口
 */
public class Login extends Activity {
    @ViewInject(R.id.login_userId_editText)
    private EditText login_userId_editText;
    @ViewInject(R.id.login_password_editText)
    private EditText login_password_editText;
    @ViewInject(R.id.login_option_button)
    private  Button login_option_button;
    @ViewInject(R.id.login_submit_button)
    private  Button login_submit_button;
    AppOption appOption = new AppOption();
    Message message = new Message();
    public static Login instance = null;
    private PendingIntent pendingIntent;
    private String imei;
    private String meid;
    private String mac;
    private String model;
    private String loginIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        UpdateUI manager = new UpdateUI(Login.this);
        String serverIP=appOption.getOption(AppOption.APP_OPTION_SERVER);
        //默认地址
        if ("".equals(serverIP)||serverIP==null) {
            appOption.setOption(AppOption.APP_OPTION_SERVER, "192.168.34.99:8888");
        }else if(!serverIP.matches(".*:.*")){
            appOption.setOption(AppOption.APP_OPTION_SERVER, serverIP+":8888");
        }
        Application.setServerIP(appOption.getOption(AppOption.APP_OPTION_SERVER));
        Log.e("serverIP",""+Application.getServerIP());
        // 检查软件更新
        manager.checkUpdate();
        setContentView(R.layout.login);
        x.view().inject(this);
        if (appOption.getOption(AppOption.APP_OPTION_STATE).equals("YES")) {
            login_userId_editText.setText(appOption.getOption(AppOption.APP_OPTION_USER));
            login_password_editText.setText(appOption.getOption(AppOption.APP_OPTION_PASSWORD));
        }
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        deviceid= tm.getDeviceId();
//        if(deviceid == null || deviceid.length()==0){
//        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        if (wifiManager != null) {
//            deviceid = wifiManager.getConnectionInfo().getMacAddress();
//        }
//        System.out.println(">>" + deviceid);
////        }
//        //设备号
//        appOption.setOption(AppOption.APP_DEVICE_ID, deviceid);
//        appOption.setOption(AppOption.APP_OPTION_WAITTIME,"3");2
        if ("".equals(appOption.getOption(AppOption.APP_OPTION_WAITTIME)))
            appOption.setOption(AppOption.APP_OPTION_WAITTIME, "3");
//        appOption.setOption(AppOption.APP_OPTION_SERVER, "192.168.34.99");

        appOption.setOption(AppOption.APP_OPTION_STATE, "YES");
//        tag_code_editText.setText(deviceid);
        //设置
//        option.setOnClickListener(new OptionOnClick());
//        try {
//            nfc = new NFC(this);
//        } catch (Exception e) {
//            new CoolToast(getBaseContext()).show(e.getMessage());
//            finish();
//            return;
//        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    @Event(value = R.id.login_submit_button)
   private void onClick(View v) {
            String userId=login_userId_editText.getText().toString().trim();
             String password=login_password_editText.getText().toString().trim();
            if("".equals(userId) || "".equals(password)){
                new CoolToast(getBaseContext()).show("用户编号和密码不允许为空");
                return;
            }
            //管理员修改服务器地址
            if ("admin".equals(userId) && "wlbgs".equals(password)) {
                login_userId_editText.setText(appOption.getOption(AppOption.APP_OPTION_USER));
                login_password_editText.setText(appOption.getOption(AppOption.APP_OPTION_PASSWORD));
                Intent intent = new Intent(Login.this, Option.class);
                startActivity(intent);
                return;
            }
            if (!isNumeric(userId)) {
                new CoolToast(getBaseContext()).show("用户编号错误");
                return;
            }
            if ("".equals(Application.getServerIP())) {
                new CoolToast(getBaseContext()).show("没有设置服务器地址,不能登陆");
                return;
            }
            //正在加载
            startActivity(new Intent(Login.this, LoadingUI.class));
            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case -1:
                            LoadingUI.instance.finish();
                            new CoolToast(getBaseContext()).show((String) msg.obj);
                            break;
                        case 0:
                            LoadingUI.instance.finish();
                            Intent intent = new Intent(Login.this, Main.class);
                            startActivity(intent);
                            getDeviceInfo();
                            LoginThread(0);
                            //设置间隔时间
                            appOption.setOption(AppOption.APP_OPTION_WAITTIME, "2");
                            //检查推送服务是否运行
                            break;
                    }

                }
            };
            new Thread() {
                @Override
                public void run() {
                    try {
                        String info = new UserUtil().confirmPasswd(userId, password);
                        if (info.equals("success")) {
                            appOption.setOption(AppOption.APP_OPTION_USER, userId);
                            appOption.setOption(AppOption.APP_OPTION_PASSWORD, password);
                            //获取登录用户详细信息
                            new LogUserInfo().setLogUsers();
                            message.what=0;
                            handler.sendMessage(message);
                        } else {
                            message.what=-1;
                            message.obj = info;
                            handler.sendMessage(message);
                        }
                    } catch (Exception e) {
//                        Log.e("错误",e.getMessage());
                        if(handler.obtainMessage(message.what, message.obj) != null){
                            Message _msg = new Message();
                            _msg.what = message.what;
                            _msg.obj= e.getMessage();
                            message = _msg;
                        }else{
                        message.obj = e.getMessage();
                        }
                        message.what=-1;
//                        message.obj = e.getMessage();
                        handler.sendMessage(message);
                    }
                }
            }.start();
    }
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    new AppOption().setOption(AppOption.APP_OPTION_STATION, "");
//                    finishExt();
                    Login.this.finish();
                    break;
                case 0:
                    Application.setRulesDomain((RulesDomain) msg.obj);
                    new AppOption().setOption(AppOption.APP_OPTION_STATION, String.valueOf(((RulesDomain) msg.obj).getName()));
//                    finishExt();
                    Login.this.finish();
                    break;
                case 1:
                    Login.this.finish();
                    break;
            }
        }
    };
    private void finishExt(){
        //设置间隔时间
        appOption.setOption(AppOption.APP_OPTION_WAITTIME, "2");
        //检查推送服务是否运行

        //结束当前
        LoginThread(1);
    }
    private void getDeviceInfo() {
        TelephonyManager mTm = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            mac = wifiManager.getConnectionInfo().getMacAddress();
            loginIP=HerbalUtil.intToIp(wifiManager.getConnectionInfo().getIpAddress());
            appOption.setOption(AppOption.APP_DEVICE_ID, mac);
        }
        imei = mTm.getDeviceId();
        meid = mTm.getSubscriberId();
        model = android.os.Build.MODEL; // 手机型号
        Log.i("Device:", "手机IMEI号："+imei+"手机IESI号："+meid+"手机型号："+model+"mac："+mac+"IP"+loginIP);
    }
    private void LoginThread(int way) {
        final Message message = new Message();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (way){
                        case 0:
                            StationDomain st = new StationUtil().getStationByDevice();
                            if (st == null) {
                                message.obj = null;
                                message.what = -1;
                            } else {
                                message.obj = new RuleUtil().getRules(st.getRulesId());
                                message.what = 0;
                            }
                            if(mac!=null) {
                                new DeviceUtil().insert(imei, meid, mac, model, loginIP);
                            }
                            handler.sendMessage(message);
                            break;
                        case 1:
                            if(mac!=null) {
                                new DeviceUtil().insert(imei, meid, mac, model, loginIP);
                            }
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                    }

                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }


//    public class OptionOnClick implements Button.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(Login.this, Option.class);
//            startActivity(intent);
//        }
//    }

//    public void checkService() {
////        new Thread() {
////            @Override
////            public void run() {
////                try {
//                    boolean isRun = false;
//                    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//                        if ("net.andy.com.MqttService".equals(service.service.getClassName())) {
//                            isRun = true;
//                        }
//                    }
//                    if (isRun) {
//                        Log.e(">>已经存在此服务","#########");
//                        MqttService.disconnect();
//                        MqttService.connect();
//                    } else {
//                        Log.e(">>不存在服务新建","#########");
//
////                        new Thread(){
////                            @Override
////                            public void run() {
////                                super.run();
//                                //启动推送服务
//                                startService(new Intent(Application.getContext(), MqttService.class));
////                            }
////                        }.start();
//
//                    }
////                } catch (Exception ex) {
////                    message.what = -1;
////                    message.obj = ex.getMessage();
////                    handler.sendMessage(message);
////                }
////            }
////        }.start();
//    }
}
