package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.DatePickDialogUtil;
import net.andy.dispensing.util.SelectPresUtil;
import net.andy.dispensing.util.UrgentDelPresUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 查询处方
 * Created by Guang on 2016/7/5.
 */
public class SelectPresUI extends Activity{
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @ViewInject(R.id.selectPres_patient_listView)
    private ListView selectPres_patient_listView;
    @ViewInject(R.id.selectPres_search_button)
    private Button selectPres_search_button;
    @ViewInject(R.id.selectPres_patientId_editText)
    private EditText selectPres_patientId_editText;
    @ViewInject(R.id.selectPres_startTime_editText)
    private EditText selectPres_startTime_editText;
    @ViewInject(R.id.selectPres_endTime_editText)
    private EditText selectPres_endTime_editText;
    private String patId;
    private Integer Id;
    private String startTime;
    private String endTime;
    private List<Map<String, Object>> urgList = new ArrayList<Map<String, Object>>();
    private UrgDelAdapter urgDelAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectpres);
        x.view().inject(this);
        urgDelAdapter=new UrgDelAdapter(this);
        selectPres_patient_listView.setAdapter ( urgDelAdapter );
        Init();
    }
    private void Init(){
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        selectPres_startTime_editText.setText(dateFormat.format(cal_1.getTime()));
        selectPres_endTime_editText.setText(dateFormat.format(cal_1.getTime()));
    }
    @Event(value ={R.id.selectPres_search_button,
            R.id.selectPres_endTime_editText,
            R.id.selectPres_startTime_editText},
            type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()){
            case R.id.selectPres_search_button:
                confirm();
                break;
            case R.id.selectPres_startTime_editText:
                DatePickDialogUtil startdateTimePicKDialog = new DatePickDialogUtil(
                        SelectPresUI.this,String.valueOf( selectPres_startTime_editText.getText()));
                startdateTimePicKDialog.dateTimePicKDialog(selectPres_startTime_editText);
                break;
            case R.id.selectPres_endTime_editText:
                DatePickDialogUtil enddateTimePicKDialog = new DatePickDialogUtil(
                        SelectPresUI.this,String.valueOf( selectPres_endTime_editText.getText()));
                enddateTimePicKDialog.dateTimePicKDialog(selectPres_endTime_editText);
                break;
        }
    }
    private void confirm(){
        urgList.clear();
        patId=selectPres_patientId_editText.getText().toString().trim();
        startTime=selectPres_startTime_editText.getText().toString().trim();
        endTime=selectPres_endTime_editText.getText().toString().trim();
        if("".equals(startTime)||"".equals(endTime)){
            new CoolToast(getBaseContext()).show("开始时间或结束时间不能为空");
        }else {
            if ("".equals(patId)) {
                new CoolToast(getBaseContext()).show("请输入门诊号或住院号");
            } else {
                startActivity(new Intent(SelectPresUI.this, LoadingUI.class));
                endTime+=" 23:59:59";
                selectPresThread(0);
            }
        }
    }
    @Event(value = R.id.selectPres_patient_listView,type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map map = (Map) urgList.get(position);
            Intent in=new Intent(SelectPresUI.this,PrescriptionStatusUI.class);
            in.putExtra("id",String.valueOf(map.get("id")));
            startActivity(in);
    }
    public void setListView(List presList) {
        urgDelAdapter.notifyDataSetChanged();
        if(presList.size()==0){
            new CoolToast( getBaseContext () ).show ( "未查询到此卡号的相关信息");
            return;
        }
        urgList=presList;
        urgDelAdapter.notifyDataSetChanged();
    }
    private void selectPresThread(int what) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        LoadingUI.instance.finish();
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        LoadingUI.instance.finish();
                        setListView ( (List) msg.obj );
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
                            message.what = 0;
                            message.obj = new SelectPresUtil().getPrescriptionByPatientNo(startTime,endTime,patId, String.valueOf(Application.getUsers().getId()));
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
    private class UrgDelAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public UrgDelAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return urgList.size();
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
            final UrgDelPresView urgDelPresView;
            if (view == null) {
                view = inflater.inflate(R.layout.selectpreslist, viewGroup, false);
                urgDelPresView = new UrgDelPresView();
                urgDelPresView.selectPres_id_textView  = (TextView) view.findViewById(R.id.selectPres_id_textView);
                urgDelPresView.selectPres_presDept_textView  = (TextView) view.findViewById(R.id.selectPres_presDept_textView);
                urgDelPresView.selectPres_category_textView= (TextView) view.findViewById(R.id.selectPres_category_textView);
                urgDelPresView.selectPres_name_textView = (TextView) view.findViewById(R.id.selectPres_name_textView);
                urgDelPresView.selectPres_patientName_textView= (TextView) view.findViewById(R.id.selectPres_patientName_textView);
                urgDelPresView.selectPres_main_textView= (TextView) view.findViewById(R.id.selectPres_main_textView);
                urgDelPresView.selectPres_way_textView= (TextView) view.findViewById(R.id.selectPres_way_textView);
                urgDelPresView.selectPres_subTime_textView= (TextView) view.findViewById(R.id.selectPres_subTime_textView);
                view.setTag(urgDelPresView);
            } else {
                urgDelPresView = (UrgDelPresView) view.getTag();
            }
            Map map = (Map)urgList.get(i);
            Log.e("map", map.toString());
            urgDelPresView.selectPres_id_textView .setText(""+map.get ( "id" ));
            urgDelPresView.selectPres_patientName_textView.setText(""+map.get("patientName"));
            urgDelPresView.selectPres_category_textView.setText(""+map.get ( "category" )+map.get ("classification" )+" "+ map.get ( "patientNo" ));
            urgDelPresView.selectPres_name_textView  .setText(""+ map.get("patientName"));
            urgDelPresView.selectPres_presDept_textView .setText(map.get("deptName")+" "+ map.get("doctorName"));
            urgDelPresView.selectPres_main_textView .setText(""+ map.get("main")+ " " +map.get("presNumber")+"付"+ map.get("herbCnt")+"味");
            urgDelPresView.selectPres_way_textView .setText(""+ map.get("way")+ map.get("process")+ map.get("frequency"));
            try {
                urgDelPresView.selectPres_subTime_textView.setText(dateFormat.format(dateFormat.parse(String.valueOf(map.get("subTime")))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (i == selectItem) {
//                view.setBackgroundColor(Color.RED);
//            }
//            else {
//                view.setBackgroundColor(Color.BLUE);
//            }
            return view;
        }
//        public  void setSelectItem(int selectItem) {
//            this.selectItem = selectItem;
//        }
//        private int  selectItem=-1;
        private class UrgDelPresView {
            private TextView selectPres_presDept_textView;
            private TextView selectPres_patientName_textView;
            private TextView selectPres_name_textView;
            private TextView selectPres_category_textView;
            private TextView selectPres_id_textView;
            private TextView selectPres_main_textView;
            private TextView selectPres_way_textView;
            private TextView selectPres_subTime_textView;
            }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        confirm();
    }
}
