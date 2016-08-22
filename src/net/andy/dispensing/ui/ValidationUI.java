package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import net.andy.dispensing.util.HerbalUtil;
import net.andy.dispensing.util.ReplenishUtil;
import net.andy.dispensing.util.ServerUtil;
import net.andy.dispensing.util.ValidationUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 复核处方
 * Created by Guang on 2016/5/26.
 */
public class ValidationUI extends Activity {
    @ViewInject(R.id.validation_title)
    private TextView validation_title;
    @ViewInject(R.id.validation_ready_list)
    private ListView validation_ready_list;
    @ViewInject(R.id.validation_already_list)
    private ListView validation_already_list;
    @ViewInject(R.id.validation_ready_button)
    private Button validation_ready_button;
    @ViewInject(R.id.validation_already_button)
    private Button validation_already_button;
    private String begin;
    private String end;
    private List list = new ArrayList<>();
    private ReadyAdapter readyAdapter;
    private AlreadyAdapter alreadyAdapter;
    private ValidationUtil util = new ValidationUtil();
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat format2 = new SimpleDateFormat("d日 HH:mm");
    private int what = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation);
        x.view().inject(this);
        readyAdapter = new ReadyAdapter(this);
        validation_ready_list.setAdapter(readyAdapter);
        alreadyAdapter = new AlreadyAdapter(this);
        validation_already_list.setAdapter(alreadyAdapter);
        getList(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validation_title.setText("待选复核");
        getList(0);
    }
    @Event(value = {
            R.id.validation_ready_button,
            R.id.validation_already_button
    },type = View.OnClickListener.class)
    private void btnClick(View v) {
            switch (v.getId()){
                case R.id.validation_ready_button:
                    getList(0);
                    validation_title.setText(((TextView) findViewById(R.id.validation_ready_button)).getText());
                    break;
                case R.id.validation_already_button:
                    getList(1);
                    validation_title.setText(((TextView) findViewById(R.id.validation_already_button)).getText());
                    break;
            }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1:
                    new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                    break;
                case 0:
                    if(list==null){
                        new CoolToast( getBaseContext () ).show ( "没有可选复核处方");
                    }else {
                        validation_ready_list.setVisibility(View.VISIBLE);
                        validation_already_list.setVisibility(View.GONE);
                        readyAdapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    if(list==null){
                        new CoolToast( getBaseContext () ).show ( "没有已选复核处方");
                    }else {
                        validation_ready_list.setVisibility(View.GONE);
                        validation_already_list.setVisibility(View.VISIBLE);
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
                            list = util.getWaitValidation(String.valueOf(Application.getUsers().getId()));
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                        case 1:
                            long serverTime = Long.parseLong(new ServerUtil().getServerTime());
//                            Log.e("serverTime", HerbalUtil.transferLongToDate("yyyy-MM-dd" ,serverTime));
//                            Log.e("serverTime", HerbalUtil.transferLongToDate("yyyy-MM-dd" ,serverTime+86400000));
                            list=util.getValidationByStatus(HerbalUtil.transferLongToDate("yyyy-MM-dd" ,serverTime),HerbalUtil.transferLongToDate("yyyy-MM-dd" ,serverTime+86400000), String.valueOf(Application.getUsers().getId()),"复核");
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
        }.start();
    }
    /*    监听ListView      */
   @Event(value = R.id.validation_ready_list,type = AdapterView.OnItemClickListener.class)
        private void readyItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
//            map.get("online_id_textView");
            Map map = (Map) list.get(position);
            Intent in = new Intent(ValidationUI.this, ValidationDetailUI.class);
            in.putExtra("disId",Integer.parseInt(String.valueOf(map.get("disId"))));
            in.putExtra("preId", Integer.parseInt(String.valueOf(map.get("id"))));
            startActivity(in);
    }
    /*    监听ListView      */
    @Event(value = R.id.validation_already_list,type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
//            map.get("online_id_textView");
            Map map = (Map) list.get(position);
            Intent in = new Intent(ValidationUI.this, ValidationDetailUI.class);
            in.putExtra("disId",Integer.parseInt(String.valueOf(map.get("disId"))));
            in.putExtra("id", Integer.parseInt(String.valueOf(map.get("id"))));
            in.putExtra("pId", Integer.parseInt(String.valueOf(map.get("pId"))));
            startActivity(in);
    }
    public class ReadyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ReadyAdapter(Context context) {
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
                view = inflater.inflate(R.layout.validationitem, viewGroup, false);
                val = new Val();
                val.validationItem_id = (TextView) view.findViewById(R.id.validationItem_id);
                val.validationItem_classification = (TextView) view.findViewById(R.id.validationItem_classification);
                val.validationItem_diser = (TextView) view.findViewById(R.id.validationItem_diser);
                val.validationItem_presNumber = (TextView) view.findViewById(R.id.validationItem_presNumber);
                val.validationItem_count = (TextView) view.findViewById(R.id.validationItem_count);
                val.validationItem_info = (TextView) view.findViewById(R.id.validationItem_info);
                val.validationItem_cnt=(TextView)view.findViewById(R.id.validationItem_cnt);
                view.setTag(val);
            } else {
                val = (Val) view.getTag();
            }
            Map map = (Map) list.get(i);
            Log.e("map", map.toString());
            val.validationItem_id .setText(String.valueOf(map.get("disId")));
            val.validationItem_diser .setText((CharSequence) map.get("userName"));
            val.validationItem_count.setText(i+1+"");
            val.validationItem_presNumber.setText(map.get("presNumber")+"付"+ map.get("herbCnt")+"味");
            val.validationItem_classification.setText( map.get("category")+""+ map.get("classification"));
            val.validationItem_info.setText( map.get("presName")+" "+map.get("way")+map.get("manufacture")+map.get("frequency"));
            val.validationItem_cnt.setText("剩余味数："+map.get("cnt"));
            return view;
        }

        private class Val {
            private TextView validationItem_id;
            private TextView validationItem_count;
            private TextView validationItem_diser;
            private TextView validationItem_classification;
            private TextView validationItem_presNumber;
            private TextView validationItem_info;
            private TextView validationItem_cnt;
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
                view = inflater.inflate(R.layout.validationitem_already, viewGroup, false);
                val = new Val();
                val.validationItem_already_id = (TextView) view.findViewById(R.id.validationItem_already_id);
                val.validationItem_already_count = (TextView) view.findViewById(R.id.validationItem_already_count);
                val.validationItem_already_diser = (TextView) view.findViewById(R.id.validationItem_already_diser);
                val.validationItem_already_time = (TextView) view.findViewById(R.id.validationItem_already_time);
                view.setTag(val);
            } else {
                val = (Val) view.getTag();
            }
            Map map = (Map) list.get(i);
            Log.e("map", map.toString());
            val.validationItem_already_id .setText(String.valueOf(map.get("id")));
            val.validationItem_already_diser .setText((CharSequence) map.get("dName"));
            val.validationItem_already_count.setText(i+1+"");
            try {
                val.validationItem_already_time .setText("选中时间：" +format2.format(format1.parse(String.valueOf(map.get("requestTime")))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return view;
        }
        private class Val {
            private TextView validationItem_already_id;
            private TextView validationItem_already_count;
            private TextView validationItem_already_time;
            private TextView validationItem_already_diser;
        }
    }

}
