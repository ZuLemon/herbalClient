package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.domain.PrescriptionDomain;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.UrgentDelPresUtil;

import java.util.List;

/**
 * Created by Guang on 2016/3/16.
 */
public class UrgDelPresUI extends Activity{
    private LinearLayout urgdelpres_linearLayout;
    private TextView urgdelpres_urgpres_textView;
    private TextView urgdelpres_delpres_textView;
    private ClickLis clickLis=new ClickLis();
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urgdelpres);
        urgdelpres_linearLayout= (LinearLayout) findViewById(R.id.urgdelpres_linearLayout);
        urgdelpres_urgpres_textView= (TextView) findViewById(R.id.urgdelpres_urgpres_textView);
        urgdelpres_delpres_textView= (TextView) findViewById(R.id.urgdelpres_delpres_textView);
        urgdelpres_linearLayout.setOnClickListener(clickLis);
        urgdelpres_urgpres_textView.setOnClickListener(clickLis);
        urgdelpres_delpres_textView.setOnClickListener(clickLis);
        Intent in=getIntent();
        id=in.getStringExtra("id");
        Log.e("id",id);
    }
    class ClickLis implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.urgdelpres_linearLayout:
                    finish();
                    break;
                case R.id.urgdelpres_urgpres_textView:
                    urgentPresThread(0);
                    Log.e("urgentPresThread",id);
                    break;
                case R.id.urgdelpres_delpres_textView:
                    urgentPresThread(1);
                    Log.e("urgentPresThread",id);
                    break;
            }
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
