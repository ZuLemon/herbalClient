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
import net.andy.dispensing.ui.StationRuleUI;
import net.andy.dispensing.util.SpinnerItem;
import net.andy.dispensing.util.StationUtil;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/2/24.
 */
public class SettingUI extends Activity {
    private LinearLayout setting_station_linearLayout;
    private SwitchButton switchButton;
    private TextView setting_interval_textView;
    private TextView setting_station_textView;
    private EditText setting_interval_editText;
    private ButtonListener buttonListener;
    private Spinner setting_station_spinner;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        coolToast = new CoolToast(getBaseContext());
        setting_station_linearLayout = (LinearLayout) findViewById(R.id.setting_station_linearLayout);
        buttonListener = new ButtonListener();
//        setting_station_spinner= (Spinner) findViewById(R.id.setting_station_spinner);
        switchButton = (SwitchButton) findViewById(R.id.switchButton);
        setting_interval_textView = (TextView) findViewById(R.id.setting_interval_textView);
//        setting_station_textView= (TextView) findViewById(R.id.setting_station_textView);
        setting_interval_editText = (EditText) findViewById(R.id.setting_interval_editText);
//        setting_station_spinner.setOnItemSelectedListener(new StSpinnerListener());
        stationDomain = new StationDomain();
        init();
        setMonitor();
        replenishController();
//        settingThread(0);
    }

    private void init() {
        setting_interval_editText.setText("" + appOption.getOption(AppOption.APP_OPTION_WAITTIME));
        switchButton.setChecked(Boolean.parseBoolean(appOption.getOption(AppOption.APP_OPTION_HERSPEC)));
    }

    private void setMonitor() {
        setting_interval_textView.setOnClickListener(buttonListener);
        setting_station_linearLayout.setOnClickListener(buttonListener);
//        setting_station_textView.setOnClickListener(buttonListener);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    appOption.setOption(AppOption.APP_OPTION_HERSPEC, "true");
                    new CoolToast(getBaseContext()).show("打开");
                } else {
                    appOption.setOption(AppOption.APP_OPTION_HERSPEC, "false");
                    new CoolToast(getBaseContext()).show("关闭");
                }
            }
        });
    }

    //    private class StSpinnerListener implements AdapterView.OnItemSelectedListener{
//
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            selectedStationId = ((SpinnerItem)setting_station_spinner.getSelectedItem()).GetID();
//        }
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//        }
//    }
//    private void setStationView(){
//        if(isStation){
//            isStation=false;
//            setting_station_spinner.setClickable(false);
//            setting_station_textView.setText("修改");
//            stationDomain.setId(selectedStationId);
//            for(int i=0;i<stationAll.size();i++){
//                if(Integer.parseInt(String.valueOf(((Map)stationAll.get(i)).get("id")))==selectedStationId) {
//                    stationDomain.setRulesId(Integer.parseInt(String.valueOf(((Map)stationAll.get(i)).get("rulesId"))));
//                    stationDomain.setShelfId(Integer.parseInt(String.valueOf(((Map)stationAll.get(i)).get("shelfId"))));
//                    stationDomain.setName(String.valueOf(((Map) stationAll.get(i)).get("name")));
//                    stationDomain.setDevice(new AppOption().getOption(AppOption.APP_DEVICE_ID));
//                    settingThread(1);
//                }
//            }
//        }else{
//            isStation=true;
//            setting_station_spinner.setClickable(true);
//            setting_station_textView.setText("保存");
//        }
//    }
    private void setInterval() {
        if (isInterval) {
            isInterval = false;
            if (Integer.parseInt(setting_interval_editText.getText() + "") == 0) {
                setting_interval_editText.setText("1");
                coolToast.show("间隔时间至少为 1");
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

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.setting_interval_textView:
                    setInterval();
                    break;
                case R.id.setting_station_linearLayout:
                    Intent intent = new Intent(SettingUI.this, StationRuleUI.class);
                    startActivity(intent);
                    break;
//                case R.id.setting_station_textView:
//                    setStationView();
//                    break;
                default:
                    break;
            }
        }
    }
//    private void setStation(){
//        System.out.println(stationAll.size());
//        for(int i=0;i<stationAll.size();i++){
//            spinnerItem= new SpinnerItem (Integer.parseInt(String.valueOf(((Map)stationAll.get(i)).get("id"))),(String)((Map)stationAll.get(i)).get("name"));
//            stationNameList.add(spinnerItem);
//            System.out.println(spinnerItem.GetValue());
//        }
//        stationAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stationNameList);
//        //设置下拉列表的风格
//        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        setting_station_spinner.setAdapter(stationAdapter);
//        stationAdapter.notifyDataSetChanged();
//    }

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
                    case -1:
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
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private void replenishController() {
        ((CheckBox) findViewById(R.id.replenish)).setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReplenish(((CheckBox)view.findViewById(R.id.replenish)).isChecked() ?
                        "topic/insert.do" : "topic/delete.do", "上药");
            }
        });
        initReplenish();
    }

    private void initReplenish() {
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                List<NameValuePair> pairs = new ArrayList<>();
                pairs.add(new BasicNameValuePair("userId", appOption.getOption(AppOption.APP_OPTION_USER)));
                try {
                    ReturnDomain returnDomain = (ReturnDomain) (
                            new Http().post("topic/getTopicByUserId.do", pairs, ReturnDomain.class));
                    if (returnDomain.getSuccess()) {
                        List list = JSON.parseObject(returnDomain.getObject().toString(), List.class);
                        for (Object o : list) {
                            String s = (String) ((Map)o).get("topic");
                            Log.e("json",s);
                            if (s.equals("上药")) {
                                reset.sendEmptyMessage(1);
                            }
                        }
                    } else {
                        Toast.makeText(SettingUI.this, returnDomain.getException(), Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SettingUI.this, e.getMessage(), Toast.LENGTH_SHORT);
                }
                Looper.loop();
            }
        }.start();
    }

    Handler reset = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ((CheckBox) findViewById(R.id.replenish)).setChecked(false);
                    break;
                case 1:
                    ((CheckBox) findViewById(R.id.replenish)).setChecked(true);
                    break;
            }
        }
    };

    private void setReplenish(String uri, String topic) {
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                List<NameValuePair> pairs = new ArrayList<>();
                pairs.add(new BasicNameValuePair("userId", appOption.getOption(AppOption.APP_OPTION_USER)));
                pairs.add(new BasicNameValuePair("topic", topic));
                try {
                    ReturnDomain returnDomain = (ReturnDomain) (new Http().post(uri, pairs, ReturnDomain.class));
                } catch (Exception e) {
                    Toast.makeText(SettingUI.this, e.getMessage(), Toast.LENGTH_SHORT);
                }
                Looper.loop();
            }
        }.start();
    }
}
