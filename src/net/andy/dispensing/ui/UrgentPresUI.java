package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.UrgentDelPresUtil;
import net.andy.hos.ui.ExtInPatientUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 置顶处方
 * Created by Guang on 2016/5/18.
 */
public class UrgentPresUI extends Activity{
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ListView urgentPres_patient_listView;
    private Button urgentPres_search_button;
    private EditText urgentPres_patientId_editText;
    private String patId;
    private Integer Id;
    private List<Map<String, Object>> urgList = new ArrayList<Map<String, Object>>();
    private UrgDelAdapter urgDelAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urgentpres);
        urgentPres_patient_listView= (ListView) findViewById(R.id.urgentPres_patient_listView);
        urgentPres_search_button= (Button) findViewById(R.id.urgentPres_search_button);
        urgentPres_patientId_editText= (EditText) findViewById(R.id.urgentPres_patientId_editText);
        urgDelAdapter=new UrgDelAdapter(this);
        urgentPres_patient_listView.setAdapter ( urgDelAdapter );
//        urgentPres_patient_listView.setOnItemClickListener ( new ListItemClick() );
        urgentPres_patient_listView.setOnItemLongClickListener(new LongClick());
        urgentPres_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }
    private void confirm(){
        urgList.clear();
        patId=urgentPres_patientId_editText.getText().toString().trim();
        if("".equals(patId)){
            new CoolToast(getBaseContext()).show("请输入门诊号或住院号");
        }else {
            startActivity(new Intent(UrgentPresUI.this, LoadingUI.class));
            urgentPresThread(0);
        }
    }
    private class LongClick implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Map map = (Map) urgList.get(position);
//            dialog(Integer.parseInt(String.valueOf(map.get("urgentPres_id_textView"))),map.get("urgentPres_name_textView")+"");
            Intent in=new Intent(UrgentPresUI.this,UrgDelPresUI.class);
            in.putExtra("id",String.valueOf(map.get("id")));
            startActivity(in);
            return false;
        }
    }
    /*    监听ListView      */
//    private class ListItemClick implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
//
//            dialog(Integer.parseInt(String.valueOf(map.get("urgentPres_id_textView"))),map.get("urgentPres_name_textView")+"");
//        }
//    }
//    protected void dialog(Integer id,String name) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(UrgentPresUI.this);
//        builder.setMessage("优先调剂"+name+"的处方？");  builder.setTitle("提示");
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//        public void onClick(DialogInterface dialog, int which) {
//            dialog.dismiss();
//                Id=id;
//                urgentPresThread(1);
//        }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//        public void onClick(DialogInterface dialog, int which) {
//            dialog.dismiss();
//        }
//        });
//        builder.create().show();
//    }
    public void setListView(List presList) {
        urgDelAdapter.notifyDataSetChanged();
        if(presList.size()==0){
            new CoolToast( getBaseContext () ).show ( "此卡号没有需调剂处方");
            return;
        }
        urgList=presList;
        urgDelAdapter.notifyDataSetChanged();
    }
    private void urgentPresThread(int what) {
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
                            message.obj = new UrgentDelPresUtil().getPrescriptionByPatientNo(patId, String.valueOf(Application.getUsers().getId()));
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
                view = inflater.inflate(R.layout.urgentpreslist, viewGroup, false);
                urgDelPresView = new UrgDelPresView();
                urgDelPresView.urgentPres_id_textView  = (TextView) view.findViewById(R.id.urgentPres_id_textView);
                urgDelPresView.urgentPres_presDept_textView  = (TextView) view.findViewById(R.id.urgentPres_presDept_textView);
                urgDelPresView.urgentPres_category_textView= (TextView) view.findViewById(R.id.urgentPres_category_textView);
                urgDelPresView.urgentPres_name_textView = (TextView) view.findViewById(R.id.urgentPres_name_textView);
                urgDelPresView.urgentPres_patientName_textView= (TextView) view.findViewById(R.id.urgentPres_patientName_textView);
                urgDelPresView.urgentPres_main_textView= (TextView) view.findViewById(R.id.urgentPres_main_textView);
                urgDelPresView.urgentPres_way_textView= (TextView) view.findViewById(R.id.urgentPres_way_textView);
                urgDelPresView.urgentPres_subTime_textView= (TextView) view.findViewById(R.id.urgentPres_subTime_textView);
                view.setTag(urgDelPresView);
            } else {
                urgDelPresView = (UrgDelPresView) view.getTag();
            }
            Map map = (Map)urgList.get(i);
            Log.e("map", map.toString());
            urgDelPresView.urgentPres_id_textView .setText(""+map.get ( "id" ));
            urgDelPresView.urgentPres_patientName_textView.setText(""+map.get("patientName"));
            urgDelPresView.urgentPres_category_textView.setText(""+map.get ( "category" )+map.get ("classification" )+" "+ map.get ( "patientNo" ));
            urgDelPresView.urgentPres_name_textView  .setText(""+ map.get("patientName"));
            urgDelPresView.urgentPres_presDept_textView .setText(map.get("deptName")+" "+ map.get("doctorName"));
            urgDelPresView.urgentPres_main_textView .setText(""+ map.get("main")+ " " +map.get("presNumber")+"付"+ map.get("herbCnt")+"味");
            urgDelPresView.urgentPres_way_textView .setText(""+ map.get("way")+ map.get("process")+ map.get("frequency"));
            try {
                urgDelPresView.urgentPres_subTime_textView.setText(dateFormat.format(dateFormat.parse(String.valueOf(map.get("subTime")))));
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
            private TextView urgentPres_presDept_textView;
            private TextView urgentPres_patientName_textView;
            private TextView urgentPres_name_textView;
            private TextView urgentPres_category_textView;
            private TextView urgentPres_id_textView;
            private TextView urgentPres_main_textView;
            private TextView urgentPres_way_textView;
            private TextView urgentPres_subTime_textView;
}
    }
    @Override
    protected void onResume() {
        super.onResume();
//        confirm();
    }
}
