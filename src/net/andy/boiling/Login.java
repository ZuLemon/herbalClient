package net.andy.boiling;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import net.andy.dispensing.util.RuleUtil;
import net.andy.dispensing.util.StationUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登陆窗口
 */
public class Login extends Activity {
    private EditText userId;
    private EditText password;
    AppOption appOption = new AppOption();
    Message message = new Message();
    public static Login instance = null;
    private PendingIntent pendingIntent;
    String deviceid;

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
        // 检查软件更新
        manager.checkUpdate();
        setContentView(R.layout.login);
        userId = (EditText) this.findViewById(R.id.login_userId_editText);
        password = (EditText) this.findViewById(R.id.login_password_editText);
        Button option = (Button) this.findViewById(R.id.login_option_button);
        Button submit = (Button) this.findViewById(R.id.login_submit_button);
        if (appOption.getOption(AppOption.APP_OPTION_STATE).equals("YES")) {
            userId.setText(appOption.getOption(AppOption.APP_OPTION_USER));
            password.setText(appOption.getOption(AppOption.APP_OPTION_PASSWORD));
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
//        appOption.setOption(AppOption.APP_OPTION_WAITTIME,"3");
        if ("".equals(appOption.getOption(AppOption.APP_OPTION_WAITTIME)))
            appOption.setOption(AppOption.APP_OPTION_WAITTIME, "3");
//        appOption.setOption(AppOption.APP_OPTION_SERVER, "192.168.34.99");

        appOption.setOption(AppOption.APP_OPTION_STATE, "YES");
//        tag_code_editText.setText(deviceid);
        submit.setOnClickListener(new SubmitOnclick());
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

    public class SubmitOnclick implements Button.OnClickListener {
        @Override
        public void onClick(View v) {

            //管理员修改服务器地址
            if ("admin".equals(userId.getText().toString()) && "wlbgs".equals(password.getText().toString())) {
                userId.setText(appOption.getOption(AppOption.APP_OPTION_USER));
                password.setText(appOption.getOption(AppOption.APP_OPTION_PASSWORD));
                Intent intent = new Intent(Login.this, Option.class);
                startActivity(intent);
                return;
            }
            if (!isNumeric(userId.getText().toString())) {
                new CoolToast(getBaseContext()).show("用户编号错误");
                return;
            }
            if ("".equals(Application.getServerIP())) {
                new CoolToast(getBaseContext()).show("没有设置服务器地址,不能登陆");
                return;
            }
            //正在加载
            startActivity(new Intent(Login.this, LoadingUI.class));
            StationThread();
            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    new CoolToast(getBaseContext()).show((String) msg.obj);
                    LoadingUI.instance.finish();
                }
            };
            new Thread() {
                @Override
                public void run() {
                    try {
                        String info = new UserUtil().confirmPasswd(userId.getText().toString(), password.getText().toString());
                        if (info.equals("success")) {
                            appOption.setOption(AppOption.APP_OPTION_USER, userId.getText().toString());
                            appOption.setOption(AppOption.APP_OPTION_PASSWORD, password.getText().toString());
                            //获取登录用户详细信息
                            new LogUserInfo().setLogUsers();
                            //检查推送服务是否运行
                            checkService();
                            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                            if (wifiManager != null) {
                                deviceid = wifiManager.getConnectionInfo().getMacAddress();
                            }
                            appOption.setOption(AppOption.APP_DEVICE_ID, deviceid);
                            //结束当前
                            Login.this.finish();
                            LoadingUI.instance.finish();
                            Intent intent = new Intent(Login.this, Main.class);
                            startActivity(intent);
                        } else {
                            message.obj = info;
                            handler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        Log.e("错误",e.getMessage());
                        if(handler.obtainMessage(message.what, message.obj) != null){
                            Message _msg = new Message();
                            _msg.what = message.what;
                            _msg.obj= e.getMessage();
                            message = _msg;
                        }else{
                        message.obj = e.getMessage();
                        }
                        handler.sendMessage(message);
                    }
                }
            }.start();
        }
    }

    private void StationThread() {
        final Message message = new Message();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case -1:
                        new AppOption().setOption(AppOption.APP_OPTION_STATION, "未设置队列");
                        break;
                    case 0:
                        Application.setRulesDomain((RulesDomain) msg.obj);
                        new AppOption().setOption(AppOption.APP_OPTION_STATION, String.valueOf(((RulesDomain) msg.obj).getName()));
                        break;
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    StationDomain st = new StationUtil().getStationByDevice();
                    if (st == null) {
                        message.obj = null;
                        message.what = -1;
                    } else {
                        message.obj = new RuleUtil().getRules(st.getRulesId());
                        message.what = 0;
                    }
                    handler.sendMessage(message);
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    public class OptionOnClick implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Login.this, Option.class);
            startActivity(intent);
        }
    }

    public void checkService() {
        boolean isRun = false;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("net.andy.com.MqttService".equals(service.service.getClassName())) {
                isRun = true;
            }
        }
        if (isRun) {
            MqttService.disconnect();
            MqttService.connect();
        } else {
            //启动推送服务
            startService(new Intent(Application.getContext(), MqttService.class));
        }
    }
}
