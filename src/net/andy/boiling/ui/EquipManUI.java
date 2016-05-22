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
import net.andy.boiling.R;
import net.andy.boiling.domain.EquipmentDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.util.EquipmentUtil;
import net.andy.com.CoolToast;
import net.andy.com.NFCActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EquipManUI
 *
 * @author RongGuang
 * @date 2015/12/2
 */
public class EquipManUI extends NFCActivity {
    private ReturnDomain returnDomain = new ReturnDomain ();
    private ListView equip_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.equipman );
        equip_listView = ( ListView ) findViewById ( R.id.equipman_list_listView );
        Button equip_add_button = ( Button ) findViewById ( R.id.equipman_add_button );
        equip_listView.setOnItemClickListener ( new OnListItemClick () );
        equip_add_button.setOnClickListener ( new OnAddClick () );
        getEquipmentList ( "equipment/getEquipmentOfAll.do" );
    }

    public class OnAddClick implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent ();
            intent.setClass ( EquipManUI.this, EquipReviseUI.class );
            startActivity ( intent );
        }
    }

    /*    监听ListView      */
    public class OnListItemClick implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
            openReturnUI ( String.valueOf ( map.get ( "equip_equipId_textView" ) ) );
            //回传值
            Intent intent = new Intent ();
            Bundle bundle = new Bundle ();
            bundle.putString ( "id", String.valueOf ( map.get ( "equip_id_textView" ) ) );
            intent.putExtras ( bundle );
            intent.setClass ( EquipManUI.this, EquipReviseUI.class );
            EquipManUI.this.startActivity ( intent );
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent ( intent );
        getEquipment ( getNfc ().readID ( intent ) );
    }

    public void openReturnUI(String equipId) {
        Intent intent = new Intent ( this, TakeUI.class );
        intent.putExtra ( "equipId", equipId );
        setResult ( 0, intent );
//        finish ();
    }

    public void getData(List equipList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
        for (Object obj : equipList) {
            Map<String, Object> map = new HashMap<String, Object> ();
            map.put ( "equip_equipId_textView", ( ( Map ) obj ).get ( "equipId" ) );
            map.put ( "equip_equipType1_textView", ( ( Map ) obj ).get ( "equipType1" ) );
            map.put ( "equip_equipName_textView", ( ( Map ) obj ).get ( "equipName" ) );
            map.put ( "equip_tagId_textView", ( ( Map ) obj ).get ( "tagId" ) );
            map.put ( "equip_id_textView", ( ( Map ) obj ).get ( "id" ) );
            list.add ( map );
        }
        SimpleAdapter adapter = new SimpleAdapter ( this, list, R.layout.equipmanlist,
                new String[]{"equip_equipId_textView", "equip_equipType1_textView",
                        "equip_equipName_textView", "equip_tagId_textView", "equip_id_textView"},
                new int[]{R.id.equip_equipId_textView, R.id.equip_equipType1_textView,
                        R.id.equip_equipName_textView, R.id.equip_tagId_textView, R.id.equip_id_textView} );
        equip_listView.setAdapter ( adapter );
    }

    public void getEquipment(final String tagId) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        openReturnUI ( ( String ) msg.obj );
                        break;
                }
            }
        };
        new Thread () {
            @Override
            public void run() {
                super.run ();
                try {
                    EquipmentDomain equipment = new EquipmentUtil ().getEquipByTagId ( tagId );
                    if ( equipment != null ) {
                        if ( equipment.getEquipType ().equals ( "煎药机" ) ) {
                            message.what = 0;
                            message.obj = equipment.getEquipId ();
                            handler.sendMessage ( message );
                        } else {
                            message.what = -1;
                            message.obj = "这是" + equipment.getEquipId () + "号" + equipment.getEquipType () + "标签";
                            handler.sendMessage ( message );
                        }
                    } else {
                        message.what = -1;
                        message.obj = "标签有误";
                        handler.sendMessage ( message );
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }

    public void getEquipmentList(final String url) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        getData ( ( List ) msg.obj );
                        break;
                }
            }
        };

        new Thread () {
            @Override
            public void run() {
                super.run ();
                try {
                    List list = new EquipmentUtil ().getAllEquip ();
                    message.what = 0;
                    message.obj = list;
                    handler.sendMessage ( message );
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }

            }
        }.start ();
    }

}
