package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.Application;
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
import net.andy.boiling.domain.TagDomain;
import net.andy.boiling.util.PrescriptionDetailUtil;
import net.andy.boiling.util.PrescriptionUtil;
import net.andy.boiling.util.TagUtil;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.DispensingDetailDomain;
import net.andy.dispensing.domain.DispensingDomain;
import net.andy.dispensing.util.DispensingUtil;
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
 *已调剂明细
 * Created by Guang on 2016/6/23.
 */
public class AlreadyDisDetailUI extends Activity {
    @ViewInject(R.id.alreadydisdetail_patientInfo_linearLayout)
    private LinearLayout alreadydisdetail_patientInfo_linearLayout;
    @ViewInject(R.id.alreadydisdetail_gridView)
    private GridView alreadydisdetail_gridView;
    @ViewInject(R.id.alreadydisdetail_doctorName_textView)
    private TextView alreadydisdetail_doctorName_textView;
    @ViewInject(R.id.alreadydisdetail_info_textView)
    private TextView alreadydisdetail_info_textView;
    @ViewInject(R.id.alreadydisdetail_deptName_textView)
    private TextView alreadydisdetail_deptName_textView;
    @ViewInject(R.id.alreadydisdetail_patientName_textView)
    private TextView alreadydisdetail_patientName_textView;
    @ViewInject(R.id.alreadydisdetail_patientNo_textView)
    private TextView alreadydisdetail_patientNo_textView;
    @ViewInject(R.id.alreadydisdetail_mainTag_textView)
    private TextView alreadydisdetail_mainTag_textView;
    private GridAdapter gridAdapter;
    private String presId = "";
    private List presDetailList = new ArrayList();
    private PrescriptionDomain pre;
    private TagDomain tagDomain;
    private DecimalFormat df1 = new DecimalFormat("#.##");
    private List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alreadydisdetail);
        x.view().inject(this);
        gridAdapter = new GridAdapter(this);
        alreadydisdetail_gridView.setAdapter(gridAdapter);
        Intent in = getIntent();
        presId = in.getStringExtra("presId");
        if ("".equals(presId)) {
            finish();
        }
        AlreadyDisThread(0);
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
                        alreadydisdetail_patientNo_textView.setText(pre.getPatientNo());
                        alreadydisdetail_patientName_textView.setText(pre.getPatientName());
                        alreadydisdetail_deptName_textView.setText(pre.getDeptName());
                        alreadydisdetail_doctorName_textView.setText(pre.getDoctorName());
                        if(tagDomain!=null) {
                            alreadydisdetail_mainTag_textView.setText(tagDomain.getColor()+tagDomain.getCode().replace("M",""));
                        }
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
                            DispensingDomain dispensingDomain=new DispensingUtil().getDispensingByPlanId(new AppOption().getOption(AppOption.APP_DEVICE_ID),pre.getPlanId());
                            if(dispensingDomain!=null&&!"".equals(dispensingDomain.getTagId())) {
                                tagDomain = new TagUtil().getTagByTagId(dispensingDomain.getTagId());
                            }
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
