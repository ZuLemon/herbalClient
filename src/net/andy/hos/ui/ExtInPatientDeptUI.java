package net.andy.hos.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.hos.util.ExtInpatientUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 16-六月-8.
 */
public class ExtInPatientDeptUI extends Activity {
    @ViewInject(R.id.dept)
    private ListView dept;
    private SimpleAdapter adapter;
    private List list=new ArrayList();
    public ExtInPatientDeptUI() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ext_inpatient_dept);
        x.view().inject(this);
        HospitalThread(0);
    }

    public void getData(List list) {
        adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.ext_inpatient_item,
                new String[]{"当前所在护理单元id", "当前所在护理单元"}, new int[]{R.id.id, R.id.content});
//        dept.setOnItemClickListener(new ListItemClick());
        dept.setAdapter(adapter);
    }

    /*    监听ListView      */
    @Event(value = R.id.dept,type = AdapterView.OnItemClickListener.class)
    private void deptItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
            Intent intent2 = new Intent();
            intent2.putExtra("id", (String) map.get("当前所在护理单元id"));
            intent2.putExtra("name", (String) map.get("当前所在护理单元"));
            ExtInPatientDeptUI.this.setResult(RESULT_OK, intent2);
            ExtInPatientDeptUI.this.finish();
//            Map map = (Map) userList.get(position);
//            dialog(Integer.parseInt(String.valueOf(map.get("id"))),map.get("name")+"");
    }
    private void HospitalThread(int what) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        break;
                    case 0:
                        getData(list);
                        break;
                    case 1:
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
                            message.what = 0;
                            list = new ExtInpatientUtil().getExtDept();
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
