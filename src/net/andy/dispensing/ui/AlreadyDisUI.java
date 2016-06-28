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
import net.andy.dispensing.util.AlreadyDisUtil;
import net.andy.dispensing.util.HerbalUtil;
import net.andy.dispensing.util.ServerUtil;
import net.andy.dispensing.util.ValidationUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 已调剂处方
 * Created by Guang on 2016/6/22.
 */
public class AlreadyDisUI extends Activity {
    private ListView alreadydis_list;
    private List list = new ArrayList<>();
    private AlreadyAdapter alreadyAdapter;
    private AlreadyDisUtil util = new AlreadyDisUtil();
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alreadydis);
        alreadydis_list= (ListView) findViewById(R.id.alreadydis_list);
        alreadyAdapter = new AlreadyAdapter(this);
        alreadydis_list.setAdapter(alreadyAdapter);
        startActivity(new Intent(AlreadyDisUI.this,LoadingUI.class));
        getList(0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoadingUI.instance.finish();
            switch (msg.what){
                case -1:
                    new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                    break;
                case 0:
                    if(list==null||"[]".equals(String.valueOf(list))){
                        new CoolToast( getBaseContext () ).show ( "今日未调剂处方");
                        finish();
                    }else{
                        alreadyAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    public void getList(int what) {
        final Message message = new Message ();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            list = util.getPrescriptionByUserId(Application.getUsers().getId());
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                    }

                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start();
    }
    /*    监听ListView      */
    public class AlreadyListItemClick implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
//            map.get("online_id_textView");
            Map map = (Map) list.get(position);
            Intent in = new Intent(AlreadyDisUI.this, ValidationDetailUI.class);
            in.putExtra("disId",Integer.parseInt(String.valueOf(map.get("disId"))));
            in.putExtra("id", Integer.parseInt(String.valueOf(map.get("id"))));
            in.putExtra("pId", Integer.parseInt(String.valueOf(map.get("pId"))));
            startActivity(in);
        }
    }
    public class AlreadyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public AlreadyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
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
            final Val val;
            if (view == null) {
                view = inflater.inflate(R.layout.alreadydisitem, viewGroup, false);
                val = new Val();
                val.alreadydisitem_id = (TextView) view.findViewById(R.id.alreadydisitem_id);
                val.validationItem_count = (TextView) view.findViewById(R.id.validationItem_count);
                val.alreadydisitem_startTime = (TextView) view.findViewById(R.id.alreadydisitem_startTime);
                val.alreadydisitem_info = (TextView) view.findViewById(R.id.alreadydisitem_info);
                val.alreadydisitem_presNumber = (TextView) view.findViewById(R.id.alreadydisitem_presNumber);
                val.alreadydisitem_classification = (TextView) view.findViewById(R.id.alreadydisitem_classification);
                val.alreadydisitem_patientName = (TextView) view.findViewById(R.id.alreadydisitem_patientName);
                val.alreadydisitem_patientNo  = (TextView) view.findViewById(R.id.alreadydisitem_patientNo);
                view.setTag(val);
            } else {
                val = (Val) view.getTag();
            }
            Map map = (Map) list.get(i);
            Log.e("map", map.toString());
            val.alreadydisitem_id .setText(String.valueOf(map.get("id")));
            val.validationItem_count.setText(i+1+"");
            val.alreadydisitem_patientName .setText(""+map.get("patientName"));
            val.alreadydisitem_classification .setText(""+map.get("category")+map.get("classification"));
            val.alreadydisitem_presNumber .setText(""+ map.get("presNumber")+"付"+ map.get("herbCnt")+"味");
            val.alreadydisitem_patientNo .setText(map.get("category")+"号："+map.get("patientNo"));
            try {
                val.alreadydisitem_startTime .setText(format2.format(format1.parse(String.valueOf(map.get("beginTime")))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return view;
        }
        private class Val {
            private TextView alreadydisitem_id;
            private TextView validationItem_count;
            private TextView alreadydisitem_startTime;
            private TextView alreadydisitem_info;
            private TextView alreadydisitem_presNumber;
            private TextView alreadydisitem_classification;
            private TextView alreadydisitem_patientName;
            private TextView alreadydisitem_patientNo;
        }
    }

}
