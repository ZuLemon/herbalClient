package net.andy.boiling.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.boiling.domain.*;
import net.andy.boiling.util.*;
import net.andy.com.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014-11-17.
 * 浸泡操作
 */
@ContentView(R.layout.soak)
public class SoakUI extends NFCActivity {
    protected final int RESULT_CODE=1001;
    protected final int REQUEST_CODE=1002;
    private TagDomain tagDomain;
    private TagDomain tagDomain1;
    private PrescriptionDomain prescriptionDomain;
    private ExtractingDomain extractingDomain;
    private EquipmentDomain equipmentDomain;
    private SoakDomain soakDomain;
    private ReturnDomain returnDomain;
    private ExtractingUtil extractingUtil;
    private EquipmentUtil equipmentUtil;
    private String eId;
    private String equipId;
    private String tagId;
    private Integer quantity1;
    private String Liquid;
    private boolean isPresc;
    private String tagType;
    private String planStatus;
    @ViewInject(R.id.soak_patientNO_textView)
    private TextView soak_patientNO_textView;
    @ViewInject(R.id.soak_patientName_textView)
    private TextView soak_patientName_textView;
    @ViewInject(R.id.soak_category_textView)
    private TextView soak_category_textView;
    @ViewInject(R.id.soak_classification_textView)
    private TextView soak_classification_textView;
    @ViewInject(R.id.soak_diagnosis_textView)
    private TextView soak_diagnosis_textView;
    @ViewInject(R.id.soak_presNumber_textView)
    private TextView soak_presNumber_textView;
    @ViewInject(R.id.soak_method_textView)
    private TextView soak_method_textView;
    @ViewInject(R.id.soak_planStatus_textView)
    private TextView soak_planStatus_textView;
    @ViewInject(R.id.soak_soakTime_editText)
    private LineEditText soak_soakTime_editText;
    @ViewInject(R.id.soak_pres_linearLayout)
    private LinearLayout soak_pres_linearLayout;
    @ViewInject(R.id.soak_efficacy_textView)
    private TextView soak_efficacy_textView;
    @ViewInject(R.id.soak_tagCode_textView)
    private TextView soak_tagCode_textView;
//    @ViewInject(R.id.soak_soakEndTime_textView)
//    private TextView soak_soakEndTime_textView;
    @ViewInject(R.id.soak_temperature_textView)
    private TextView soak_temperature_textView;
    @ViewInject(R.id.soak_pressure_textView)
    private TextView soak_pressure_textView;
    @ViewInject(R.id.soak_out_textView)
    private TextView soak_out_textView;
    @ViewInject(R.id.soak_extractTime1_editText)
    private LineEditText soak_extractTime1_editText;
    @ViewInject(R.id.soak_extractTime2_editText)
    private LineEditText soak_extractTime2_editText;
//    @ViewInject(R.id.soak_waterQuantity3_textView)
//    private TextView soak_waterQuantity3_textView;
//    @ViewInject(R.id.soak_extractTime3_textView)
//    private TextView soak_extractTime3_textView;
    @ViewInject(R.id.soak_submit_button)
    private Button soak_submit_button;
    @ViewInject(R.id.soak_waterQuantity1_textView)
    private TextView soak_waterQuantity1_textView;
    //    @ViewInject(R.id.equipId_textView)
//    private TextView equipId_textView;
    private RadioGroup soak_equiptype1_radioGroup;
    private RadioButton soak1_equiptype1_radioButton;
    private RadioButton soak2_equiptype1_radioButton;
    private String equipType;
    private RadioButton[] soak_equiptype1_radioButtonList;
    private int[] soak_equiptype1_valueList;
    private SolutionDomain solutionDomain;
    private String captrueResult;
    private boolean hasMipcaCapture=false;
    private  Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        soak_equiptype1_radioButtonList = new RadioButton[]{soak1_equiptype1_radioButton, soak2_equiptype1_radioButton};
        extractingUtil = new ExtractingUtil();
        equipmentUtil = new EquipmentUtil();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tagId = this.getNfc().readID(intent);
        Log.e("onNewIntent","###############"+tagId);
        if(!hasMipcaCapture){
            soakThread(0);
        }else{
            extractingDomain.setTagId(tagId);
            soakThread(3);

        }

    }

    private void check(String tagType) {
        if ("处方".equals(tagType)) {
            if ("开始".equals(planStatus)) {
//                Intent intentEquip = new Intent ();
//                intentEquip.setClass ( SoakUI.this, EquipmentUI.class );
//                startActivityForResult ( intentEquip, 1000 );
                soakThread(1);
                isPresc = true;
            } else {
                new CoolToast(getBaseContext()).show("处方处于" + planStatus + "状态");
                soakThread(1);
            }
        } else if ("加液机".equals(tagType)) {
            if (isPresc) {
                new CoolToast(getBaseContext()).show("已选择加液机");
            } else {
                new CoolToast(getBaseContext()).show("请先选择处方");
            }
        } else {
            new CoolToast(getBaseContext()).show("请选择处方或者加液机");
        }
    }

    /*   接收回传值         */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + "进入" + resultCode);
        if (requestCode == REQUEST_CODE ) {
            if(resultCode == ExtractPlanUI.RESULT_OK) {
//            eId = data.getStringExtra("eId");
//            equipId = data.getStringExtra("equipId");
//            getSoak(tagNo);
//                System.out.println (eId+"<>"+equipId);
                solutionDomain = (SolutionDomain) data.getSerializableExtra("solution");
                setSolution();
                setExtractingView();
            }else if(resultCode==MipcaActivityCapture.Capture_OK){
                Bundle bundle = data.getExtras();
                //显示扫描到的内容
                captrueResult=String.valueOf(bundle.getString("result"));
                if(captrueResult!=null&&!"".equals(captrueResult)){
                    hasMipcaCapture=true;
                    Log.e(">>2>>",captrueResult);
                    soakThread(0);
                }
            }
//            new CoolToast(getBaseContext()).show("选择煎制方案"+solutionDomain.getId());
        } else {
            new CoolToast(getBaseContext()).show("没有选择煎制方案");
        }
    }

    private void setSolution(){
        extractingDomain.setSoakTime(solutionDomain.getSoakTime());
        extractingDomain.setExtractTime1(solutionDomain.getExtractTime1());
        extractingDomain.setExtractTime2(solutionDomain.getExtractTime2());
        extractingDomain.setExtractTime3(solutionDomain.getExtractTime3());
        extractingDomain.setMethod(solutionDomain.getMode());
        extractingDomain.setSolutionId(String.valueOf(solutionDomain.getId()));
    }
    private void setExtractingView(){
        soak_soakTime_editText.setText(String.valueOf(extractingDomain.getSoakTime()));
        soak_method_textView.setText(extractingDomain.getMethod());
        soak_extractTime1_editText.setText(String.valueOf(extractingDomain.getExtractTime1()));
        soak_extractTime2_editText.setText(String.valueOf(extractingDomain.getExtractTime2()));
        soak_temperature_textView.setText(String.valueOf(extractingDomain.getTemperature()));
        soak_pressure_textView.setText(extractingDomain.getPressure());
        soak_out_textView.setText(String.valueOf(extractingDomain.getOut()));
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case - 1:
                    new CoolToast(getBaseContext()).show((String)msg.obj);
                    reset();
                    break;
                case 0:
                    setValue();
//                    check(tagType);
                    soak_tagCode_textView.setText(tagDomain.getCode().replace("M",""));
                    if("开始".equals(extractingDomain.getPlanStatus())) {
                        Intent intent = new Intent(SoakUI.this, ExtractPlanUI.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                    break;
                case 1:
                    new CoolToast(getBaseContext()).show("保存成功");
                    reset();
                    break;
                case 3:
                    soak_tagCode_textView.setText(tagDomain.getCode().replace("M",""));
                    new CoolToast(getBaseContext()).show("已绑定标签");
                    break;
            }
        }
    };

    /**
     * 浸泡线程
     **/
    public void soakThread(int what) {
        message= new Message();
        new Thread() {
            @Override
            public void run() {
                try {
                    switch (what) {
                        case 0:
                            tagDomain=new TagUtil().getTagByTagId(tagId);
                            if(hasMipcaCapture) {
                                extractingDomain = extractingUtil.importExtracting("", Application.getUsers().getId(), captrueResult);
                            }else {
                                extractingDomain = extractingUtil.importExtracting(tagId, Application.getUsers().getId(), "");
                            }
                            prescriptionDomain = new PrescriptionUtil().getPrescriptionByPlanId(extractingDomain.getPlanId());
                            message.what = 0;
                            handler.sendMessage(message);
                            break;
                        case 1:
                            if(hasMipcaCapture){
                             message.obj= extractingUtil.subExtracting(extractingDomain,true);
                            }else{
                                message.obj= extractingUtil.subExtracting(extractingDomain,false);
                            }
                            message.what =1;
                            handler.sendMessage(message);
                            break;
                        case 3:
                            tagDomain=new TagUtil().getTagByTagId(tagId);
                            message.what = 3;
                            handler.sendMessage(message);
                            break;
                    }
                } catch (Exception e) {
                    message.what = - 1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }


    public void setValue() {
        soak_patientNO_textView.setText(String.valueOf(prescriptionDomain.getPatientNo()));
        soak_patientName_textView.setText(String.valueOf(prescriptionDomain.getPatientName()));
        soak_category_textView.setText(String.valueOf(prescriptionDomain.getCategory()));
        soak_classification_textView.setText(String.valueOf(prescriptionDomain.getClassification()));
        soak_diagnosis_textView.setText(String.valueOf(prescriptionDomain.getDiagnosis()));
        soak_presNumber_textView.setText(String.valueOf(prescriptionDomain.getPresNumber())+" 付");
        soak_method_textView.setText(String.valueOf(extractingDomain.getMethod()));
        soak_planStatus_textView.setText(String.valueOf(extractingDomain.getPlanStatus()));
        soak_efficacy_textView.setText(String.valueOf(prescriptionDomain.getEfficacy()));
        soak_soakTime_editText.setText(String.valueOf(extractingDomain.getSoakTime()));
        soak_waterQuantity1_textView.setText(String.valueOf(extractingDomain.getQuantity()));
        soak_temperature_textView.setText(String.valueOf(extractingDomain.getTemperature()));
        soak_pressure_textView.setText(String.valueOf(extractingDomain.getPressure()));
        soak_extractTime1_editText.setText(String.valueOf(extractingDomain.getExtractTime1()));
        soak_extractTime2_editText.setText(String.valueOf(extractingDomain.getExtractTime2()));
        soak_out_textView.setText(String.valueOf(extractingDomain.getOut()));

    }

    public void reset() {
        soak_patientNO_textView.setText("");
        soak_patientName_textView.setText("");
        soak_category_textView.setText("");
        soak_classification_textView.setText("");
        soak_diagnosis_textView.setText("");
        soak_presNumber_textView.setText("");
        soak_method_textView.setText("");
        soak_planStatus_textView.setText("");
        soak_waterQuantity1_textView.setText("");
        soak_soakTime_editText.setText("");
        soak_temperature_textView.setText("");
        soak_pressure_textView.setText("");
        soak_extractTime1_editText.setText("");
        soak_extractTime2_editText.setText("");
        soak_tagCode_textView.setText("");
        soak_efficacy_textView.setText("");
        soak_out_textView.setText("");
        tagDomain = null;
        prescriptionDomain = null;
        extractingDomain = null;
        soakDomain = null;
    }

    @Event(value = {R.id.soak_submit_button,
            R.id.soak_pres_linearLayout,
    R.id.soak_extractTime1_editText,
    R.id.soak_soakTime_editText},
    type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()){
            case R.id.soak_submit_button:
                if(prescriptionDomain!=null&&extractingDomain!=null) {
                    extractingDomain.setSoakTime(Integer.parseInt(String.valueOf(soak_soakTime_editText.getText())));
                    extractingDomain.setExtractTime1(Integer.parseInt(String.valueOf(soak_extractTime1_editText.getText())));
                    extractingDomain.setExtractTime2(Integer.parseInt(String.valueOf(soak_extractTime2_editText.getText())));
                    if("".equals(extractingDomain.getTagId())){
                        new CoolToast(getBaseContext()).show("请先绑定标签");
                        return;
                    }
                    if(!"开始".equals(extractingDomain.getPlanStatus())){
                        new CoolToast(getBaseContext()).show("已经开始浸泡，不能修改");
                        return;
                    }
                    soakThread(1);
                    Log.e("soak_submit_button", "点击");
                }else{
                    new CoolToast(getBaseContext()).show("请先刷卡或扫描条码获取处方");
                }
                break;
            case R.id.soak_extractTime1_editText:
//                setInterval(false);
                setInterval(soak_extractTime1_editText,isSoak);
                break;
            case R.id.soak_soakTime_editText:
                setInterval(soak_soakTime_editText,isTime1);
                break;
            case R.id.soak_extractTime2_editText:
                setInterval(soak_extractTime2_editText,isTime2);
                break;
            case R.id.soak_pres_linearLayout:
                hasMipcaCapture=false;
                jump();
                break;
        }

    }
    /**
     * 读取二维码并返回数据
     */
    private void jump() {
        Intent intent = new Intent();
        intent.setClass(SoakUI.this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REQUEST_CODE);
    }
    private boolean isSoak;
    private boolean isTime1;
    private boolean isTime2;
    private void setInterval(LineEditText lineEditText,boolean is) {
        if (is) {
            is=false;
            lineEditText.setFocusable(false);
        } else {
            is=true;
            lineEditText.setFocusable(true);
            lineEditText.setFocusableInTouchMode(true);
            lineEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            setting_interval_editText.setSelection(setting_interval_editText.getText().length());
        }
    }
}
