package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.boiling.domain.PrescriptionDomain;
import net.andy.boiling.util.PrescriptionDetailUtil;
import net.andy.boiling.util.PrescriptionUtil;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.DispensingDetailDomain;
import org.xutils.ViewInjector;
import org.xutils.view.ViewInjectorImpl;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/6/23.
 */
public class AlreadyDisDetailUI extends Activity {
    @ViewInject(R.id.alreadydisdetail_patientInfo_linearLayout)
    private LinearLayout alreadydisdetail_patientInfo_linearLayout;
    @ViewInject(R.id.alreadydisdetail_gridView)
    private GridView alreadydisdetail_gridView;
    private GridAdapter gridAdapter;
    private TextView alreadydisdetail_doctorName_textView;
    private TextView alreadydisdetail_info_textView;
    private TextView alreadydisdetail_deptName_textView;
    private TextView alreadydisdetail_patientName_textView;
    private String presId = "";
    private List presDetailList = new ArrayList();
    //    private GridAdapter gridAdapter;
    private PrescriptionDomain pre;
    private DecimalFormat df1 = new DecimalFormat("#.##");
    private List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alreadydisdetail);
        x.view().inject(this);
//        alreadydisdetail_patientInfo_linearLayout= (LinearLayout) findViewById(R.id.alreadydisdetail_patientInfo_linearLayout);
        gridAdapter = new GridAdapter(this);
        alreadydisdetail_gridView.setAdapter(gridAdapter);
        alreadydisdetail_doctorName_textView = (TextView) findViewById(R.id.alreadydisdetail_doctorName_textView);
        alreadydisdetail_info_textView = (TextView) findViewById(R.id.alreadydisdetail_info_textView);
        alreadydisdetail_deptName_textView = (TextView) findViewById(R.id.alreadydisdetail_deptName_textView);
        alreadydisdetail_patientName_textView = (TextView) findViewById(R.id.alreadydisdetail_patientName_textView);
//        alreadydisdetail_patientInfo_linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        Intent in = getIntent();
        presId = in.getStringExtra("presId");
        if ("".equals(presId)) {
            finish();
        }
        AlreadyDisThread(0);
//        historyPre();
//        pre= (PrescriptionDomain) in.getSerializableExtra("pre");
//        disHistory_info_textView.setText(pre.getClassification()+" "+pre.getPresNumber()+"付 "+pre.getWay()+pre.getManufacture()+pre.getFrequency());
//        disHistory_patientName_textView.setText(pre.getPatientName());
//        disHistory_doctorName_textView.setText(pre.getDoctorName());
//        disHistory_deptName_textView.setText(pre.getDeptName());
////        disHistory_stationName_textView.setText(new AppOption().getOption(AppOption.APP_OPTION_STATION));
//        gridAdapter = new GridAdapter(this);
//        dis_history_gridView.setAdapter(gridAdapter);
////        simpleAdapter = new SimpleAdapter(this,
////                historyData, R.layout.dis_historygriditem, new String[]{"ItemNameView", "ItemNumView"}, new int[]{R.id.adjust_name_textView, R.id.adjust_num_textView});
////        adjust_history_gridView.setAdapter(simpleAdapter);
//        historyPre();
    }

    @Event(value = R.id.alreadydisdetail_patientInfo_linearLayout, type = View.OnClickListener.class)
    private void btnClick(View view) {
        finish();
    }

    private void AlreadyDisThread(int what) {
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
                        alreadydisdetail_info_textView.setText(pre.getCategory() + " " + pre.getPresNumber() + "付" + pre.getClassification() + pre.getManufacture() + pre.getFrequency());
                        alreadydisdetail_patientName_textView.setText(pre.getPatientName());
                        alreadydisdetail_deptName_textView.setText(pre.getDeptName());
                        alreadydisdetail_doctorName_textView.setText(pre.getDoctorName());
                        gridAdapter.notifyDataSetChanged();
//                        new CoolToast( getBaseContext () ).show ( ((Map)statusMap.get("main")).get("status").toString());
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            message.what = 0;
                            pre = new PrescriptionUtil().getPrescriptionByPresId(presId);
                            presDetailList = new PrescriptionDetailUtil().getPrescriptionDetailByPresId(presId);
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

    //    private void historyPre() {
//        boolean isEnd=true;
//        HashMap<String, Object> map;
//        int count=dispensingDetailDomainList.size();
//        int m=0;
//        if(count%2==0){m=count/2;}else{m=count/2+1;}
//        for (int i=count-1; i >=0 ; i--) {
//            map = new HashMap<String, Object>();
//            map.put("ItemNameView", dispensingDetailDomainList.get(i).getHerbName());
//            map.put("ItemNumView", (df1.format(dispensingDetailDomainList.get(i).getQuantity())) +dispensingDetailDomainList.get(i).getHerbUnit());
//            map.put("ItemWaringView", (dispensingDetailDomainList.get(i).getWarning()));
//            map.put("ItemSpecialView", (dispensingDetailDomainList.get(i).getSpecial()));
//            historyData.add(map);
//            gridAdapter.notifyDataSetChanged();
//            if((i-m)<0){break;}
//                map = new HashMap<String, Object>();
//                map.put("ItemNameView", dispensingDetailDomainList.get((i - m)).getHerbName());
//                map.put("ItemNumView", (df1.format(dispensingDetailDomainList.get((i - m)).getQuantity())) + dispensingDetailDomainList.get((i - m)).getHerbUnit());
//                map.put("ItemWaringView", (dispensingDetailDomainList.get(i - m).getWarning()));
//                map.put("ItemSpecialView", (dispensingDetailDomainList.get(i - m).getSpecial()));
//            historyData.add(map);
//            gridAdapter.notifyDataSetChanged();
//                if((i-m)==0&&count%2==0){break;}
////            }
//
//        }
//
//    }
    private class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return presDetailList.size();
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
            final Pres pres;
            if (view == null) {
                view = inflater.inflate(R.layout.dis_historygriditem, viewGroup, false);
                pres = new Pres();
                pres.historyItem_linearLayout = (LinearLayout) view.findViewById(R.id.historyItem_linearLayout);
                pres.historyItem_name_textView = (TextView) view.findViewById(R.id.historyItem_name_textView);
                pres.historyItem_waring_imageView = (ImageView) view.findViewById(R.id.historyItem_waring_imageView);
                pres.historyItem_num_textView = (TextView) view.findViewById(R.id.historyItem_num_textView);
                view.setTag(pres);
            } else {
                pres = (Pres) view.getTag();
            }
            Map map = (Map) presDetailList.get(i);
            Log.e("map", map.toString());
            if ("true".equals(map.get("warning"))) {
                pres.historyItem_waring_imageView.setVisibility(View.VISIBLE);
            } else {
                pres.historyItem_waring_imageView.setVisibility(View.INVISIBLE);
            }
            if (map.get("special").toString().length() > 0) {
                pres.historyItem_linearLayout.setBackgroundColor(Color.parseColor("#D2691E"));
                pres.historyItem_name_textView.setTextColor(Color.WHITE);
                pres.historyItem_num_textView.setTextColor(Color.WHITE);
                pres.historyItem_name_textView.setText("" + map.get("herbName") + map.get("special"));
            } else {
                pres.historyItem_linearLayout.setBackgroundColor(Color.WHITE);
                pres.historyItem_name_textView.setTextColor(Color.BLACK);
                pres.historyItem_num_textView.setTextColor(Color.BLACK);
                pres.historyItem_name_textView.setText("" + map.get("herbName"));
            }
            pres.historyItem_num_textView.setText("" + df1.format(map.get("quantity")) + map.get("herbUnit"));
            return view;
        }


        private class Pres {
            private LinearLayout historyItem_linearLayout;
            private ImageView historyItem_waring_imageView;
            private TextView historyItem_name_textView;
            private TextView historyItem_num_textView;
        }
    }
}
