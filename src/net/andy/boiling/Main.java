package net.andy.boiling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import net.andy.boiling.domain.PermissionDomain;
import net.andy.boiling.util.PermissionUtil;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.util.OnlineUtil;
import net.andy.dispensing.util.RuleUtil;
import net.andy.dispensing.util.StationUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Activity {
    private List<PermissionDomain> permissionDomainList;
    private PermissionDomain permissionDomain;
    @ViewInject(R.id.main_GridView)
    private GridView main_GridView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> listMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Init(1);
        x.view().inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    //定义点击事件监听器
    @Event(value = R.id.main_GridView, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        Intent intent = null;
        try {
            intent = new Intent(Main.this, Class.forName(permissionDomainList.get(position).getModule()));
            Log.e("Class", String.valueOf(Class.forName(permissionDomainList.get(position).getModule())));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            new CoolToast(getBaseContext()).show("未找到UI：" + permissionDomainList.get(position).getModule());
        }
        HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
        String itemName = (String) item.get("name");
        if ("退出".equals(itemName)) {
            offlineThread();
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_CALL:
                return true;
            case KeyEvent.KEYCODE_SYM:
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
            case KeyEvent.KEYCODE_STAR:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止轮询服务
        Log.i("MainActivity", "Stop polling service...");
    }

    private int getImageInt(String imagename) {
        Class drawable = R.drawable.class;
        Field field = null;
        int res_ID = 0;
        try {
            field = drawable.getField(imagename);
            res_ID = field.getInt(field.getName());
            return res_ID;
        } catch (Exception e) {
        }
        return 0;
    }

    private void setView() {
        //生成动态数组，并且转入数据
        listMain = new ArrayList<HashMap<String, Object>>();
        for (PermissionDomain per : permissionDomainList) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("iconCls", getImageInt((String) per.getIconCls()));//添加图像资源的ID
            map.put("name", per.getName());
            listMain.add(map);
        }
        adapter = new SimpleAdapter(this, //数据来源
                listMain, R.layout.gridview_item,//XML实现
                new String[]{"iconCls", "name"}, //动态数组与ImageItem对应的子项
                new int[]{R.id.gridview_imageview, R.id.gridview_textview}//  //ImageItem的XML文件里面的一个ImageView,两个TextView ID
        );
        main_GridView.setAdapter(adapter);
//       adapter.notifyDataSetChanged();
    }

    //ListMap 转为 List<Bean>
    private void parseObject(List listPermission) {
        permissionDomainList = new ArrayList<PermissionDomain>();
        for (Object obj : listPermission) {
            permissionDomain = new PermissionDomain();
            permissionDomain.setName((String) ((Map) obj).get("name"));
            permissionDomain.setModule((String) ((Map) obj).get("module"));
            permissionDomain.setIconCls((String) ((Map) obj).get("iconCls"));
            permissionDomainList.add(permissionDomain);
        }
    }

    public void Init(final int code) {
        final Message message = new Message();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                    case 0:
                        parseObject((List) msg.obj);
                        setView();
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    message.obj = new PermissionUtil().getPermissionByuserId(new AppOption().getOption(AppOption.APP_OPTION_USER));
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private void offlineThread() {
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
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    message.what = 0;
                    message.obj = new OnlineUtil().offline(Integer.parseInt(new AppOption().getOption(AppOption.APP_OPTION_USER)));
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
