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
public class SysAdminUI extends Activity {
    private LinearLayout sysadmin_extract_linearLayout;
    private LinearLayout sysadmin_topPre_linearLayout;
    private LinearLayout sysadmin_online_linearLayout;
    private LinearLayout sysadmin_assignWork_linearLayout;
    private LinearLayout sysadmin_waitDispen_linearLayout;
    private StationDomain stationDomain;
    private boolean isInterval;
    private AppOption appOption = new AppOption();
    private ButtonListener buttonListener=new ButtonListener();
    private CoolToast coolToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sysadmin);
        coolToast = new CoolToast(getBaseContext());
        sysadmin_extract_linearLayout = (LinearLayout) findViewById(R.id.sysadmin_extract_linearLayout);
        sysadmin_topPre_linearLayout= (LinearLayout) findViewById(R.id.sysadmin_topPre_linearLayout);
        sysadmin_online_linearLayout= (LinearLayout) findViewById(R.id.sysadmin_online_linearLayout);
        sysadmin_waitDispen_linearLayout= (LinearLayout) findViewById(R.id.sysadmin_waitDispen_linearLayout);
        sysadmin_assignWork_linearLayout= (LinearLayout) findViewById(R.id.sysadmin_assignWork_linearLayout);
        buttonListener = new ButtonListener();
        stationDomain = new StationDomain();
        init();
        setMonitor();
//        replenishController();
    }

    private void init() {
    }

    private void setMonitor() {
        sysadmin_extract_linearLayout.setOnClickListener(buttonListener);
        sysadmin_topPre_linearLayout.setOnClickListener(buttonListener);
        sysadmin_online_linearLayout.setOnClickListener(buttonListener);
        sysadmin_waitDispen_linearLayout.setOnClickListener(buttonListener);
        sysadmin_assignWork_linearLayout.setOnClickListener(buttonListener);
    }




    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.sysadmin_extract_linearLayout:
                    Intent intent = new Intent(SysAdminUI.this, ExtractPresUI.class);
                    startActivity(intent);
                    break;
                case R.id.sysadmin_topPre_linearLayout:
                    Intent topPreintent = new Intent(SysAdminUI.this, UrgentPresUI.class);
                    startActivity(topPreintent);
                    break;
                case R.id.sysadmin_online_linearLayout:
                    Intent onlineintent = new Intent(SysAdminUI.this, OnlineUI.class);
                    startActivity(onlineintent);
                    break;
                case R.id.sysadmin_waitDispen_linearLayout:
                    Intent waitDispen = new Intent(SysAdminUI.this, WaitDispenUI.class);
                    startActivity(waitDispen);
                    break;
                case R.id.sysadmin_assignWork_linearLayout:
                    Intent assignWork = new Intent(SysAdminUI.this, AssignOtherWayUI.class);
                    startActivity(assignWork);
                    break;
                default:
                    break;
            }
        }
    }

}
