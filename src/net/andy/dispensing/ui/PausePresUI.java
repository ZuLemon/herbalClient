package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.boiling.util.PrescriptionUtil;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.com.NFCActivity;
import net.andy.dispensing.domain.DispensingDetailDomain;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.util.ReplenishUtil;
import net.andy.dispensing.util.StationUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 暂停处方
 * Created by Guang on 2016/4/20.
 */
public class PausePresUI extends NFCActivity {
    private Integer disId;
    private String tagId;
    private String sign;
    @ViewInject(R.id.pauselinearLayout)
    private LinearLayout pauselinearLayout;
    @ViewInject(R.id.pause_bindingTag_button)
    private Button pause_bindingTag_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause_pres);
        x.view().inject(this);
        Intent intent = getIntent();
        sign=intent.getStringExtra("sign");
        if("set".equals(sign)) {
            pause_bindingTag_button.setText("绑定标签");
            disId = intent.getIntExtra("disId", 0);
            Log.e("disId", "" + disId);
        }else if("get".equals(sign)){
            pause_bindingTag_button.setText("读取暂停处方标签");
        }
    }
    @Event(R.id.pauselinearLayout)
    private void btnClick(View view) {
        finish();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tagId = getNfc().readID(intent);
        if("set".equals(sign)) {
            Log.e("tagId", "" + tagId);
            Pause();
        }else if("get".equals(sign)){
            Intent il = new Intent();
            il.putExtra("tagId",tagId);
            setResult(RESULT_OK, il);
            finish();
        }
    }
    public void Pause() {
        final Message message = new Message();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case -1:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                    case 0:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    message.obj=new PrescriptionUtil().pause(disId,tagId);
//                    message.obj="暂停成功";
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

}
