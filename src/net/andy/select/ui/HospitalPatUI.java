package net.andy.select.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import net.andy.dispensing.ui.ValidationUI;
import net.andy.dispensing.util.UrgentPresUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/6/8.
 */
public class HospitalPatUI extends Activity{
    private ListView hospitalpat_patientInfo_listView;
    private Button hospitalpat_search_button;
    private EditText hospitalpat_patientName_editText;
    private Integer Id;
    private List list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospitalpat);
        hospitalpat_patientInfo_listView= (ListView) findViewById(R.id.hospitalpat_patientInfo_listView);
        hospitalpat_search_button= (Button) findViewById(R.id.hospitalpat_search_button);
        hospitalpat_patientName_editText= (EditText) findViewById(R.id.hospitalpat_patientName_editText);
        hospitalpat_patientInfo_listView.setOnItemClickListener ( new ListItemClick() );
        hospitalpat_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
    /*    监听ListView      */
    public class ListItemClick implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );
//            dialog(Integer.parseInt(String.valueOf(map.get("urgentPres_id_textView"))),map.get("urgentPres_name_textView")+"");
        }
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
//                            message.obj = new UrgentPresUtil().getPrescriptionByPatientNo(urgentPres_patientId_editText.getText().toString().trim(), new AppOption().getOption(AppOption.APP_OPTION_USER));
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
    public class HospitalAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public HospitalAdapter(Context context) {
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
            final Val val;
            if (view == null) {
                view = inflater.inflate(R.layout.hospitalpatlist, viewGroup, false);
                val = new Val();
                val.hospitalpat_id = (TextView) view.findViewById(R.id.hospitalpat_id);
                val.hospitalpat_count = (TextView) view.findViewById(R.id.hospitalpat_count);
                val.hospitalpat_name = (TextView) view.findViewById(R.id.hospitalpat_name);
                val.hospitalpat_deptName = (TextView) view.findViewById(R.id.hospitalpat_deptName);
                val.hospitalpat_bedNo = (TextView) view.findViewById(R.id.hospitalpat_bedNo);
                view.setTag(val);
            } else {
                val = (Val) view.getTag();
            }
            Map map = (Map) list.get(i);
            Log.e("map", map.toString());
            val.hospitalpat_id .setText(String.valueOf(map.get("id")));
            val.hospitalpat_name .setText((CharSequence) map.get("dName"));
            val.hospitalpat_deptName .setText((CharSequence) map.get("dName"));
            val.hospitalpat_bedNo .setText((CharSequence) map.get("dName"));
            val.hospitalpat_count.setText(i+1+"");
            return view;
        }
        private class Val {
            private TextView hospitalpat_id;
            private TextView hospitalpat_count;
            private TextView hospitalpat_name;
            private TextView hospitalpat_deptName;
            private TextView hospitalpat_bedNo;
        }
    }
}
