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
import net.andy.dispensing.util.UrgentPresUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/5/18.
 */
public class UrgentPresUI extends Activity{
    private ListView urgentPres_patient_listView;
    private Button urgentPres_search_button;
    private EditText urgentPres_patientId_editText;
    private Integer Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urgentpres);
        urgentPres_patient_listView= (ListView) findViewById(R.id.urgentPres_patient_listView);
        urgentPres_search_button= (Button) findViewById(R.id.urgentPres_search_button);
        urgentPres_patientId_editText= (EditText) findViewById(R.id.urgentPres_patientId_editText);
        urgentPres_patient_listView.setOnItemClickListener ( new ListItemClick() );
        urgentPres_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urgentPresThread(0);
            }
        });
    }
    /*    监听ListView      */
    public class ListItemClick implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map map = ( Map ) ( parent.getItemAtPosition ( position ) );

            dialog(Integer.parseInt(String.valueOf(map.get("urgentPres_id_textView"))),map.get("urgentPres_name_textView")+"");
        }
    }
    protected void dialog(Integer id,String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UrgentPresUI.this);
        builder.setMessage("优先调剂"+name+"的处方？");  builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
                Id=id;
                urgentPresThread(1);
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
    public void setListView(List presList) {
        if(presList.size()==0){
            new CoolToast( getBaseContext () ).show ( "此卡号没有需调剂处方");
            return;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object obj : presList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put ( "urgentPres_classification_textView", ( ( Map ) obj ).get ( "classification" ) );
            map.put ( "urgentPres_presNumber_textView", ( ( Map ) obj ).get ( "presNumber" ) +"付");
            map.put ( "urgentPres_patientName_textView", ( ( Map ) obj ).get ( "patientName" ));
            map.put ( "urgentPres_category_textView", ( ( Map ) obj ).get ( "category" )+"："+( ( Map ) obj ).get ( "patientNo" ) );
            map.put ( "urgentPres_name_textView", ( ( Map ) obj ).get ( "patientName" ) );
            map.put ( "urgentPres_id_textView", ( ( Map ) obj ).get ( "id" ) );
            list.add ( map );
        }
        SimpleAdapter adapter = new SimpleAdapter ( this, list, R.layout.urgentpreslist,
                new String[]{"urgentPres_classification_textView", "urgentPres_presNumber_textView",
                "urgentPres_patientName_textView","urgentPres_category_textView", "urgentPres_id_textView","urgentPres_name_textView"},
                new int[]{R.id.urgentPres_classification_textView, R.id.urgentPres_presNumber_textView,
                        R.id.urgentPres_patientName_textView,R.id.urgentPres_category_textView, R.id.urgentPres_id_textView,R.id.urgentPres_name_textView} );
        urgentPres_patient_listView.setAdapter ( adapter );
    }
    private void urgentPresThread(int what) {
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
                            message.what = 0;
                            message.obj = new UrgentPresUtil().getPrescriptionByPatientNo(urgentPres_patientId_editText.getText().toString().trim(), new AppOption().getOption(AppOption.APP_OPTION_USER));
                            handler.sendMessage ( message );
                            break;
                        case 1:
                            message.what = 1;
                            message.obj = new UrgentPresUtil().setUrgent(Id,"02");
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
