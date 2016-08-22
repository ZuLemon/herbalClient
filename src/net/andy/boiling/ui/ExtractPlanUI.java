package net.andy.boiling.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.boiling.domain.SolutionDomain;
import net.andy.boiling.util.SolutionUtil;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.PrescriptionStatusUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 煎制方案选择
 * User: Guang
 * Date: 2016/8/16
 */
@ContentView(R.layout.extractplan)
public class ExtractPlanUI extends Activity {
    protected final int RESULT_CODE=1003;
    protected final int REQUEST_CODE=1004;
    @ViewInject(R.id.extractPlan_listView)
    private ListView extractPlan_listView;
    @ViewInject(R.id.extractPlan_linearLayout)
    private LinearLayout extractPlan_linearLayout;
    //    @ViewInject(R.id.extractPlan_type_radioGroup)
//    private RadioGroup extractPlan_type_radioGroup;
//    @ViewInject(R.id.extractPlan_one_radioButton)
//    private RadioButton extractPlan_one_radioButton;
//    @ViewInject(R.id.extractPlan_two_radioButton)
//    private RadioButton extractPlan_two_radioButton;
    private SolutionAdapter solutionAdapter;
    private int cur_pos = 0;// 当前显示的一行
    private Drawable ltgraydrawable;
    private Drawable whitedrawable;
    private List<SolutionDomain> solutionDomainList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        solutionAdapter = new SolutionAdapter(this);
//        extractPlan_one_radioButton.setChecked(true);
        ltgraydrawable = getResources().getDrawable(R.drawable.ltgray_circular);
        whitedrawable = getResources().getDrawable(R.drawable.white_circular);
        whitedrawable.setBounds(0, 0, whitedrawable.getMinimumWidth(), whitedrawable.getMinimumHeight());
        ltgraydrawable.setBounds(0, 0, ltgraydrawable.getMinimumWidth(), ltgraydrawable.getMinimumHeight());
        extractPlan_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ExtractPlanThread(0);
    }
    @Event(value = R.id.extractPlan_linearLayout,type = View.OnClickListener.class)
    private void onClick(View v){
        finish();
    }

    @Event(value = R.id.extractPlan_listView, type = AdapterView.OnItemClickListener.class)
    private void ItemClick(AdapterView<?> arg0, View arg1,
                           int position, long id) {
        Log.e(">>", "点击");
        cur_pos = position;
//        solutionAdapter.notifyDataSetChanged();
        Intent intent=new Intent();
        intent.putExtra("solution", (Serializable) solutionDomainList.get(position));
        setResult(RESULT_OK,intent);
        finish();
    }
    private void parseObject(List<Map> list){
        solutionDomainList=new ArrayList<>();
        SolutionDomain tempSolution;
        for(Map map : list){
            tempSolution=new SolutionDomain();
            tempSolution.setId(Integer.parseInt(String.valueOf(map.get("id"))));
            tempSolution.setName(String.valueOf(map.get("name")));
            tempSolution.setSoakTime(Integer.parseInt(String.valueOf(map.get("soakTime"))));
            tempSolution.setExtractTime1(Integer.parseInt(String.valueOf(map.get("extractTime1"))));
            tempSolution.setExtractTime2(Integer.parseInt(String.valueOf(map.get("extractTime2"))));
            tempSolution.setExtractTime3(Integer.parseInt(String.valueOf(map.get("extractTime3"))));
            tempSolution.setTemperature(Integer.parseInt(String.valueOf(map.get("temperature"))));
            tempSolution.setPressure(String.valueOf(map.get("pressure")));
            tempSolution.setClassification(String.valueOf(map.get("classification")));
            tempSolution.setEfficacy(String.valueOf(map.get("efficacy")));
            tempSolution.setMode(String.valueOf(map.get("mode")));
            tempSolution.setStatus(String.valueOf(map.get("status")));
            solutionDomainList.add(tempSolution);
        }
    }
    final Message message = new Message();
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case - 1:

                    break;
                case 0:
                    parseObject((List<Map>) msg.obj);
                    extractPlan_listView.setAdapter(solutionAdapter);
//                    solutionAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void ExtractPlanThread(int what) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            message.obj= new SolutionUtil().getSolutionForAll("正常");
                            message.what = 0;
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

    public class SolutionAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public SolutionAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return solutionDomainList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final SolutionItem solutionItem;

            if (view == null) {
                view = inflater.inflate(R.layout.extractsolutionitem, viewGroup, false);
                solutionItem = new SolutionItem();
                solutionItem.solutionItem_id = (TextView) view.findViewById(R.id.solutionItem_id);
                solutionItem.solutionItem_name = (TextView) view.findViewById(R.id.solutionItem_name);
                solutionItem.solutionItem_mode = (TextView) view.findViewById(R.id.solutionItem_mode);
                solutionItem.solutionItem_efficacy = (TextView) view.findViewById(R.id.solutionItem_efficacy);
                solutionItem.solutionItem_soakTime = (TextView) view.findViewById(R.id.solutionItem_soakTime);
                solutionItem.solutionItem_extractTime1 = (TextView) view.findViewById(R.id.solutionItem_extractTime1);
                solutionItem.solutionItem_extractTime2 = (TextView) view.findViewById(R.id.solutionItem_extractTime2);
                solutionItem.solutionItem_extractTime3 = (TextView) view.findViewById(R.id.solutionItem_extractTime3);
                solutionItem.solutionItem_count = (TextView) view.findViewById(R.id.solutionItem_count);
                solutionItem.solutionItem_linearLayout = (LinearLayout) view.findViewById(R.id.solutionItem_linearLayout);
                view.setTag(solutionItem);
            } else {
                solutionItem = (SolutionItem) view.getTag();
            }
            SolutionDomain temp = solutionDomainList.get(i);
            solutionItem.solutionItem_id.setText(String.valueOf(temp.getId()));
            solutionItem.solutionItem_name.setText(temp.getName());
            solutionItem.solutionItem_mode.setText(temp.getMode());
            solutionItem.solutionItem_efficacy.setText(temp.getEfficacy()==null?"    ":temp.getEfficacy());
            solutionItem.solutionItem_soakTime.setText(temp.getSoakTime()==null?"    ":(temp.getSoakTime() + "分"));
            solutionItem.solutionItem_extractTime1.setText(temp.getExtractTime1()==null?"    ":(temp.getExtractTime1() + "分"));
            solutionItem.solutionItem_extractTime2.setText(temp.getExtractTime2()==null?"    ":(temp.getExtractTime2() + "分"));
            solutionItem.solutionItem_extractTime3.setText(temp.getExtractTime3()==null?"    ":(temp.getExtractTime3() + "分"));
            solutionItem.solutionItem_count.setText(i + 1 + "");
            if (i == cur_pos) {// 如果当前的行就是ListView中选中的一行，就更改显示样式
                solutionItem.solutionItem_linearLayout.setBackground(ltgraydrawable);// 更改整行的背景色
            } else {
                solutionItem.solutionItem_linearLayout.setBackground(whitedrawable);// 更改整行的背景色
            }
            return view;
        }

        private class SolutionItem {
            private TextView solutionItem_count;
            private TextView solutionItem_name;
            private TextView solutionItem_mode;
            private TextView solutionItem_efficacy;
            private TextView solutionItem_soakTime;
            private TextView solutionItem_extractTime1;
            private TextView solutionItem_extractTime2;
            private TextView solutionItem_extractTime3;
            private TextView solutionItem_id;
            private LinearLayout solutionItem_linearLayout;
        }
    }
}
