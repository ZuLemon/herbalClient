package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.RulesDomain;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.util.*;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 个人工作量
 * Created by Guang on 2016/6/6.
 */

public class PersonalEffortUI extends Activity {
    private AppOption appOption = new AppOption();
    @ViewInject(R.id.personaleffort_startTime_editText)
    private EditText personaleffort_startTime_editText;
    @ViewInject(R.id.personaleffort_endTime_editText)
    private EditText personaleffort_endTime_editText;
    @ViewInject(R.id.personaleffort_today_button)
    private Button personaleffort_today_button;
    @ViewInject(R.id.personaleffort_month_button)
    private Button personaleffort_month_button;
    @ViewInject(R.id.personaleffort_preMonth_button)
    private Button personaleffort_preMonth_button;
    @ViewInject(R.id.personaleffort_presNumTotal_textView)
    private TextView personaleffort_presNumTotal_textView;
    @ViewInject(R.id.personaleffort_presTotal_textView)
    private TextView personaleffort_presTotal_textView;
    private Map effortMap;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personaleffort);
        x.view().inject(this);
        getTime("month");
        setMonitor();
        PersonalThread();
    }


    private void setMonitor() {
        personaleffort_startTime_editText.addTextChangedListener(textWatcher);
        personaleffort_endTime_editText.addTextChangedListener(textWatcher);
    }
    @Event(value = {
            R.id.personaleffort_startTime_editText,
            R.id.personaleffort_endTime_editText,
            R.id.personaleffort_today_button,
            R.id.personaleffort_month_button,
            R.id.personaleffort_preMonth_button
    },type = View.OnClickListener.class)
    private void onClick(View view) {
            switch (view.getId()) {
                case R.id.personaleffort_startTime_editText:
                    DateTimePickDialogUtil startdateTimePicKDialog = new DateTimePickDialogUtil(
                            PersonalEffortUI.this,String.valueOf( personaleffort_startTime_editText.getText()));
                    startdateTimePicKDialog.dateTimePicKDialog(personaleffort_startTime_editText);
                     break;
                case R.id.personaleffort_endTime_editText:
                    DateTimePickDialogUtil enddateTimePicKDialog = new DateTimePickDialogUtil(
                            PersonalEffortUI.this, String.valueOf( personaleffort_endTime_editText.getText()));
                    enddateTimePicKDialog.dateTimePicKDialog(personaleffort_endTime_editText);
                    break;
                case R.id.personaleffort_today_button:
                    getTime("today");
                    startActivity(new Intent(PersonalEffortUI.this,LoadingUI.class));
                    PersonalThread();
                    break;
                case R.id.personaleffort_month_button:
                    getTime("month");
                    startActivity(new Intent(PersonalEffortUI.this,LoadingUI.class));
                    PersonalThread();
                    break;
                case R.id.personaleffort_preMonth_button:
                    getTime("preMonth");
                    startActivity(new Intent(PersonalEffortUI.this,LoadingUI.class));
                    PersonalThread();
                    break;
            }
    }


    private void PersonalThread() {
        final Message message = new Message();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        LoadingUI.instance.finish();
                        setView();
                        break;
                    case -1:
                        LoadingUI.instance.finish();
                        break;
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    effortMap=new PersonalEffortUtil().getEffortByUserIdAndTime(String.valueOf(Application.getUsers().getId()),String.valueOf(personaleffort_startTime_editText.getText()),String.valueOf(personaleffort_endTime_editText.getText()));
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
    private void getTime(String sign){
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        if("preMonth".equals(sign)){
            cal_1.set(Calendar.DAY_OF_MONTH,0);//上月最后一天
            personaleffort_endTime_editText.setText(dateFormat.format(cal_1.getTime()));
            cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为上月第一天
            personaleffort_startTime_editText.setText(dateFormat.format(cal_1.getTime()));
        }else if("month".equals(sign)){
            cal_1.add(Calendar.MONTH, 0);
            cal_1.set(Calendar.DAY_OF_MONTH,1);
            personaleffort_startTime_editText.setText(dateFormat.format(cal_1.getTime()));
            cal_1.set(Calendar.DAY_OF_MONTH, cal_1.getActualMaximum(Calendar.DAY_OF_MONTH));
            personaleffort_endTime_editText.setText(dateFormat.format(cal_1.getTime()));
        }else if("today".equals(sign)){
            personaleffort_startTime_editText.setText(dateFormat.format(cal_1.getTime()));
            personaleffort_endTime_editText.setText(dateFormat.format(cal_1.getTime()));
        }
    }
    private void setView() {
        personaleffort_presNumTotal_textView.setText(String.valueOf(effortMap.get("pt")));
        personaleffort_presTotal_textView.setText(String.valueOf(effortMap.get("ct")));
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            PersonalThread();
        }
    };
}
