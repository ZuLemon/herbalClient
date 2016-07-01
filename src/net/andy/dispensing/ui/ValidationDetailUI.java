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
import net.andy.boiling.util.PrescriptionUtil;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.DispensingDetailDomain;
import net.andy.dispensing.util.Arith;
import net.andy.dispensing.util.DispensingDetailUtil;
import net.andy.dispensing.util.ValidationUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/3/18.
 */
public class ValidationDetailUI extends Activity{
    private LinearLayout validationdetail_linearLayout;
    private GridView validationdetail_gridView;
    private TextView validationdetail_info_textView;
    private TextView validationdetail_doctorName_textView;
    private Button validationdetail_confirm_button;
    private Button validationdetail_return_button;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ButtonListener buttonListener=new ButtonListener();
    private List listDis;
    private boolean isReady=false;
    private TextView validationdetail_category_textView;
    private TextView validationdetail_patientId_textView;
    private TextView validationdetail_patientName_textView;
    private TextView validationdetail_deptName_textView;
    private Integer preId;
    private Integer disId;
    private Integer id;
    private Integer pId;
    private PrescriptionDomain pre;
    private GridAdapter gridAdapter;
    private DecimalFormat df1 = new DecimalFormat("#.##");
    private List<DispensingDetailDomain> dispensingDetailDomainList=new ArrayList<>();
    private List<HashMap<String, Object>> historyData = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validationdetail);
        validationdetail_linearLayout= (LinearLayout) findViewById(R.id.validationdetail_linearLayout);
        validationdetail_gridView = (GridView) findViewById(R.id.validationdetail_gridView);
        validationdetail_doctorName_textView= (TextView) findViewById(R.id.validationdetail_doctorName_textView);
        validationdetail_info_textView= (TextView) findViewById(R.id.validationdetail_info_textView);
        validationdetail_patientId_textView= (TextView) findViewById(R.id.validationdetail_patientId_textView);
        validationdetail_category_textView= (TextView) findViewById(R.id.validationdetail_category_textView);
        validationdetail_patientName_textView= (TextView) findViewById(R.id.validationdetail_patientName_textView);
        validationdetail_deptName_textView= (TextView) findViewById(R.id.validationdetail_deptName_textView);
        validationdetail_return_button= (Button) findViewById(R.id.validationdetail_return_button);
        validationdetail_confirm_button= (Button) findViewById(R.id.validationdetail_confirm_button);
        validationdetail_return_button.setOnClickListener(buttonListener);
        validationdetail_confirm_button.setOnClickListener(buttonListener);
        gridAdapter = new GridAdapter(this);
        validationdetail_gridView.setAdapter(gridAdapter);
        Intent in=getIntent();
        preId= in.getIntExtra("preId",0);
        disId= in.getIntExtra("disId",0);
        pId=in.getIntExtra("pId",0);
        Log.i("preId", String.valueOf(preId));
        id=in.getIntExtra("id",0);
        if(preId!=0) {
            //待选复核明细
            validationdetail_confirm_button.setText("确认复核");
            isReady=true;
            ValidationDetailThread(0);
        }else if(id!=0){
            //已选复核明细
            validationdetail_confirm_button.setText("通过复核");
            isReady=false;
            ValidationDetailThread(2);
        }
    }
    private class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.validationdetail_confirm_button:
                    if(isReady) {
                        //添加复核
                        ValidationDetailThread(1);
                    }else {
                        //复核通过
                        ValidationDetailThread(3);
                    }
                    break;
                case R.id.validationdetail_return_button:
                    finish();
                    break;
            }
        }
    }
    private void showDisList() {
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
    private void ValidationDetailThread(int what) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        validationdetail_info_textView.setText(pre.getClassification()+" "+pre.getPresNumber()+"付 "+pre.getWay()+pre.getManufacture()+pre.getFrequency());
                        validationdetail_patientName_textView.setText(pre.getPatientName());
                        validationdetail_category_textView.setText(pre.getCategory()+"：");
                        validationdetail_patientId_textView.setText(pre.getPatientNo());
                        validationdetail_doctorName_textView.setText(pre.getDoctorName());
                        validationdetail_deptName_textView.setText(pre.getDeptName());
                        parseObject(listDis);
                        showDisList();
                        break;
                    case 1:
                        new CoolToast( getBaseContext () ).show ((String) msg.obj);
                        finish();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        };

        new Thread () {
            @Override
            public void run() {
                super.run ();
                try {
                    switch (what){
                        case 0:
                            pre=new PrescriptionUtil().getPrescription(String.valueOf(preId));
                            listDis=new DispensingDetailUtil().getDispensingDetailByDisId(disId);
                            message.obj = "";
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                        case 1:
                            message.obj=new ValidationUtil().insert(String.valueOf(disId),String.valueOf( Application.getUsers().getId()));
                            message.what = 1;
                            handler.sendMessage ( message );
                            break;
                        case 2:
                            pre=new PrescriptionUtil().getPrescription(String.valueOf(pId));
                            listDis=new DispensingDetailUtil().getDispensingDetailByDisId(disId);
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                        case 3:
                            message.obj=new ValidationUtil().pass(String.valueOf(id));
                            message.what = 1;
                            handler.sendMessage ( message );
                            break;
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }
    //ListMap 转为 List<Bean>
    private void parseObject(List listDis) {
        dispensingDetailDomainList = new ArrayList<DispensingDetailDomain>();
        DispensingDetailDomain dispensingDetailDomain;
        if (listDis.size() != 0) {
            for (Object obj : listDis) {
                dispensingDetailDomain = new DispensingDetailDomain();
                dispensingDetailDomain.setId((Integer) ((Map) obj).get("id"));
                dispensingDetailDomain.setDisId((Integer) ((Map) obj).get("disId"));
                dispensingDetailDomain.setPlanId((String) ((Map) obj).get("planId"));
                dispensingDetailDomain.setHerbId((String) ((Map) obj).get("herbId"));
                dispensingDetailDomain.setHerbName((String) ((Map) obj).get("herbName"));
                dispensingDetailDomain.setHerbUnit((String) ((Map) obj).get("herbUnit"));
                dispensingDetailDomain.setQuantity(new BigDecimal(df1.format(((Map) obj).get("quantity"))));
                dispensingDetailDomain.setSpecial((String) ((Map) obj).get("special"));
                dispensingDetailDomain.setValuables((String) ((Map) obj).get("valuables"));
                dispensingDetailDomain.setUserId((Integer) ((Map) obj).get("userId"));
                dispensingDetailDomain.setDeptId((String) ((Map) obj).get("deptId"));
                dispensingDetailDomain.setHerbSpec((String) ((Map) obj).get("herbSpec"));
                dispensingDetailDomain.setWarning((String) ((Map) obj).get("warning"));
                if ((((Map) obj).get("beginTime") != null) && (((Map) obj).get("endTime") != null))
                    try {
                        dispensingDetailDomain.setBeginTime(sdf.parse((String) ((Map) obj).get("beginTime")));
                        dispensingDetailDomain.setEndTime(sdf.parse((String) ((Map) obj).get("endTime")));
                    } catch (ParseException e) {
//                e.printStackTrace();
                        new CoolToast(getBaseContext()).show((String) e.getMessage());
                    }
                dispensingDetailDomain.setStatus((String) ((Map) obj).get("status"));
                dispensingDetailDomain.setShelf((String) ((Map) obj).get("shelf"));

                dispensingDetailDomainList.add(dispensingDetailDomain);
            }
        }
    }
}
