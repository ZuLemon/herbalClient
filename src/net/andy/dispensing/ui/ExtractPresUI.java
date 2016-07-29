package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.R;
import net.andy.com.CoolToast;
import net.andy.com.Http;
import net.andy.dispensing.util.DatePickDialogUtil;
import net.andy.dispensing.util.DateTimePickDialogUtil;
import net.andy.dispensing.util.ExtPreUtil;
import org.w3c.dom.Text;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 提取处方
 * Created by Guang on 2016/5/12.
 */
public class ExtractPresUI extends Activity{
    @ViewInject(R.id.extractpres_extPre_button)
    private Button extractpres_extPre_button;
    @ViewInject(R.id.extractpres_update_button)
    private Button extractpres_update_button;
    @ViewInject(R.id.extractpres_startTime_editText)
    private EditText extractpres_startTime_editText;
    @ViewInject(R.id.extractpres_endTime_editText)
    private EditText extractpres_endTime_editText;
    @ViewInject(R.id.extractpres_presId_editText)
    private EditText extractpres_presId_editText;
    @ViewInject(R.id.extractpres_description_textView)
    private TextView extractpres_description_textView;
    private Integer intevalId=0;
    private String beginTime;
    private String endTime;
    private boolean isClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extractpres);
        x.view().inject(this);
        Init();
    }
    private void Init(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");//可以方便地修改日期格式
        extractpres_startTime_editText.setText(dateFormat.format(now));
        extractpres_endTime_editText.setText(dateFormat.format(now));
    }
    @Event(value = {
        R.id.extractpres_update_button,
            R.id.extractpres_extPre_button,
            R.id.extractpres_startTime_editText,
            R.id.extractpres_endTime_editText
    },type = View.OnClickListener.class)
    private void btnClick(View view) {
            switch (view.getId()){
                case R.id.extractpres_update_button:
                    if(isClick){
                        startActivity(new Intent(ExtractPresUI.this,LoadingUI.class));
                        extractPresThread(2);
                    }else{
                        startActivity(new Intent(ExtractPresUI.this,LoadingUI.class));
                        extractPresThread(1);
                    }
                    break;
                case R.id.extractpres_extPre_button:
                    if("".equals(extractpres_presId_editText.getText().toString().trim())) {
                        if ("".equals(extractpres_startTime_editText.getText().toString().trim()) || "".equals(extractpres_endTime_editText.getText().toString().trim())) {
                            new CoolToast(getBaseContext()).show("时间不能为空！");
                            return;
                        }
                        beginTime = extractpres_startTime_editText.getText().toString();
                        endTime = extractpres_endTime_editText.getText().toString();
                        startActivity(new Intent(ExtractPresUI.this, LoadingUI.class));
                        extractPresThread(0);
                    }else{
                        startActivity(new Intent(ExtractPresUI.this, LoadingUI.class));
                        extractPresThread(3);
                    }
                    break;
                case R.id.extractpres_startTime_editText:
                    DateTimePickDialogUtil startdateTimePicKDialog = new DateTimePickDialogUtil(
                            ExtractPresUI.this,String.valueOf( extractpres_startTime_editText.getText()));
                    startdateTimePicKDialog.dateTimePicKDialog(extractpres_startTime_editText);
                    break;
                case R.id.extractpres_endTime_editText:
                    DateTimePickDialogUtil enddateTimePicKDialog = new DateTimePickDialogUtil(
                            ExtractPresUI.this,String.valueOf( extractpres_endTime_editText.getText()));
                    enddateTimePicKDialog.dateTimePicKDialog(extractpres_endTime_editText);
                    break;
            }
    }
    /**
     * 刷新处方子线程
     **/
    private void extractPresThread(int what) {
        final Message message = new Message();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        LoadingUI.instance.finish();
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                    case 0:
                        LoadingUI.instance.finish();
                        new CoolToast(getBaseContext()).show("提取处方成功");
                        extractpres_description_textView.setText((String) msg.obj);
                        break;
                    case 1:
                        LoadingUI.instance.finish();
                        Map valMap= (Map) msg.obj;
                        intevalId=Integer.parseInt(String.valueOf(valMap.get("id")));
                        extractpres_update_button.setText("修改时间");
                        extractpres_presId_editText.setText(String.valueOf(valMap.get("loadInterval")));
//                        extractpres_time_editText.setFocusable(true);
//                        extractpres_time_editText.setFocusableInTouchMode(true);
//                        extractpres_time_editText.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        isClick=true;
                        break;
                    case 2:
                        LoadingUI.instance.finish();
                        new CoolToast(getBaseContext()).show(String.valueOf(msg.obj));
                        extractpres_description_textView.setText("修改为1分钟提取"+extractpres_presId_editText.getText()+"分钟的处方");
                        isClick=false;
                        extractpres_update_button.setText("更新时间");
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
                            Map map= new ExtPreUtil().importPresByTime(beginTime,endTime);
                            if(map!=null){
                                message.obj="本次任务检索出("+map.get("begin")+"至"+map.get("end")+")的处方共"+map.get("count")+"条,导入系统"+map.get("import")+"条,消耗时间"+map.get("time")+"秒";
                            }else{
                                message.obj="提取处方失败,请稍后再试。";
                            }
                            message.what = 0;
                            handler.sendMessage(message);
                            break;
                        case 1:
                            Map valMap= new ExtPreUtil().getInterval();
                            message.obj=valMap;
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        case 2:
                            message.obj= new ExtPreUtil().setInterval(intevalId,Integer.parseInt(String.valueOf(extractpres_presId_editText.getText())));
                            message.what = 2;
                            handler.sendMessage(message);
                            break;
                        case 3:
                            Map map1= new ExtPreUtil().importPresBypresId(extractpres_presId_editText.getText().toString().trim());
                            if(map1!=null){
                                message.obj="本次任务检索出("+map1.get("begin")+"至"+map1.get("end")+")的处方共"+map1.get("count")+"条,导入系统"+map1.get("import")+"条,消耗时间"+map1.get("time")+"秒";
                            }else{
                                message.obj="提取处方失败,请稍后再试。";
                            }
                            message.what = 0;
                            handler.sendMessage(message);
                            break;
                    }
                }catch (TimeoutException ex){
                    message.what = -1;
                    message.obj="提取处方超时,提取或许已经成功。";
                    handler.sendMessage(message);
                }
                catch (Exception e) {
                    message.what = -1;
                    message.obj="提取处方失败,请稍后再试。";
//                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }
}
