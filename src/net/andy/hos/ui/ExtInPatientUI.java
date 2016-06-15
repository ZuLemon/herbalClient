package net.andy.hos.ui;

import android.app.Activity;
import android.content.Context;
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
import net.andy.hos.util.ExtInpatientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 16-六月-8.
 */
public class ExtInPatientUI extends Activity{
    private SimpleAdapter adapter;
    private String deptId;
    private TextView deptName;
    private EditText patName;
    private ListView patList;
    private Button search;
    private List list=new ArrayList();
    private ContentAdapter contentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ext_inpatient);
        deptName= (TextView) findViewById(R.id.deptName);
        patName= (EditText) findViewById(R.id.patName);
        patList= (ListView) findViewById(R.id.patList);
        search= (Button) findViewById(R.id.search);
        search.setOnClickListener(new ButtonListerner());
        deptName.setOnClickListener(new ButtonListerner());
        contentAdapter=new ContentAdapter(this);
        patList.setAdapter(contentAdapter);
    }
    private class ButtonListerner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.deptName:
                    Intent intn = new Intent(ExtInPatientUI.this,ExtInPatientDeptUI.class);
                    startActivityForResult(intn,0);
                    break;
                case R.id.search:
                    HospitalThread(0);
                    break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Bundle b=data.getExtras();
                deptId=b.getString("id");//str即为回传的值
                deptName.setText(b.getString("name"));
                break;
            default:
                break;
        }
    }
    public void getData() {
        adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.ext_inpatient_item,
                new String[]{"病人姓名"}, new int[]{R.id.content});
//        dept.setOnItemClickListener(new ListItemClick());
        patList.setAdapter(adapter);
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
//                        getData();
                        contentAdapter.notifyDataSetChanged();
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
                            list=new ExtInpatientUtil().getPatient(String.valueOf(patName.getText()),deptId);
                            message.what = 0;
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
    private class ContentAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ContentView contentView;

        public ContentAdapter(Context context) {
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
            if (view == null) {
                view = inflater.inflate(R.layout.ext_inpatient_item, viewGroup, false);
                contentView = new ContentView();
                contentView.id  = (TextView) view.findViewById(R.id.id);
                contentView.content= (TextView) view.findViewById(R.id.content);
                view.setTag(contentView);
            } else {
                contentView = (ContentView) view.getTag();
            }
            Map map = (Map) list.get(i);
            Log.e("map", map.toString());
            contentView.id.setText("");
            contentView.content.setText(""+map.get("病人姓名")+"  "+map.get("病人性别")+"  "+map.get("病人年龄")+"岁  "+map.get("病人民族")+"  \n"+map.get("当前所在护理单元")+"  "+map.get("当前所在床位")+"床");
            return view;
        }
        private class ContentView {
            private TextView id;
            private TextView content;
        }
    }
}
