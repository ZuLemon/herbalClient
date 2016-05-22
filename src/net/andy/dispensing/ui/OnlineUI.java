package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.OnlineUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/5/20.
 */
public class OnlineUI extends Activity{
    private ListView online_uname_listView;
    private Integer userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online);
        online_uname_listView= (ListView) findViewById(R.id.online_uname_listView);
        online_uname_listView.setOnItemClickListener ( new ListItemClick() );
        onlineThread(0);
    }
    /*    监听ListView      */
    public class ListItemClick implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
            dialog(Integer.parseInt(String.valueOf(map.get("online_id_textView"))),map.get("online_uname_textView")+"");
        }
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
    public void setListView(List UserList) {
        if(UserList.size()==0){
            new CoolToast( getBaseContext () ).show ( "无在线用户");
            finish();
            return;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object obj : UserList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put ( "online_id_textView", ( ( Map ) obj ).get ( "id" ));
            map.put ( "online_uname_textView", ( ( Map ) obj ).get ( "name" ) );
            list.add ( map );
        }
        SimpleAdapter adapter = new SimpleAdapter ( this, list, R.layout.onlinelist,
                new String[]{ "online_id_textView","online_uname_textView" },
                new int[]{ R.id.online_id_textView,R.id.online_uname_textView} );
                online_uname_listView.setAdapter ( adapter );
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
                        break;
                    case 0:
                        setListView ( ( List ) msg.obj );
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
                            message.obj = new OnlineUtil().getOnline(new AppOption().getOption(AppOption.APP_OPTION_USER));
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
}
