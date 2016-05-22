package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.ReplenishDomain;
import net.andy.dispensing.util.ReplenishUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/3/16.
 */
public class ReplenishUI extends Activity {
    private TextView replenish_title;
    private ListView replenish_list;
    private Button replenish_new;
    private Button replenish_accept;
    private Button replenish_finish;
    private List list = new ArrayList<>();
    private Adapter adapter;
    private ReplenishUtil util = new ReplenishUtil();
    private int what = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replenish);
        replenish_title = (TextView) findViewById(R.id.replenish_title);
        replenish_list = (ListView) findViewById(R.id.replenish_list);
        replenish_new = (Button) findViewById(R.id.replenish_new);
        replenish_accept = (Button) findViewById(R.id.replenish_accept);
        replenish_finish = (Button) findViewById(R.id.replenish_finish);
        adapter = new Adapter(this);
        replenish_list.setAdapter(adapter);
        handler.sendEmptyMessage(what);
        findViewById(R.id.replenish_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                what = 0;
                handler.sendEmptyMessage(what);
                replenish_title.setText(((TextView) findViewById(R.id.replenish_new)).getText());
            }
        });

        findViewById(R.id.replenish_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                what = 1;
                handler.sendEmptyMessage(what);
                replenish_title.setText(((TextView) findViewById(R.id.replenish_accept)).getText());
            }
        });

        findViewById(R.id.replenish_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                what = 2;
                handler.sendEmptyMessage(what);
                replenish_title.setText(((TextView) findViewById(R.id.replenish_finish)).getText());
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == -1) {
                adapter.notifyDataSetChanged();
            } else {
                getList(msg.what);
            }
        }
    };

    public void getList(int what) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            list = util.getReplenishNoAccept();
                            break;
                        case 1:
                            list = util.getReplenishByStatus("replenish/getReplenishByAcceptUser.do",
                                    new AppOption().getOption(AppOption.APP_OPTION_USER), "接收");
                            break;
                        case 2:
                            list = util.getReplenishByStatus("replenish/getReplenishByAcceptUser.do",
                                    new AppOption().getOption(AppOption.APP_OPTION_USER), "完成");
                            break;
                    }
                    handler.sendEmptyMessage(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Application.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
                view = inflater.inflate(R.layout.replenishitem, viewGroup, false);
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
                hold.sendTime.setText(format2.format(format1.parse(String.valueOf(map.get("sendTime")))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hold.status.setText((CharSequence) map.get("status"));
            hold.herbName.setText((CharSequence) map.get("herbName"));
            hold.shelf.setText((CharSequence) map.get("shelf"));
            switch (what) {
                case 0:
                    hold.replenish_button.setText("接收");
                    hold.replenish_button.setVisibility(View.VISIBLE);
                    hold.replenish_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            request("replenish/accept.do", String.valueOf(hold.id.getText()));
                        }
                    });
                    break;
                case 1:
                    hold.replenish_button.setVisibility(View.GONE);
                    break;
                case 2:
                    hold.replenish_button.setVisibility(View.GONE);
                    break;
            }
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
                        handler.sendEmptyMessage(what);
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
