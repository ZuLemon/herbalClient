package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.DispensingDetailDomain;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.util.ReplenishUtil;
import net.andy.dispensing.util.StationUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 确认上药申请
 * Created by Guang on 2016/4/20.
 */

public class AckReplenishUI extends Activity {
    @ViewInject(R.id.acklinearLayout)
    private LinearLayout acklinearLayout;
    @ViewInject(R.id.ackreplenish_ackMechine_linearLayout)
    private LinearLayout ackreplenish_ackMechine_linearLayout;
    @ViewInject(R.id.ackreplenish_info_textView)
    private TextView ackreplenish_info_textView;
    @ViewInject(R.id.ackreplenish_listView)
    private ListView ackreplenish_listView;
    @ViewInject(R.id.ackreplenish_confirm_button)
    private Button ackreplenish_confirm_button;
    @ViewInject(R.id.ackreplenish_cancel_button)
    private Button ackreplenish_cancel_button;
    @ViewInject(R.id.ackreplenish_ack_linearLayout)
    private LinearLayout ackreplenish_ack_linearLayout;
    private List list = new ArrayList<>();
    private ReplenishUtil util = new ReplenishUtil();
    private Adapter ackAdapter;
    private DispensingDetailDomain dis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ackreplenish);
        x.view().inject(this);
        Intent in=getIntent();
        dis= (DispensingDetailDomain) in.getSerializableExtra("dis");
        if(dis!=null) {
            String herbName = dis.getHerbName();
            ackreplenish_info_textView.setText("你确认申请" + herbName + "上药吗？");
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(ackreplenish_info_textView.getText().toString());
            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
            spannableStringBuilder.setSpan(redSpan, 5, 5 + herbName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ackreplenish_info_textView.setText(spannableStringBuilder);
            ackAdapter = new Adapter(this);
            ackreplenish_listView.setAdapter(ackAdapter);
            handler.sendEmptyMessage(0);
            acklinearLayout.setVisibility(View.VISIBLE);
        }else{
            ackreplenish_ack_linearLayout.setVisibility(View.GONE);
            ackAdapter = new Adapter(this);
            ackreplenish_listView.setAdapter(ackAdapter);
            handler.sendEmptyMessage(0);
        }
//        getData();
    }
    @Event(value = {
            R.id.acklinearLayout,
            R.id.ackreplenish_confirm_button,
            R.id.ackreplenish_cancel_button
    },type = View.OnClickListener.class)
   private void btnClick(View view) {
            switch (view.getId()){
                case R.id.acklinearLayout:
                    finish();
                    break;
                case R.id.ackreplenish_confirm_button:
                    getData(1);
                    break;
                case R.id.ackreplenish_cancel_button:
                    finish();
                    break;
            }
    }
    final Message message = new Message();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case -1:
                if(list.size()==0){
                    ackreplenish_ackMechine_linearLayout.setVisibility(View.GONE);
                    new CoolToast(Application.getContext()).show("暂无上药确认");
                    if(dis==null){
                        finish();
                    }
                }else{
                    acklinearLayout.setVisibility(View.VISIBLE);
                    ackreplenish_ackMechine_linearLayout.setVisibility(View.VISIBLE);
                    ackAdapter.notifyDataSetChanged();
                }
                break;
            case 0:
                getData(0);
                break;
                case 1:
                    new CoolToast(getBaseContext()).show((String) msg.obj);
                    finish();
                    break;
            }
        }
    };
    public void getData(int what) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what){
                        case 0:
                            list = new ArrayList<>();
                            list = util.getReplenishByStatus("replenish/getReplenishByRequestUser.do",
                                    new AppOption().getOption(AppOption.APP_OPTION_USER), "接收");
                            handler.sendEmptyMessage(-1);
                            break;
                        case 1:
                            StationDomain stationDomain=new StationUtil().getStationByDevice();
                            message.obj=util.request(stationDomain.getId(),dis.getHerbId());
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new CoolToast(getBaseContext()).show((String) e.getMessage());
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
            final Hold hold;
            if (view == null) {
                view = inflater.inflate(R.layout.ackreplenishitem, viewGroup, false);
                hold = new Hold();
                hold.id = (TextView) view.findViewById(R.id.id);
                hold.userName = (TextView) view.findViewById(R.id.userName);
                hold.sendTime = (TextView) view.findViewById(R.id.sendTime);
                hold.status = (TextView) view.findViewById(R.id.status);
                hold.herbName = (TextView) view.findViewById(R.id.herbName);
                hold.shelf = (TextView) view.findViewById(R.id.shelf);
                hold.replenish_button = (Button) view.findViewById(R.id.replenish_button);
                view.setTag(hold);
            } else {
                hold = (Hold) view.getTag();
            }

            Map map = (Map) list.get(i);
            Log.e("map", map.toString());
            hold.id.setText(String.valueOf(map.get("id")));
            hold.userName.setText((CharSequence) map.get("userName"));
            try {
                hold.sendTime.setText(""+(System.currentTimeMillis()-(format1.parse(String.valueOf(map.get("sendTime"))).getTime()))/(60*1000)+"分钟前");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hold.status.setText("申请");
            hold.herbName.setText((CharSequence) map.get("herbName"));
            hold.shelf.setText((CharSequence) map.get("shelf"));

                    hold.replenish_button.setText("确认");
                    hold.replenish_button.setVisibility(View.VISIBLE);
                    hold.replenish_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            request("replenish/finish.do", String.valueOf(hold.id.getText()));
                        }
                    });

            return view;
        }

        private void request(String url, String id) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Looper.prepare();
                    try {
                        util.setStatus(url, id);
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Application.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }
            }.start();
        }

        private class Hold {
            private TextView id;
            private TextView userName;
            private TextView sendTime;
            private TextView status;
            private TextView herbName;
            private TextView shelf;
            private Button replenish_button;
        }
    }
}
