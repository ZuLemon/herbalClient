package net.andy.boiling.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.R;
import net.andy.boiling.domain.ExtractingDomain;
import net.andy.boiling.domain.PrescriptionDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.domain.TagDomain;
import net.andy.com.ChineseToSpeech;
import net.andy.com.CoolToast;
import net.andy.com.Http;
import net.andy.com.NFCActivity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014-11-24.
 * 取桶操作
 */
public class TakeUI extends NFCActivity {
    @ViewInject(R.id.take_patientNO_textView)
    private TextView take_patientNO_textView;
    @ViewInject(R.id.take_patientName_textView)
    private TextView take_patientName_textView;
    @ViewInject(R.id.take_category_textView)
    private TextView take_category_textView;
    @ViewInject(R.id.take_classification_textView)
    private TextView take_classification_textView;
    @ViewInject(R.id.take_diagnosis_textView)
    private TextView take_diagnosis_textView;
    @ViewInject(R.id.take_presNumber_textView)
    private TextView take_presNumber_textView;
    @ViewInject(R.id.take_code_textView)
    private TextView take_code_textView;
    @ViewInject(R.id.take_hint_textView)
    private TextView take_hint_textView;
    @ViewInject(R.id.take_get_button)
    private Button take_get_button;
    private String equipId;
    private ExtractingDomain extracting = new ExtractingDomain();
    private PrescriptionDomain prescription = new PrescriptionDomain();
    private TagDomain tag = new TagDomain();
    private ChineseToSpeech speech = new ChineseToSpeech();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take);
        x.view().inject(this);
    }
 @Event(value = R.id.take_get_button)
  private void onClick(View v) {
            Intent intent = new Intent(TakeUI.this,EquipmentUI.class);
            startActivityForResult(intent, 0);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && data != null){
            equipId = data.getStringExtra("equipId");
            if (equipId != null){
                takePlan(1);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (extracting.getPlanStatus().equals("待领取")){
            if (tag.getTagId().equals(getNfc().readID(intent))){
                takePlan(2);
                speech.speech("核对正确");
            } else {
                speech.speech("处方错误");
            }
        } else {
            speech.speech("处方已被领取");
        }

    }

    public void setValue(){
        take_patientNO_textView.setText(String.valueOf(prescription.getPatientNo()));
        take_patientName_textView.setText(prescription.getPatientName());
        take_category_textView.setText(prescription.getCategory());
        take_classification_textView.setText(prescription.getClassification());
        take_diagnosis_textView.setText(prescription.getDiagnosis());
        take_presNumber_textView.setText(String.valueOf(prescription.getPresNumber()));
        take_code_textView.setText(tag.getCode());
        take_hint_textView.setText("扫描标签核对处方");
        if (!tag.getCode().equals("")){
            if (extracting.getPlanStatus().equals("待领取")){
                speech.speech("领取"+tag.getCode()+"处方");
            } else {
                speech.speech("处方已被领取");
            }

        }
    }

    public void reset(){
        take_patientNO_textView.setText("");
        take_patientName_textView.setText("");
        take_category_textView.setText("");
        take_classification_textView.setText("");
        take_diagnosis_textView.setText("");
        take_presNumber_textView.setText("");
        take_code_textView.setText("");
        take_hint_textView.setText("点击取处方");
    }

    public void takePlan(final int what){
        final Message message = new Message();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case -1:
                        new CoolToast(getBaseContext()).show(String.valueOf(msg.obj));
                        reset();
                        break;
                    case 1:
                        setValue();
                        break;
                    case 2:
                        reset();
                        break;
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {

                    switch (what){
                        case 1:
                            takePlanRequest();
                            getPres();
                            getTag();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        case 2:
                            takeSubmit();
                            message.what = 2;
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

    public void takePlanRequest() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("equipId",equipId));

            ReturnDomain returnDomain = (ReturnDomain)(new Http().post("equipment/takePlan.do",pairs,ReturnDomain.class));
            if (returnDomain.getSuccess()){
                extracting = JSON.parseObject(returnDomain.getObject().toString(),ExtractingDomain.class);
            }else {
                throw new Exception(returnDomain.getException());
            }
    }

    public void getPres() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("presId",extracting.getPresId()));

        ReturnDomain returnDomain = (ReturnDomain)(new Http().post("prescription/getPrescriptionByPresId.do",pairs,ReturnDomain.class));
        if (returnDomain.getSuccess()){
            prescription = JSON.parseObject(returnDomain.getObject().toString(),PrescriptionDomain.class);
        }else {
            throw new Exception(returnDomain.getException());
        }
    }

    public void getTag() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("tagId",extracting.getTagId()));

        ReturnDomain returnDomain = (ReturnDomain)(new Http().post("tag/getTagByTagId.do",pairs,ReturnDomain.class));
        if (returnDomain.getSuccess()){
            tag = JSON.parseObject(returnDomain.getObject().toString(),TagDomain.class);
        }else {
            throw new Exception(returnDomain.getException());
        }
    }

    public void takeSubmit() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("planId",extracting.getPlanId()));
        pairs.add(new BasicNameValuePair("equipId",equipId));

        ReturnDomain returnDomain = (ReturnDomain)(new Http().post("take/submit.do",pairs,ReturnDomain.class));
        if (!returnDomain.getSuccess()){
            throw new Exception(returnDomain.getException());
        }
    }

    @Override
    protected void onDestroy() {
        if (speech != null){
            speech.destroy();
        }
        super.onDestroy();
    }
}
