package net.andy.boiling.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.domain.TopicDomain;
import net.andy.dispensing.ui.AlreadyDisUI;
import net.andy.dispensing.ui.PersonalEffortUI;
import net.andy.dispensing.ui.StationRuleUI;
import net.andy.dispensing.ui.WaitDispenUI;
import net.andy.dispensing.util.SpinnerItem;
import net.andy.dispensing.util.StationUtil;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.TopicUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/2/24.
 */
@ContentView(R.layout.setting)
public class SettingUI extends Activity {
    @ViewInject(R.id.switchButton)
    private SwitchButton switchButton;
    @ViewInject(R.id.setting_interval_textView)
    private TextView setting_interval_textView;
    @ViewInject(R.id.setting_interval_editText)
    private EditText setting_interval_editText;
    private List stationNameList = new ArrayList<String>();
    private ArrayAdapter<String> stationAdapter;
    private SpinnerItem spinnerItem;
    private StationDomain stationDomain;
    private List stationAll;
    private StationUtil stationUtil = new StationUtil();
    private boolean isInterval;
    private boolean isStation;
    private Integer selectedStationId;
    private AppOption appOption = new AppOption();
    private CoolToast coolToast;
    private TopicUtil topicUtil;
    @ViewInject(R.id.topic_mianjian)
    private CheckBox topic_mianjian;
    @ViewInject(R.id.topic_yinpian)
    private CheckBox topic_yinpian;
    @ViewInject(R.id.topic_xiaobaozhuang)
    private CheckBox topic_xiaobaozhuang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        coolToast = new CoolToast(getBaseContext());
        stationDomain = new StationDomain();
        init();
        initReplenish();
//        setMonitor();
//        settingThread(0);
    }

    private void init() {
        setting_interval_editText.setText("" + appOption.getOption(AppOption.APP_OPTION_WAITTIME));
        switchButton.setChecked(Boolean.parseBoolean(appOption.getOption(AppOption.APP_OPTION_HERSPEC)));
        topicUtil = new TopicUtil();
    }

    @Event(value = R.id.switchButton, type = CompoundButton.OnCheckedChangeListener.class)
    private void CheckedChanged(CompoundButton arg0, boolean arg1) {
        if (arg1) {
            appOption.setOption(AppOption.APP_OPTION_HERSPEC, "true");
            new CoolToast(getBaseContext()).show("打开");
        } else {
            appOption.setOption(AppOption.APP_OPTION_HERSPEC, "false");
            new CoolToast(getBaseContext()).show("关闭");
        }
    }

    private void setInterval() {
        if (isInterval) {
            isInterval = false;
            if (Integer.parseInt(setting_interval_editText.getText() + "") < 2) {
                setting_interval_editText.setText("2");
                coolToast.show("间隔时间至少为 2");
            } else {
                coolToast.show("保存成功");
            }
            setting_interval_editText.setFocusable(false);
            appOption.setOption(AppOption.APP_OPTION_WAITTIME, setting_interval_editText.getText().toString());
            setting_interval_textView.setText("修改");
        } else {
            isInterval = true;
            setting_interval_editText.setFocusable(true);
            setting_interval_editText.setFocusableInTouchMode(true);
            setting_interval_editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            setting_interval_editText.setSelection(setting_interval_editText.getText().length());
            setting_interval_textView.setText("保存");
        }
    }

    private class settingOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    @Event(value = {R.id.setting_interval_textView,
            R.id.setting_station_linearLayout,
            R.id.setting_personaleffort_linearLayout,
//           R.id.setting_waitDispen_linearLayout,
            R.id.setting_alreadydis_linearLayout,
    }, type = View.OnClickListener.class)
    private void btnClick(View view) {
        switch (view.getId()) {
            case R.id.setting_interval_textView:
                setInterval();
                break;
            case R.id.setting_station_linearLayout:
                Intent intent = new Intent(SettingUI.this, StationRuleUI.class);
                startActivity(intent);
                break;
            case R.id.setting_personaleffort_linearLayout:
                Intent effortIntent = new Intent(SettingUI.this, PersonalEffortUI.class);
                startActivity(effortIntent);
                break;
//                case R.id.setting_waitDispen_linearLayout:
//                    Intent waitIntent = new Intent(SettingUI.this, WaitDispenUI.class);
//                    startActivity(waitIntent);
//                    break;
            case R.id.setting_alreadydis_linearLayout:
                Intent alreadyIntent = new Intent(SettingUI.this, AlreadyDisUI.class);
                startActivity(alreadyIntent);
                break;
            default:
                break;
        }
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param value
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    /**
     * 设置子线程
     **/
    private void settingThread(int what) {
        final Message message = new Message();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case - 1:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
//                        setStation();
                        break;
                    case 0:
//                       setStation();
//                       setSpinnerItemSelectedByValue(setting_station_spinner,stationDomain.getName());
                        break;
                    case 1:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    switch (what) {
                        //设置工位
                        case 0:
                            stationAll = stationUtil.getStationAll();
                            if (stationAll != null) {
                                stationDomain = stationUtil.getStationByDevice();
                                message.what = 0;
                                message.obj = stationAll;
                                handler.sendMessage(message);
                            }
                            break;
                        //更新工位信息
                        case 1:
                            message.obj = stationUtil.updateStation(stationDomain, 1);
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                    }
                } catch (Exception e) {
                    message.what = - 1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }
    @Event(value = {R.id.topic_mianjian,
                    R.id.topic_yinpian,
                    R.id.topic_xiaobaozhuang},type = View.OnClickListener.class)
    private void checkClick(View view) {
            CheckBox temp=(CheckBox) view;
            setReplenish(temp.isChecked(), String.valueOf(temp.getText()));
//            initReplenish();
    }

    private Message message;

    private void initReplenish() {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    message = new Message();
                    message.what = 0;
                    message.obj = topicUtil.getTopicByUserId();
                    handl.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SettingUI.this, e.getMessage(), Toast.LENGTH_SHORT);
                }
                Looper.loop();
            }
        }.start();
    }
    private void reset(){
        topic_mianjian.setChecked(false);
        topic_yinpian.setChecked(false);
        topic_xiaobaozhuang.setChecked(false);
    }
    Handler handl = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    reset();
                    for (Object o : (List) msg.obj) {
                        String tem=((Map) o).get("topic").toString();
                        Log.e("topic",tem);
                        if ("草药免煎".equals(tem)) {
                            topic_mianjian.setChecked(true);
                        }else if ("草药饮片".equals(tem)) {
                            topic_yinpian.setChecked(true);
                        }else if ("小包装".equals(tem)) {
                            topic_xiaobaozhuang.setChecked(true);
                        }
                    }
                    break;
                case 1:
                    coolToast.show(String.valueOf(msg.obj));
                    break;
            }
        }
    };

    private void setReplenish(boolean save, String topic) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    message = new Message();
                    if (save) {
                        message.obj = topicUtil.insert(topic);
                    } else {
                        message.obj = topicUtil.delete(topic);
                    }
                    message.what=1;
                    handl.sendMessage(message);
                } catch (Exception e) {
                    Toast.makeText(SettingUI.this, e.getMessage(), Toast.LENGTH_SHORT);
                }
                Looper.loop();
            }
        }.start();
    }
}
