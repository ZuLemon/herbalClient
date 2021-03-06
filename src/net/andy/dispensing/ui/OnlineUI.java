package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.OnlineUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线用户
 * Created by Guang on 2016/5/20.
 */
public class OnlineUI extends Activity{
    @ViewInject(R.id.online_uname_listView)
    private ListView online_uname_listView;
    @ViewInject(R.id.online_refresh_linearLayout)
    private LinearLayout online_refresh_linearLayout;
    private OnlineAdapter onlineAdapter;
    private Integer userId;
    private List userList;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf2= new SimpleDateFormat("d日 HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online);
        x.view().inject(this);
        onlineAdapter=new OnlineAdapter(this);

        onlineThread(0);
    }
    /*    监听ListView      */
    @Event(value = R.id.online_uname_listView,type = AdapterView.OnItemClickListener.class)
   private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
            Map map = (Map) userList.get(position);
            dialog(Integer.parseInt(String.valueOf(map.get("id"))),map.get("name")+"");
    }
    @Event(value = R.id.online_refresh_linearLayout,
    type = View.OnClickListener.class)
    private void btnClick(View v){
        startActivity(new Intent(OnlineUI.this,LoadingUI.class));
        onlineThread(0);
    }
    protected void dialog(Integer id,String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OnlineUI.this);
        builder.setMessage("强制"+name+"下线？");  builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
                userId=id;
                onlineThread(1);

        }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
        });
        builder.create().show();
    }
    public void setListView() {
        if(userList.size()==0){
            new CoolToast( getBaseContext () ).show ( "无在线用户");
            finish();
            return;
        }else{
            online_uname_listView.setAdapter ( onlineAdapter );
            onlineAdapter.notifyDataSetChanged();
        }
    }
    private void onlineThread(int what) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        LoadingUI.instance.finish();
                        break;
                    case 0:
                        setListView ();
                        LoadingUI.instance.finish();
                        break;
                    case 1:
                        onlineThread(0);
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
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
                            userList= new OnlineUtil().getOnline(new AppOption().getOption(AppOption.APP_OPTION_USER));
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                        case 1:
                            message.what = 1;
                            message.obj = new OnlineUtil().offline(userId);
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

    private class OnlineAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public OnlineAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return userList.size();
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
            final OnlineView onlineView;
            if (view == null) {
                view = inflater.inflate(R.layout.onlinelist, viewGroup, false);
                onlineView = new OnlineView();
                onlineView.online_id_textView  = (TextView) view.findViewById(R.id.online_id_textView);
                onlineView.online_count_textView= (TextView) view.findViewById(R.id.online_count_textView);
                onlineView.online_uname_textView = (TextView) view.findViewById(R.id.online_uname_textView);
                onlineView.online_ruleName_textView= (TextView) view.findViewById(R.id.online_ruleName_textView);
                onlineView.online_last_textView= (TextView) view.findViewById(R.id.online_last_textView);
                view.setTag(onlineView);
            } else {
                onlineView = (OnlineView) view.getTag();
            }
            Map map = (Map) userList.get(i);
            Log.e("map", map.toString());
            onlineView.online_id_textView  .setText(String.valueOf(map.get("id")));
            onlineView.online_uname_textView .setText((CharSequence) map.get("name"));
            onlineView.online_ruleName_textView .setText((CharSequence) map.get("ruleName"));
            onlineView.online_count_textView.setText(i+1+"");
            try {
                onlineView.online_last_textView .setText(sdf2.format(sdf1.parse(String.valueOf(map.get("last")))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return view;
        }
        private class OnlineView {
            private TextView online_id_textView;
            private TextView online_count_textView;
            private TextView online_uname_textView;
            private TextView online_ruleName_textView;
            private TextView online_last_textView;
        }
    }
}
