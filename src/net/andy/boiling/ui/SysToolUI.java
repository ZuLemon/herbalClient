package net.andy.boiling.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.ui.*;

/**
 * 系统管理
 * Created by Guang on 2016/5/12.
 */
public class SysToolUI extends Activity {
    private LinearLayout sysadmin_extract_linearLayout;
    private LinearLayout sysadmin_online_linearLayout;
    private LinearLayout sysadmin_waitDispen_linearLayout;
    private LinearLayout sysadmin_selectPres_linearLayout;
    private TextView sysadmin_interval_textView;
    private EditText sysadmin_interval_editText;
    private StationDomain stationDomain;
    private boolean isInterval;
    private AppOption appOption = new AppOption();
    private ButtonListener buttonListener=new ButtonListener();
    private CoolToast coolToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.systool);
        coolToast = new CoolToast(getBaseContext());
        sysadmin_extract_linearLayout = (LinearLayout) findViewById(R.id.sysadmin_extract_linearLayout);
        sysadmin_online_linearLayout= (LinearLayout) findViewById(R.id.sysadmin_online_linearLayout);
        sysadmin_waitDispen_linearLayout= (LinearLayout) findViewById(R.id.sysadmin_waitDispen_linearLayout);
        sysadmin_selectPres_linearLayout= (LinearLayout) findViewById(R.id.sysadmin_selectPres_linearLayout);
        buttonListener = new ButtonListener();
        sysadmin_interval_textView = (TextView) findViewById(R.id.sysadmin_interval_textView);
        sysadmin_interval_editText = (EditText) findViewById(R.id.sysadmin_interval_editText);
        stationDomain = new StationDomain();
        init();
        setMonitor();
//        replenishController();
    }

    private void init() {
    }

    private void setMonitor() {
        sysadmin_interval_textView.setOnClickListener(buttonListener);
        sysadmin_extract_linearLayout.setOnClickListener(buttonListener);
        sysadmin_online_linearLayout.setOnClickListener(buttonListener);
        sysadmin_waitDispen_linearLayout.setOnClickListener(buttonListener);
        sysadmin_selectPres_linearLayout.setOnClickListener(buttonListener);
    }

    private void setInterval() {
        if (isInterval) {
            isInterval = false;
            if (Integer.parseInt(sysadmin_interval_editText.getText() + "") == 0) {
                sysadmin_interval_editText.setText("1");
                coolToast.show("间隔时间至少为 1");
            } else {
                coolToast.show("保存成功");
            }
            sysadmin_interval_editText.setFocusable(false);
            appOption.setOption(AppOption.APP_OPTION_WAITTIME, sysadmin_interval_editText.getText().toString());
            sysadmin_interval_textView.setText("修改");
        } else {
            isInterval = true;
            sysadmin_interval_editText.setFocusable(true);
            sysadmin_interval_editText.setFocusableInTouchMode(true);
            sysadmin_interval_editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            sysadmin_interval_textView.setText("保存");
        }
    }


    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.sysadmin_interval_textView:
                    setInterval();
                    break;
                case R.id.sysadmin_extract_linearLayout:
                    Intent intent = new Intent(SysToolUI.this, ExtractPresUI.class);
                    startActivity(intent);
                    break;
                case R.id.sysadmin_topPre_linearLayout:
                    Intent topPreintent = new Intent(SysToolUI.this, UrgentPresUI.class);
                    startActivity(topPreintent);
                    break;
                case R.id.sysadmin_online_linearLayout:
                    Intent onlineintent = new Intent(SysToolUI.this, OnlineUI.class);
                    startActivity(onlineintent);
                    break;
                case R.id.sysadmin_waitDispen_linearLayout:
                    Intent waitDispen = new Intent(SysToolUI.this, WaitDispenUI.class);
                    startActivity(waitDispen);
                    break;
                case R.id.sysadmin_selectPres_linearLayout:
                    Intent selDispen = new Intent(SysToolUI.this, SelectPresUI.class);
                    startActivity(selDispen);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 系统管理子线程
     **/
    private void settingThread(int what) {
        final Message message = new Message();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        new CoolToast(getBaseContext()).show((String) msg.obj);

                        break;
                    case 0:
                        break;
                    case 1:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    switch (what) {
                        case 0:
                            break;
                        case 1:
                            message.obj = 0;
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

}
