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
import net.andy.com.CoolToast;
import net.andy.dispensing.util.ReplenishUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/11/22.
 */
@ContentView(R.layout.replenishdetail)
public class ReplenishDetailUI extends Activity {
    @ViewInject(R.id.replenishdetail_listView)
    private ListView replenishdetail_listView;
    @ViewInject(R.id.replenishdetail_button_linearLayout)
    private LinearLayout replenishdetail_button_linearLayout;
    @ViewInject(R.id.herbName)
    private TextView herbName;
    private Map replenishMap;
    private   List listMap;
    private Integer id;
    private int count = 0;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        replenishMap=(Map) bundle.getSerializable("replenishMap");
        listMap= (List) replenishMap.get("user");
        herbName.setText(((Map)replenishMap.get("herb")).get("herbName").toString());
        status=bundle.getString("status");
        if("完成".equals(status)){
            replenishdetail_button_linearLayout.setVisibility(View.GONE);
        }
        Adapter adpter=new Adapter(this);
        replenishdetail_listView.setAdapter(adpter);
    }
    @Event(value ={R.id.ackreplenishList_cancel_button,
    R.id.ackreplenishList_confirm_button,
    R.id.replenishdetail_linearLayout},
            type = View.OnClickListener.class)
    private void btnClick(View view){
        switch (view.getId()){
            case R.id.ackreplenishList_confirm_button:
                id= Integer.valueOf(((Map)listMap.get(count)).get("id").toString());
                count++;
                replenishThread(0);
                break;
            case R.id.ackreplenishList_cancel_button:
                finishActivity("");
                break;
            case R.id.replenishdetail_linearLayout:
                finishActivity("");
                break;
        }
    }
    Message message=new Message();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1:
                    new CoolToast(getBaseContext()).show(String.valueOf(msg.obj));
                    break;
                case 0:
                    for (; count <listMap.size() ; count++) {
//            Log.e("xx",((Map)listMap.get(x)).get("puser").toString());
                        id= Integer.valueOf(((Map)listMap.get(count)).get("id").toString());
                        replenishThread(0);
                    }
                    break;
                case 2:
                    new CoolToast(getBaseContext()).show(String.valueOf(msg.obj));
                    finishActivity(status);
                    break;
            }
        }
    };
    private void finishActivity(String sta){
        Intent intent=new Intent();
        intent.putExtra("status",sta);
        ReplenishDetailUI.this.setResult(RESULT_OK,intent);
        finish();
    }
    private void replenishThread(int what) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            message.obj=new ReplenishUtil().setStatus(id,"完成");
                            if(count==listMap.size()) {
                                message.what=2;
                            }else{
                                message.what=0;
                            }
                            handler.sendMessage(message);
                            break;

                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    message.obj=e.getMessage();
                    message.what=-1;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }
    public class Adapter extends BaseAdapter {
        private LayoutInflater inflater;
        private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private SimpleDateFormat format2 = new SimpleDateFormat("d日 HH:mm");

        public Adapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listMap.size();
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
            final Hold hold;

            if (view == null) {
                view = inflater.inflate(R.layout.replenishdetail_item, viewGroup, false);
                hold = new Hold();
                hold.id = (TextView) view.findViewById(R.id.id);
                hold.userName = (TextView) view.findViewById(R.id.userName);
                hold.dateTime = (TextView) view.findViewById(R.id.dateTime);
//                hold.button = (Button) view.findViewById(R.id.button);
                view.setTag(hold);
            } else {
                hold = (Hold) view.getTag();
            }
            Map map = (Map) listMap.get(i);
            Log.e("map",map.get("puser").toString());
            hold.id.setText(String.valueOf(map.get("id")));

            try {
                if("申请".equals(status)) {
                    hold.dateTime.setText(format2.format(format1.parse(String.valueOf(map.get("sendTime")))) + status);
                    hold.userName.setText((CharSequence) map.get("puser"));
                }else if("完成".equals(status)){
                    hold.dateTime.setText(format2.format(format1.parse(String.valueOf(map.get("finishTime")))) + status);
                    hold.userName.setText((CharSequence) map.get("ruser"));
                }
                if("紧急".equals(map.get("type"))){
                    hold.dateTime.setTextColor(Color.RED);
                    hold.userName.setTextColor(Color.RED);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return view;
        }
        private class Hold {
            private TextView id;
            private TextView userName;
            private TextView dateTime;
//            private Button button;
        }
    }

}
