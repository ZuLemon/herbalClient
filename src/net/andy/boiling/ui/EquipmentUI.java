package net.andy.boiling.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.R;
import net.andy.boiling.domain.EquipmentDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.util.EquipmentUtil;
import net.andy.com.CoolToast;
import net.andy.com.Http;
import net.andy.com.NFCActivity;
import org.apache.http.NameValuePair;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014-11-18.
 * 设备列表
 */
public class EquipmentUI extends NFCActivity {
    private ReturnDomain returnDomain = new ReturnDomain();
    @ViewInject(R.id.equip_list_listView)
    private ListView equip_list_listView;
    @ViewInject(R.id.equip_my_button)
    private Button equip_my_button;
    @ViewInject(R.id.equip_all_button)
    private Button equip_all_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment);
        x.view().inject(this);
        getEquipmentList("equipment/getEquipmentByUser.do");
    }

    @Event(value = {R.id.equip_my_button, R.id.equip_all_button}, type = View.OnClickListener.class)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.equip_my_button:
                getEquipmentList("equipment/getEquipmentByUser.do");
                break;
            case R.id.equip_all_button:
                getEquipmentList("equipment/getEquipmentOfAll.do");
                break;
        }
    }

    /*    监听ListView   并回传选择的数据     */
    @Event(value = R.id.equipman_list_listView, type = AdapterView.OnItemClickListener.class)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map map = (Map) (parent.getItemAtPosition(position));
//            openReturnUI(String.valueOf(map.get("equip_equipId_textView")));
        System.out.println("#" + String.valueOf(map.get("equip_id_textView")));
        //回传值
        Intent intentEquip = new Intent(EquipmentUI.this, TakeUI.class);
        Bundle bundle = new Bundle();
        bundle.putString("eId", String.valueOf(map.get("equip_id_textView")));
        bundle.putString("equipId", String.valueOf(map.get("equip_equipId_textView")));
        intentEquip.putExtras(bundle);
        setResult(1001, intentEquip);
        //    结束当前这个Activity对象的生命
        finish();
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        getEquipment(getNfc().readID(intent));
    }

    public void openReturnUI(String equipId) {
        Intent intent = new Intent(this, TakeUI.class);
        intent.putExtra("equipId", equipId);
        setResult(0, intent);
        finish();
    }

    public void getData(List equipList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object obj : equipList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("equip_equipId_textView", ((Map) obj).get("equipId"));
            map.put("equip_equipPurpose_textView", ((Map) obj).get("equipPurpose"));
            map.put("equip_equipStatus_textView", ((Map) obj).get("equipStatus"));
            map.put("equip_id_textView", ((Map) obj).get("id"));
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.equipmentlist,
                new String[]{"equip_equipId_textView", "equip_equipPurpose_textView",
                        "equip_equipStatus_textView", "equip_id_textView"},
                new int[]{R.id.equip_equipId_textView, R.id.equip_equipPurpose_textView,
                        R.id.equip_equipStatus_textView, R.id.equip_id_textView});
        equip_list_listView.setAdapter(adapter);
    }

    public void getEquipment(final String tagId) {

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
                        openReturnUI((String) msg.obj);
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    EquipmentDomain equipment = new EquipmentUtil().getEquipByTagId(tagId);
                    if (equipment != null) {
                        if (equipment.getEquipType().equals("煎药机")) {
                            message.what = 0;
                            message.obj = equipment.getEquipId();
                            handler.sendMessage(message);
                        } else {
                            message.what = -1;
                            message.obj = "这是" + equipment.getEquipId() + "号" + equipment.getEquipType() + "标签";
                            handler.sendMessage(message);
                        }

                    } else {
                        message.what = -1;
                        message.obj = "标签有误";
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    public void getEquipmentList(final String url) {
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
                        getData((List) msg.obj);
                        break;
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                super.run();
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                try {
                    returnDomain = (ReturnDomain) (Http.post(url, pairs, ReturnDomain.class));
                    if (returnDomain.getSuccess()) {
                        List list = JSON.parseObject(returnDomain.getObject().toString(), List.class);
                        message.what = 0;
                        message.obj = list;
                        handler.sendMessage(message);
                    } else {
                        message.what = -1;
                        message.obj = returnDomain.getException();
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

}
