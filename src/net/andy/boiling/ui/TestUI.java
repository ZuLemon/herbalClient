package net.andy.boiling.ui;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import junit.framework.Test;
import net.andy.boiling.R;
import net.andy.boiling.domain.PermissionDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.domain.TagDomain;
import net.andy.boiling.util.PermissionUtil;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.com.Http;
import net.andy.com.NFCActivity;
import net.andy.dispensing.domain.RulesDomain;
import net.andy.dispensing.ui.StationRuleUI;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015-11-26.
 * 测试
 */
public class TestUI extends NFCActivity {
    private TagDomain tagDomain;
    private ReturnDomain returnDomain;
    private EditText tag_tagId_editText;
    private EditText tag_code_editText;
    private EditText tag_bindId_editText;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    List imagelist=new ArrayList<>();
    private GridView gridView;
    private Map listmap1;
    private Map listmap2;
    private List<Map> mapList;
    private List<PermissionDomain> permissionDomainList;
    private PermissionDomain permissionDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Init(1);
//        listmap1=new HashMap<>();
//        listmap2=new HashMap<>();
//        mapList=new ArrayList<>();
//        listmap1.put("id",1);
//        listmap1.put("name","1号");
//        listmap1.put("image","equip");
//        listmap1.put("activity","net.andy.dispensing.ui.AdjustUI");
//        listmap2.put("id",2);
//        listmap2.put("name","2号");
//        listmap2.put("image","equip");
//        listmap2.put("activity","net.andy.dispensing.ui.AdjustUI");
//        mapList.add(listmap1);
//        mapList.add(listmap2);

//        image= new int[]{res_ID, R.drawable.take, R.drawable.extract,
//                R.drawable.unknown, R.drawable.unknown, R.drawable.unknown,
//                R.drawable.unknown, R.drawable.adjust, R.drawable.exit,
//                R.drawable.revise, R.drawable.equip, R.drawable.tag};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gridView = (GridView)findViewById(R.id.main_GridView);
    }
    private int getImageInt(String imagename){
        Class drawable = R.drawable.class;
        Field field = null;
        int res_ID = 0;
        try {
            field = drawable.getField(imagename);
            res_ID = field.getInt(field.getName());
            return res_ID;
//            mView.setImageResource(res_ID);
        } catch (Exception e) {}
        return 0;
    }
    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    private AdapterView.OnItemClickListener clickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0,
                                View arg1,
                                int arg2,
                                long arg3
        )
        {

            for(int i=0;i<permissionDomainList.size();i++) {
                if(i==arg2) {
                    Intent intent = null;
                    try {
                        intent = new Intent(TestUI.this, Class.forName(permissionDomainList.get(i).getModule()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            }
        }
    };
    private void setView(){
        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>>  list=new ArrayList<HashMap<String,Object>>();
        for(PermissionDomain per:permissionDomainList){
            HashMap<String, Object>  map = new HashMap<String, Object>();
            map.put("iconCls", getImageInt((String) per.getIconCls()));//添加图像资源的ID
            map.put("name",per.getName());
            list.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(this, //数据来源
                list,R.layout.gridview_item,//XML实现
                new String[]{"iconCls","name"}, //动态数组与ImageItem对应的子项
                new int[]{R.id.gridview_imageview,R.id.gridview_textview}//  //ImageItem的XML文件里面的一个ImageView,两个TextView ID
        );
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(clickListener);
    }
    //ListMap 转为 List<Bean>
    private void parseObject(List listPermission) {
        permissionDomainList=new ArrayList<PermissionDomain>();
        for (Object obj : listPermission) {
            permissionDomain=new PermissionDomain();
            permissionDomain.setName((String)((Map)obj).get("name"));
            permissionDomain.setModule((String)((Map)obj).get("module"));
            permissionDomain.setIconCls((String)((Map)obj).get("iconCls"));
            permissionDomainList.add(permissionDomain);
        }
    }
    public void Init(final int code) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        parseObject((List) msg.obj);
                        setView ();
                        break;
                }
            }
        };
        new Thread ( ) {
            @Override
            public void run() {
                try {
                    message.obj=new PermissionUtil().getPermissionByuserId(new AppOption().getOption(AppOption.APP_OPTION_USER));
                    message.what = 0;
                    handler.sendMessage ( message );
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test);
//        tag_tagId_editText = (EditText) findViewById(R.id.tag_tagId_editText);
//        tag_code_editText = (EditText) findViewById(R.id.tag_code_editText);
//        tag_bindId_editText = (EditText) findViewById(R.id.tag_bindId_editText);
//
//        Button tag_ok_button = (Button) findViewById(R.id.tag_ok_button);
//        tag_ok_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(TestUI.this, MipcaActivityCapture.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//            }
//        });
//    }
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
////        getTag(this.getNfc().readID(intent));
//        tag_tagId_editText.setText(this.getNfc().readID(intent));
//        Intent intentEquip = new Intent();
//        intentEquip.setClass(TestUI.this, EquipmentUI.class);
//        startActivityForResult(intentEquip, 1001);
//    }
//    /*   接收回传值         */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        switch (requestCode) {
//            case SCANNIN_GREQUEST_CODE:
//                if(resultCode == RESULT_OK){
//                    Bundle bundle = data.getExtras();
//                    //显示扫描到的内容
//                    tag_code_editText.setText(bundle.getString("result"));
//                }
//                break;
//        }
////        System.out.println("$$"+data.getStringExtra("equipId"));
////        super.onActivityResult(requestCode, resultCode, data);
////        if(requestCode == 1001)
////        {
////
////            String result_value = data.getStringExtra("equipId");
////            System.out.println(result_value);
////            tag_code_editText.setText(result_value);
////        }
//    }
//    public void reset(){
//        tag_tagId_editText.setText("");
//        tag_code_editText.setText("");
//        tag_bindId_editText.setText("");
//    }
//
//    public void setValue(String tagId){
//            tag_tagId_editText.setText(tagId);
//            tag_code_editText.setText(tagDomain.getCode());
//            tag_bindId_editText.setText(tagDomain.getBindId());
//    }
//
//    public String getType(){
//        return "";
//    }
//
//    public String getStatus(){
//
//        return "";
//    }
//    public void getTag(final String tagId){
//        final Message message = new Message();
//        final Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case -1:
//                        reset();
//                        new CoolToast(getBaseContext()).show((String) msg.obj);
//                        break;
//                    case 0:
//                        setValue(tagId);
//                        break;
//                }
//            }
//        };
//        new Thread(tagId) {
//            @Override
//            public void run() {
//                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//                pairs.add(new BasicNameValuePair("tagId", tagId));
//
//                try {
//                    returnDomain = (ReturnDomain)(Http.post("tag/getTagByTagId.do", pairs,ReturnDomain.class));
//                    if (returnDomain.getSuccess()) {
//                        tagDomain = JSON.parseObject(returnDomain.getObject().toString(), TagDomain.class);
//                        message.what = 0;
//                        handler.sendMessage(message);
//                    } else {
//                        message.what = -1;
//                        message.obj = returnDomain.getException();
//                        handler.sendMessage(message);
//                    }
//                } catch (Exception e) {
//                    message.what = -1;
//                    message.obj = e.getMessage();
//                    handler.sendMessage(message);
//                }
//            }
//        }.start();
//    }
//    private class Submit implements Button.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            if (tagDomain.getId() == null) return;
//
//            final Message message = new Message();
//            final Handler handler = new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    reset();
//                    new CoolToast(getBaseContext()).show((String) msg.obj);
//                }
//            };

//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//
//                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//                    pairs.add(new BasicNameValuePair("id",String.valueOf(tagDomain.getId())));
//                    pairs.add(new BasicNameValuePair("tagId",String.valueOf(tag_tagId_editText.getText())));
//                    pairs.add(new BasicNameValuePair("code", String.valueOf(tag_code_editText.getText())));
//                    pairs.add(new BasicNameValuePair("type",getType()));
//                    pairs.add(new BasicNameValuePair("status",getStatus()));
//                    pairs.add(new BasicNameValuePair("bindId", String.valueOf(tag_bindId_editText.getText())));
//
//                    try {
//                        returnDomain = (ReturnDomain) Http.post(tagDomain.getId()==0?"tag/insert.do":"tag/update.do",pairs,ReturnDomain.class);
//
//                        message.obj = "保存成功";
//                        handler.sendMessage(message);
//                    } catch (Exception e) {
//                        message.obj = e.getMessage();
//                        handler.sendMessage(message);
//                    }
//                }
//            }.start();
//        }
//    }

}
