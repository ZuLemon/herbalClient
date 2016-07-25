package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.domain.PrescriptionDomain;
import net.andy.boiling.util.PrescriptionUtil;
import net.andy.boiling.util.UserUtil;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.UrgentDelPresUtil;
import org.w3c.dom.Text;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 置顶、删除、修改处方属性
 * Created by Guang on 2016/3/16.
 */
public class UrgDelPresUI extends Activity{
    @ViewInject(R.id.urgdelpres_linearLayout)
    private LinearLayout urgdelpres_linearLayout;
    @ViewInject(R.id.urgdelpres_urgpres_textView)
    private TextView urgdelpres_urgpres_textView;
    @ViewInject(R.id.urgdelpres_delpres_textView)
    private TextView urgdelpres_delpres_textView;
    @ViewInject(R.id.urgdelpres_noProcess_textView)
    private TextView urgdelpres_noProcess_textView;
    @ViewInject(R.id.urgdelpres_process_textView)
    private TextView urgdelpres_process_textView;
    private String process="";
    private String passwd;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urgdelpres);
        x.view().inject(this);
        Intent in=getIntent();
        id=in.getStringExtra("id");
        Log.e("id",id);
    }
    @Event(value = {
            R.id.urgdelpres_linearLayout,
            R.id.urgdelpres_urgpres_textView,
            R.id.urgdelpres_noProcess_textView,
            R.id.urgdelpres_process_textView,
            R.id.urgdelpres_delpres_textView
    },type = View.OnClickListener.class)
   private void btnClick(View v) {
            switch (v.getId()){
                case R.id.urgdelpres_linearLayout:
                    finish();
                    break;
                case R.id.urgdelpres_urgpres_textView:
                    urgentPresThread(0);
                    break;
                case R.id.urgdelpres_noProcess_textView:
                    process="自煎";
                    urgentPresThread(3);
                    break;
                case R.id.urgdelpres_process_textView:
                    process="代煎";
                    urgentPresThread(3);
                    break;
                case R.id.urgdelpres_delpres_textView:
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_passwd,
                            (ViewGroup) findViewById(R.id.dialog_passwd));
                    EditText passwdEditText= (EditText) layout.findViewById(R.id.passwd);
                    new AlertDialog.Builder(UrgDelPresUI.this).setTitle("请输入密码").setView(layout)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            passwd=passwdEditText.getText().toString();
                            if("".equals(passwd)){
                                new CoolToast(getBaseContext()).show("密码不能为空");
                            }else {
                                urgentPresThread(2);
                            }
                        }
                    }) .setNegativeButton("取消", null).show();
                    break;
        }
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
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        finish();
                        break;
                    case 1:
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        finish();
                        break;
                    case 2:
                      if("success".equals( msg.obj )){
                          urgentPresThread(1);
                          finish();
                      }else{
                          new CoolToast( getBaseContext () ).show ( "密码错误" );
                      }
                        break;
                    case 3:
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        finish();
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
                            message.obj = new UrgentDelPresUtil().setUrgent(Integer.parseInt(id),"02");
                            handler.sendMessage ( message );
                            break;
                        case 1:
                            message.what = 1;
                            message.obj = new UrgentDelPresUtil().delPres(Integer.parseInt(id));
                            handler.sendMessage ( message );
                            break;
                        case 2:
                            message.what = 2;
                            message.obj = new UserUtil().confirmPasswd(String.valueOf(Application.getUsers().getId()),passwd);
                            handler.sendMessage ( message );
                            break;
                        case 3:
                            message.what = 3;
                            message.obj = new PrescriptionUtil().setProcess(Integer.parseInt(id),process);
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
