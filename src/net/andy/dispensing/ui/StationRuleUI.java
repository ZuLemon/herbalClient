package net.andy.dispensing.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import net.andy.dispensing.domain.RulesDomain;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.util.RuleUtil;
import net.andy.dispensing.util.ShelfUtil;
import net.andy.dispensing.util.SpinnerItem;
import net.andy.dispensing.util.StationUtil;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.com.NFCActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/3/9.
 */

public class StationRuleUI extends Activity {
    private List<SpinnerItem> stationNameList = new ArrayList<SpinnerItem>();
    private List<SpinnerItem> rlueNameList = new ArrayList<SpinnerItem>();
    private List<SpinnerItem> shelfNameList = new ArrayList<SpinnerItem>();
    private ArrayAdapter<SpinnerItem> shelfAdapter;
    private ArrayAdapter<SpinnerItem> stationAdapter;
    private ArrayAdapter<SpinnerItem> rlueAdapter;
    private EditText stationrule_stationName_editText;
    private EditText stationrule_driver_editText;
    private Spinner stationrule_rule_spinner;
    private Spinner stationrule_shelf_spinner;
    private StationDomain stationDomain;
    private StationUtil stationUtil = new StationUtil();
    private RuleUtil ruleUtil = new RuleUtil();
    private List<RulesDomain> rulesDomainList;
    private RulesDomain rulesDomain;
    private SpinnerItem spinnerItem;
    private TextView stationrule_description_textView;
    private TextView stationrule_type_textView;
    private List stationAll;
    private List ruleAll;
    private List shelfAll;
    private Integer selectedStationId;
    private Integer selectedRuleId;
    private Integer selectedShelfId;
    private Button stationrule_submit_button;
    private AppOption appOption = new AppOption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stationrule);
        stationrule_stationName_editText = (EditText) findViewById(R.id.stationrule_stationName_editText);
        stationrule_driver_editText = (EditText) findViewById(R.id.stationrule_driver_editText);
        stationrule_rule_spinner = (Spinner) findViewById(R.id.stationrule_rule_spinner);
        stationrule_shelf_spinner = (Spinner) findViewById(R.id.stationrule_shelf_spinner);
        stationrule_description_textView = (TextView) findViewById(R.id.stationrule_description_textView);
        stationrule_type_textView = (TextView) findViewById(R.id.stationrule_type_textView);
        stationrule_submit_button = (Button) findViewById(R.id.stationrule_submit_button);
        stationrule_driver_editText.setText(appOption.getOption(AppOption.APP_DEVICE_ID));
        rlueAdapter = new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item, rlueNameList);
        //设置下拉列表的风格
        rlueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shelfAdapter = new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item, shelfNameList);
        shelfAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stationrule_shelf_spinner.setAdapter(shelfAdapter);
        stationrule_rule_spinner.setAdapter(rlueAdapter);
        stationrule_rule_spinner.setOnItemSelectedListener(new RuSpinnerListener());
        stationrule_shelf_spinner.setOnItemSelectedListener(new ShSpinnerListener());
        stationrule_submit_button.setOnClickListener(new Submit());
//        StationThread(1);
        StationThread(0);
    }

    private class Submit implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            stationDomain.setId(selectedStationId);
//            for(int i=0;i<stationAll.size();i++){
//                if(Integer.parseInt(String.valueOf(((Map)stationAll.get(i)).get("id")))==selectedStationId) {
//                    stationDomain.setName(String.valueOf(((Map) stationAll.get(i)).get("name")));
//                    stationDomain.setDevice(String.valueOf(((Map) stationAll.get(i)).get("device")));
//                }
//            }
            if(stationrule_stationName_editText.getText().toString().length()==0){
                new CoolToast(getBaseContext()).show("工位名称不能为空");
                return;
            }
            if (stationDomain.getId() != null) {

                stationDomain.setName("" + stationrule_stationName_editText.getText());
                stationDomain.setRulesId(selectedRuleId);
                stationDomain.setShelfId(selectedShelfId);
                StationThread(2);
            } else {
                stationDomain = new StationDomain();
                stationDomain.setName("" + stationrule_stationName_editText.getText());
                stationDomain.setDevice(appOption.getOption(AppOption.APP_DEVICE_ID));
                stationDomain.setRulesId(selectedRuleId);
                stationDomain.setShelfId(selectedShelfId);
                StationThread(3);
            }
        }
    }

    private class ShSpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectedShelfId = ((SpinnerItem) stationrule_shelf_spinner.getSelectedItem()).GetID();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class RuSpinnerListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            for (RulesDomain ru : rulesDomainList) {
                if (((SpinnerItem) stationrule_rule_spinner.getSelectedItem()).GetID() == ru.getId()) {
                    stationrule_type_textView.setText(ru.getType());
                    stationrule_description_textView.setText(ru.getDescription());
                    selectedRuleId = ((SpinnerItem) stationrule_rule_spinner.getSelectedItem()).GetID();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
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
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                    case 0:
                        new AppOption().setOption(AppOption.APP_OPTION_STATION, String.valueOf(msg.obj));
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
                    message.obj = new RuleUtil().getRules(st.getRulesId()).getName();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }
    private void setStation() {
        Log.e("df", String.valueOf(shelfAll));
        for (RulesDomain ru : rulesDomainList) {
            spinnerItem = new SpinnerItem(ru.getId(), ru.getName());
            rlueNameList.add(spinnerItem);
        }
        for (int i = 0; i < shelfAll.size(); i++) {
            spinnerItem = new SpinnerItem(Integer.parseInt(String.valueOf(((Map) shelfAll.get(i)).get("id"))), (String) ((Map) shelfAll.get(i)).get("name"));
            shelfNameList.add(spinnerItem);
        }
        rlueAdapter.notifyDataSetChanged();
        shelfAdapter.notifyDataSetChanged();
        setSpinnerItemSelectedByValue(stationrule_rule_spinner,stationDomain.getRulesId());
        setSpinnerItemSelectedByValue(stationrule_shelf_spinner,stationDomain.getShelfId());
    }

    //ListMap 转为 List<Bean>
    private void parseObject(List listRule) {
        rulesDomainList = new ArrayList<RulesDomain>();
        for (Object obj : listRule) {
            rulesDomain = new RulesDomain();
            rulesDomain.setId((Integer) ((Map) obj).get("id"));
            rulesDomain.setName((String) ((Map) obj).get("name"));
            rulesDomain.setRules((String) ((Map) obj).get("rules"));
            rulesDomain.setDescription((String) ((Map) obj).get("description"));
            rulesDomain.setType((String) ((Map) obj).get("type"));
            rulesDomainList.add(rulesDomain);
        }
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param id
     */

    public static void setSpinnerItemSelectedByValue(Spinner spinner, int id) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (id==((SpinnerItem)apsAdapter.getItem(i)).GetID()) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
//                        setStation();
                    new CoolToast(getBaseContext()).show("error:" + (String) msg.obj);
                    break;
                case 0:
//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    parseObject(ruleAll);
                    setStation();
                    stationrule_stationName_editText.setText(stationDomain.getName());
                    break;
                case 1:
//                    parseObject(ruleAll);
//                    setStation();
//                    stationrule_stationName_editText.setText(stationDomain.getName());
                    break;
                case 2:
                    new CoolToast(getBaseContext()).show((String) msg.obj);
                    StationThread(0);
                    StationThread();
                    break;
            }
        }
    };

    /**
     * 子线程
     **/
    private void StationThread(int what) {
        final Message message = new Message();
        new Thread() {
            @Override
            public void run() {
                try {
                    switch (what) {
                        //获取工位
                        case 0:
                            stationAll = stationUtil.getStationAll();
                            shelfAll = new ShelfUtil().getShelfList();
                            stationDomain = stationUtil.getStationByDevice();
                            ruleAll = ruleUtil.getRulesList();
                            message.what = 0;
                            handler.sendMessage(message);
                            break;
                        //获取规则
                        case 1:

//                            if(ruleAll!=null){
//                                message.what = 1;
//                                message.obj = ruleAll;
//                                handler.sendMessage ( message );
//                            }
                            break;
                        //更新工位信息
                        case 2:
                            message.obj = stationUtil.updateStation(stationDomain, 0);
                            message.what = 2;
                            handler.sendMessage(message);
                            break;
                        //添加工位信息
                        case 3:
                            message.obj = stationUtil.updateStation(stationDomain, 2);
                            message.what = 2;
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
}
