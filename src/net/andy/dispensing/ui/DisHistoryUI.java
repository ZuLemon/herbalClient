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
import net.andy.com.AppOption;
import net.andy.dispensing.domain.DispensingDetailDomain;
import org.w3c.dom.Text;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回查
 * Created by Guang on 2016/3/18.
 */
public class DisHistoryUI extends Activity{
    @ViewInject(R.id.adjust_history_linearLayout)
    private LinearLayout adjust_history_linearLayout;
    @ViewInject(R.id.dis_history_gridView)
    private GridView dis_history_gridView;
    @ViewInject(R.id.disHistory_info_textView)
    private TextView disHistory_info_textView;
    @ViewInject(R.id.disHistory_doctorName_textView)
    private TextView disHistory_doctorName_textView;
    @ViewInject(R.id.disHistory_category_textView)
    private TextView disHistory_category_textView;
    @ViewInject(R.id.disHistory_patientId_textView)
    private TextView disHistory_patientId_textView;
    @ViewInject(R.id.disHistory_patientName_textView)
    private TextView disHistory_patientName_textView;
    @ViewInject(R.id.disHistory_deptName_textView)
    private TextView disHistory_deptName_textView;

    private GridAdapter gridAdapter;
    private PrescriptionDomain pre;
    private DecimalFormat df1 = new DecimalFormat("#.##");
    private List<DispensingDetailDomain> dispensingDetailDomainList;
    private List<HashMap<String, Object>> historyData = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dis_history);
        x.view().inject(this);
        Intent in=getIntent();
        dispensingDetailDomainList= (List<DispensingDetailDomain>) in.getSerializableExtra("dises");
        pre= (PrescriptionDomain) in.getSerializableExtra("pre");
        disHistory_info_textView.setText(pre.getClassification()+" "+pre.getPresNumber()+"付 "+pre.getWay()+pre.getManufacture()+pre.getFrequency());
        disHistory_category_textView.setText(pre.getCategory()+"：");
        disHistory_patientId_textView.setText(pre.getPatientNo());
        disHistory_patientName_textView.setText(pre.getPatientName());
        disHistory_doctorName_textView.setText(pre.getDoctorName());
        disHistory_deptName_textView.setText(pre.getDeptName());
        gridAdapter = new GridAdapter(this);
        dis_history_gridView.setAdapter(gridAdapter);
        historyPre();
    }
    @Event(value = R.id.adjust_history_linearLayout)
    private void btnClick(View view) {
        finish();
    }
    private void historyPre() {
        boolean isEnd=true;
        HashMap<String, Object> map;
        int count=dispensingDetailDomainList.size();
        int m=0;
        if(count%2==0){m=count/2;}else{m=count/2+1;}
        for (int i=count-1; i >=0 ; i--) {
            map = new HashMap<String, Object>();
            map.put("ItemNameView", dispensingDetailDomainList.get(i).getHerbName());
            map.put("ItemNumView", (df1.format(dispensingDetailDomainList.get(i).getQuantity())) +dispensingDetailDomainList.get(i).getHerbUnit());
            map.put("ItemWaringView", (dispensingDetailDomainList.get(i).getWarning()));
            map.put("ItemSpecialView", (dispensingDetailDomainList.get(i).getSpecial()));
            historyData.add(map);
            gridAdapter.notifyDataSetChanged();
            if((i-m)<0){break;}
                map = new HashMap<String, Object>();
                map.put("ItemNameView", dispensingDetailDomainList.get((i - m)).getHerbName());
                map.put("ItemNumView", (df1.format(dispensingDetailDomainList.get((i - m)).getQuantity())) + dispensingDetailDomainList.get((i - m)).getHerbUnit());
                map.put("ItemWaringView", (dispensingDetailDomainList.get(i - m).getWarning()));
                map.put("ItemSpecialView", (dispensingDetailDomainList.get(i - m).getSpecial()));
            historyData.add(map);
            gridAdapter.notifyDataSetChanged();
                if((i-m)==0&&count%2==0){break;}
//            }

        }

    }
    private class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return historyData.size();
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
            final Dis dis;
            if (view == null) {
                view = inflater.inflate(R.layout.dis_historygriditem, viewGroup, false);
                dis = new Dis();
                dis.historyItem_linearLayout= (LinearLayout) view.findViewById(R.id.historyItem_linearLayout);
                dis.historyItem_name_textView= (TextView) view.findViewById(R.id.historyItem_name_textView);
                dis.historyItem_waring_imageView = (ImageView) view.findViewById(R.id.historyItem_waring_imageView);
                dis.historyItem_num_textView = (TextView) view.findViewById(R.id.historyItem_num_textView);
                view.setTag(dis);
            } else {
                dis = (Dis) view.getTag();
            }
            Map map = (Map) historyData.get(i);
            Log.e("map", map.toString());
            if("true".equals(map.get("ItemWaringView"))){
                dis.historyItem_waring_imageView.setVisibility(View.VISIBLE);
            }else{
                dis.historyItem_waring_imageView.setVisibility(View.INVISIBLE);
            }
            if(map.get("ItemSpecialView").toString().length()>0){
                dis.historyItem_linearLayout.setBackgroundColor(Color.parseColor("#D2691E"));
                dis.historyItem_name_textView.setTextColor(Color.WHITE);
                dis.historyItem_num_textView.setTextColor(Color.WHITE);
                dis.historyItem_name_textView .setText(""+map.get("ItemNameView")+map.get("ItemSpecialView"));
            }else {
                dis.historyItem_linearLayout.setBackgroundColor(Color.WHITE);
                dis.historyItem_name_textView.setTextColor(Color.BLACK);
                dis.historyItem_num_textView.setTextColor(Color.BLACK);
                dis.historyItem_name_textView.setText("" + map.get("ItemNameView"));
            }
            dis.historyItem_num_textView .setText(""+map.get("ItemNumView"));
            return view;
        }


        private class Dis {
            private LinearLayout historyItem_linearLayout;
            private ImageView historyItem_waring_imageView;
            private TextView historyItem_name_textView;
            private TextView historyItem_num_textView;
        }
    }
}
