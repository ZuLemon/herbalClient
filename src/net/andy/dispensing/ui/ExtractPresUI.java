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
import net.andy.dispensing.util.ExtPreUtil;
import org.w3c.dom.Text;

import java.util.Map;

/**
 * 提取处方
 * Created by Guang on 2016/5/12.
 */
public class ExtractPresUI extends Activity{
    private Button extractpres_extPre_button;
    private Button extractpres_update_button;
    private EditText extractpres_time_editText;
    private TextView extractpres_description_textView;
    private Integer intevalId=0;
    private Integer extTime;
    private boolean isClick;
    private ButtonListener buttonListener=new ButtonListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extractpres);
        extractpres_extPre_button= (Button) findViewById(R.id.extractpres_extPre_button);
        extractpres_update_button= (Button) findViewById(R.id.extractpres_update_button);
        extractpres_time_editText= (EditText) findViewById(R.id.extractpres_time_editText);
        extractpres_description_textView= (TextView) findViewById(R.id.extractpres_description_textView);
        extractpres_extPre_button.setOnClickListener(buttonListener);
        extractpres_update_button.setOnClickListener(buttonListener);
    }
    private class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
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
                    if(extractpres_time_editText.getText().toString().length()==0){
                        new CoolToast(getBaseContext()).show("时间不能为空！");
                        return;
                    }
                    extTime= Integer.valueOf(extractpres_time_editText.getText().toString());
                    startActivity(new Intent(ExtractPresUI.this,LoadingUI.class));
                    extractPresThread(0);
                    break;
            }
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
                        extractpres_time_editText.setText(String.valueOf(valMap.get("loadInterval")));
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
                        extractpres_description_textView.setText("修改为1分钟提取"+extractpres_time_editText.getText()+"分钟的处方");
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
                            Map map= new ExtPreUtil().importPres(extTime);
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
                            message.obj= new ExtPreUtil().setInterval(intevalId,Integer.parseInt(String.valueOf(extractpres_time_editText.getText())));
                            message.what = 2;
                            handler.sendMessage(message);
                            break;
                    }
                } catch (Exception e) {
                    Log.e("错误",e.getMessage());
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }
}
