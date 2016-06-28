package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.boiling.domain.PrescriptionDomain;
import net.andy.dispensing.domain.DispensingDetailDomain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/6/23.
 */
public class AlreadyDisDetailUI extends Activity{
    private LinearLayout alreadydisdetail_patientInfo_linearLayout;
    private GridView alreadydisdetail_gridView;
    private TextView alreadydisdetail_doctorName_textView;
    private TextView alreadydisdetail_info_textView;
    private TextView alreadydisdetail_deptName_textView;
    private TextView alreadydisdetail_patientName_textView;

//    private GridAdapter gridAdapter;
    private PrescriptionDomain pre;
    private DecimalFormat df1 = new DecimalFormat("#.##");
    private List<DispensingDetailDomain> dispensingDetailDomainList;
    private List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alreadydisdetail);
        alreadydisdetail_patientInfo_linearLayout= (LinearLayout) findViewById(R.id.alreadydisdetail_patientInfo_linearLayout);
        alreadydisdetail_gridView = (GridView) findViewById(R.id.alreadydisdetail_gridView);
        alreadydisdetail_doctorName_textView= (TextView) findViewById(R.id.alreadydisdetail_doctorName_textView);
        alreadydisdetail_info_textView= (TextView) findViewById(R.id.alreadydisdetail_info_textView);
        alreadydisdetail_deptName_textView= (TextView) findViewById(R.id.alreadydisdetail_deptName_textView);
        alreadydisdetail_patientName_textView= (TextView) findViewById(R.id.alreadydisdetail_patientName_textView);
        alreadydisdetail_patientInfo_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        Intent in=getIntent();
//        dispensingDetailDomainList= (List<DispensingDetailDomain>) in.getSerializableExtra("dises");
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
//    private class GridAdapter extends BaseAdapter {
//        private LayoutInflater inflater;
//
//        public GridAdapter(Context context) {
//            this.inflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            return historyData.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return i;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            final Dis dis;
//            if (view == null) {
//                view = inflater.inflate(R.layout.dis_historygriditem, viewGroup, false);
//                dis = new Dis();
//                dis.historyItem_linearLayout= (LinearLayout) view.findViewById(R.id.historyItem_linearLayout);
//                dis.historyItem_name_textView= (TextView) view.findViewById(R.id.historyItem_name_textView);
//                dis.historyItem_waring_imageView = (ImageView) view.findViewById(R.id.historyItem_waring_imageView);
//                dis.historyItem_num_textView = (TextView) view.findViewById(R.id.historyItem_num_textView);
//                view.setTag(dis);
//            } else {
//                dis = (Dis) view.getTag();
//            }
//            Map map = (Map) historyData.get(i);
//            Log.e("map", map.toString());
//            if("true".equals(map.get("ItemWaringView"))){
//                dis.historyItem_waring_imageView.setVisibility(View.VISIBLE);
//            }else{
//                dis.historyItem_waring_imageView.setVisibility(View.INVISIBLE);
//            }
//            if(map.get("ItemSpecialView").toString().length()>0){
//                dis.historyItem_linearLayout.setBackgroundColor(Color.parseColor("#D2691E"));
//                dis.historyItem_name_textView.setTextColor(Color.WHITE);
//                dis.historyItem_num_textView.setTextColor(Color.WHITE);
//                dis.historyItem_name_textView .setText(""+map.get("ItemNameView")+map.get("ItemSpecialView"));
//            }else {
//                dis.historyItem_linearLayout.setBackgroundColor(Color.WHITE);
//                dis.historyItem_name_textView.setTextColor(Color.BLACK);
//                dis.historyItem_num_textView.setTextColor(Color.BLACK);
//                dis.historyItem_name_textView.setText("" + map.get("ItemNameView"));
//            }
//            dis.historyItem_num_textView .setText(""+map.get("ItemNumView"));
//            return view;
//        }
//
//
//        private class Dis {
//            private LinearLayout historyItem_linearLayout;
//            private ImageView historyItem_waring_imageView;
//            private TextView historyItem_name_textView;
//            private TextView historyItem_num_textView;
//        }
//    }
}
