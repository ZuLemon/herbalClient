package net.andy.boiling.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.util.BoilingEffortUtil;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.dispensing.ui.LoadingUI;
import net.andy.dispensing.util.DatePickDialogUtil;
import net.andy.dispensing.util.PersonalEffortUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * 煎制工作量
 * Created by Guang on 2016/9/22.
 */

public class BoilingEffortUI extends Activity {
    private AppOption appOption = new AppOption();
    @ViewInject(R.id.boilingeffort_startTime_editText)
    private EditText boilingeffort_startTime_editText;
    @ViewInject(R.id.boilingeffort_endTime_editText)
    private EditText boilingeffort_endTime_editText;
    @ViewInject(R.id.boilingeffort_today_button)
    private Button boilingeffort_today_button;
    @ViewInject(R.id.boilingeffort_month_button)
    private Button boilingeffort_month_button;
    @ViewInject(R.id.boilingeffort_preMonth_button)
    private Button boilingeffort_preMonth_button;
    @ViewInject(R.id.boilingeffort_sockTotal_textView)
    private TextView boilingeffort_sockTotal_textView;
    @ViewInject(R.id.boilingeffort_sockPresNumTotal_textView)
    private TextView boilingeffort_sockPresNumTotal_textView;
    @ViewInject(R.id.boilingeffort_extractTotal_textView)
    private TextView boilingeffort_extractTotal_textView;
    @ViewInject(R.id.boilingeffort_extractPresNumTotal_textView)
    private TextView boilingeffort_extractPresNumTotal_textView;
    @ViewInject(R.id.boilingeffort_pasteTotal_textView)
    private TextView boilingeffort_pasteTotal_textView;
    @ViewInject(R.id.boilingeffort_pastePresNumTotal_textView)
    private TextView boilingeffort_pastePresNumTotal_textView;
    private Map effortMap;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boilingeffort);
        x.view().inject(this);
        getTime("month");
        setMonitor();
        PersonalThread();
    }


    private void setMonitor() {
        boilingeffort_startTime_editText.addTextChangedListener(textWatcher);
        boilingeffort_endTime_editText.addTextChangedListener(textWatcher);
    }
    @Event(value = {
            R.id.boilingeffort_startTime_editText,
            R.id.boilingeffort_endTime_editText,
            R.id.boilingeffort_today_button,
            R.id.boilingeffort_month_button,
            R.id.boilingeffort_preMonth_button
    },type = View.OnClickListener.class)
    private void onClick(View view) {
            switch (view.getId()) {
                case R.id.boilingeffort_startTime_editText:
                    DatePickDialogUtil startdateTimePicKDialog = new DatePickDialogUtil(
                            BoilingEffortUI.this,String.valueOf( boilingeffort_startTime_editText.getText()));
                    startdateTimePicKDialog.dateTimePicKDialog(boilingeffort_startTime_editText);
                     break;
                case R.id.boilingeffort_endTime_editText:
                    DatePickDialogUtil enddateTimePicKDialog = new DatePickDialogUtil(
                            BoilingEffortUI.this, String.valueOf( boilingeffort_endTime_editText.getText()));
                    enddateTimePicKDialog.dateTimePicKDialog(boilingeffort_endTime_editText);
                    break;
                case R.id.boilingeffort_today_button:
                    getTime("today");
                    startActivity(new Intent(BoilingEffortUI.this,LoadingUI.class));
                    PersonalThread();
                    break;
                case R.id.boilingeffort_month_button:
                    getTime("month");
                    startActivity(new Intent(BoilingEffortUI.this,LoadingUI.class));
                    PersonalThread();
                    break;
                case R.id.boilingeffort_preMonth_button:
                    getTime("preMonth");
                    startActivity(new Intent(BoilingEffortUI.this,LoadingUI.class));
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
                    effortMap=new BoilingEffortUtil().getBoilingEffortByUserIdAndTime(String.valueOf(Application.getUsers().getId()),String.valueOf(boilingeffort_startTime_editText.getText()),String.valueOf(boilingeffort_endTime_editText.getText()));
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
            boilingeffort_endTime_editText.setText(dateFormat.format(cal_1.getTime()));
            cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为上月第一天
            boilingeffort_startTime_editText.setText(dateFormat.format(cal_1.getTime()));
        }else if("month".equals(sign)){
            cal_1.add(Calendar.MONTH, 0);
            cal_1.set(Calendar.DAY_OF_MONTH,1);
            boilingeffort_startTime_editText.setText(dateFormat.format(cal_1.getTime()));
            cal_1.set(Calendar.DAY_OF_MONTH, cal_1.getActualMaximum(Calendar.DAY_OF_MONTH));
            boilingeffort_endTime_editText.setText(dateFormat.format(cal_1.getTime()));
        }else if("today".equals(sign)){
            boilingeffort_startTime_editText.setText(dateFormat.format(cal_1.getTime()));
            boilingeffort_endTime_editText.setText(dateFormat.format(cal_1.getTime()));
        }
    }
    private void setView() {
        boilingeffort_sockTotal_textView.setText(String.valueOf(effortMap.get("sockct")));
        boilingeffort_sockPresNumTotal_textView.setText(String.valueOf(effortMap.get("sockpt")));
        boilingeffort_extractTotal_textView.setText(String.valueOf(effortMap.get("extractct")));
        boilingeffort_extractPresNumTotal_textView.setText(String.valueOf(effortMap.get("extractpt")));
        boilingeffort_pasteTotal_textView.setText(String.valueOf(effortMap.get("pastect")));
        boilingeffort_pastePresNumTotal_textView.setText(String.valueOf(effortMap.get("pastept")));
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
