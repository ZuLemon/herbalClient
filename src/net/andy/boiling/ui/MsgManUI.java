package net.andy.boiling.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import net.andy.boiling.R;
import net.andy.boiling.domain.EquipmentDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.util.EquipmentUtil;
import net.andy.boiling.util.MsgUtil;
import net.andy.com.CoolToast;
import net.andy.com.NFCActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *消息管理
 * @author RongGuang
 * @date 2016/02/16
 */
public class MsgManUI extends Activity {
    private ReturnDomain returnDomain = new ReturnDomain();
    private ListView equip_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msgman);
        equip_listView = (ListView) findViewById(R.id.msgman_list_listView);
        equip_listView.setOnItemClickListener(new OnListItemClick());
        getMsgList();
    }

    /*    监听ListView      */
    public class OnListItemClick implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map map = (Map) (parent.getItemAtPosition(position));
//            openReturnUI ( String.valueOf ( map.get ( "msglist_planStatus_textView" ) ) );
            //回传值
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("planId", String.valueOf(map.get("msglist_planId_textView")));
            bundle.putString("planStatus", String.valueOf(map.get("msglist_planStatus_textView")));
            bundle.putString("code", String.valueOf(map.get("msglist_code_textView")));
            intent.putExtras(bundle);
            intent.setClass(MsgManUI.this, ExtractUI.class);
            MsgManUI.this.startActivity(intent);
        }
    }

    //    public void openReturnUI(String equipId) {
//        Intent intent = new Intent ( this, TakeUI.class );
//        intent.putExtra ( "equipId", equipId );
//        setResult ( 0, intent );
////        finish ();
//    }
    public void getData(List<Map> returnResult) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object obj : returnResult) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("msglist_equipId_textView", ((Map) obj).get("equipId"));
            map.put("msglist_planStatus_textView", ((Map) obj).get("planStatus"));
            map.put("msglist_content_textView", ((Map) obj).get("color").toString()
                    + ((Map) obj).get("code").toString()
                    + "处方已于"
                    + ((Map) obj).get("endTime").toString().substring(10, 16)
                    + "完成");
            map.put("msglist_planId_textView", ((Map) obj).get("planId"));
            map.put("msglist_code_textView", ((Map) obj).get("code"));
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.msglist,
                new String[]{"msglist_equipId_textView", "msglist_planStatus_textView",
                        "msglist_content_textView", "msglist_planId_textView","msglist_code_textView"},
                new int[]{R.id.msglist_equipId_textView, R.id.msglist_planStatus_textView,
                        R.id.msglist_content_textView, R.id.msglist_planId_textView,R.id.msglist_code_textView});
        equip_listView.setAdapter(adapter);
    }

    public void getMsgList() {
        final Message message = new Message();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case -1:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                    case 0:
                        getData((List<Map>) msg.obj);
                        break;
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    List<Map> returnResult = new MsgUtil().getMsg();
                    message.what = 0;
                    message.obj = returnResult;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }

            }
        }.start();
    }

}
