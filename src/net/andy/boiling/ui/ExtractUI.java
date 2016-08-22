package net.andy.boiling.ui;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.boiling.domain.*;
import net.andy.boiling.util.*;
import net.andy.com.Application;
import net.andy.com.ChineseToSpeech;
import net.andy.com.CoolToast;
import net.andy.com.NFCActivity;
import net.andy.dispensing.domain.DispensingDomain;
import net.andy.dispensing.util.DispensingUtil;
import net.andy.dispensing.util.HerbalUtil;
import net.andy.dispensing.util.ServerUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014-11-14.
 * 煎制操作
 */
@ContentView(R.layout.extract)
public class ExtractUI extends NFCActivity {
    @ViewInject(R.id.extract_extractEquip_textView)
    private TextView extract_extractEquip_textView;
    @ViewInject(R.id.extract_soakEquip_textView)
    private TextView extract_soakEquip_textView;

    @ViewInject(R.id.extract_tagCode_textView)
    private TextView extract_tagCode_textView;
    @ViewInject(R.id.extract_presStatus_textView)
    private TextView extract_presStatus_textView;
    @ViewInject(R.id.extract_patientNo_textView)
    private TextView extract_patientNo_textView;
    @ViewInject(R.id.extract_patientName_textView)
    private TextView extract_patientName_textView;
    @ViewInject(R.id.extract_category_textView)
    private TextView extract_category_textView;
    @ViewInject(R.id.extract_classification_textView)
    private TextView extract_classification_textView;
    @ViewInject(R.id.extract_presNumber_textView)
    private TextView extract_presNumber_textView;
    @ViewInject(R.id.extract_way_textView)
    private TextView extract_way_textView;
    @ViewInject(R.id.extract_temperature_textView)
    private TextView extract_temperature_textView;
    @ViewInject(R.id.extract_pressure_textView)
    private TextView extract_pressure_textView;
    //    @ViewInject(R.id.extract_planStatus_textView)
//    private TextView extract_planStatus_textView;
    @ViewInject(R.id.extract_waterQuantity1_textView)
    private TextView extract_waterQuantity1_textView;
    @ViewInject(R.id.extract_method_textView)
    private TextView extract_method_textView;
    @ViewInject(R.id.extract_soakTime_textView)
    private TextView extract_soakTime_textView;
    @ViewInject(R.id.extract_extractTime1_textView)
    private TextView extract_extractTime1_textView;
    @ViewInject(R.id.extract_extractTime2_textView)
    private TextView extract_extractTime2_textView;
    @ViewInject(R.id.extract1_equiptype1_radioButton)
    private RadioButton extract1_equiptype1_radioButton;
    @ViewInject(R.id.extract2_equiptype1_radioButton)
    private RadioButton extract2_equiptype1_radioButton;
    private TextView extract_status_textView;
    @ViewInject(R.id.extract_pack_button)
    private Button extract_pack_button;
    @ViewInject(R.id.extract_extract1_button)
    private Button extract_extract1_button;
    @ViewInject(R.id.extract_extract2_button)
    private Button extract_extract2_button;
    @ViewInject(R.id.extract_extract3_button)
    private Button extract_extract3_button;
    @ViewInject(R.id.extract_soak_button)
    private Button extract_soak_button;
    @ViewInject(R.id.extract_equiptype1_radioGroup)
    private RadioGroup extract_equiptype1_radioGroup;
    @ViewInject(R.id.extract_startTime_textView)
    private TextView extract_startTime_textView;
    @ViewInject(R.id.extract_endTime_textView)
    private TextView extract_endTime_textView;
    @ViewInject(R.id.extract_extractbutton_linearLayout)
    private LinearLayout extract_extractbutton_linearLayout;
    @ViewInject(R.id.extract_soakbutton_linearLayout)
    private LinearLayout extract_soakbutton_linearLayout;
    @ViewInject(R.id.extract_out_textView)
    private TextView extract_out_textView;
    private ReturnDomain returnDomain;
    private DispensingDomain dispensing = new DispensingDomain();
    private TagDomain tagDomain = new TagDomain();
    private TagDomain waterTag = new TagDomain();
    private TagDomain extractTag = new TagDomain();
    private PrescriptionDomain prescriptionDomain = null;
    private ExtractingDomain extractingDomain = null;
    private EquipmentDomain waterEquipment = null;
    private EquipmentDomain extractEquipment =null;
    private SoakDomain soakDomain = null;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private SoakUtil soakUtil=new SoakUtil();
    private ChineseToSpeech speech = new ChineseToSpeech();
    private String extractStatus;
    private String equipId;
    private String tagId;
    private String code;
    private boolean hasPre;
    private boolean hasWater;
    private boolean hasExtract;
    private boolean hasPack;
    private String equipType1;
    private Integer quantity;
    private Message message;
    private List ExtractList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
//        Bundle bundle = this.getIntent().getExtras();
//        if(bundle!=null) {
//            String planStatus = bundle.getString("planStatus");
//            String planId = bundle.getString("planId");
//            code = bundle.getString("code");
//            System.out.println(planId + "planStatus:" + planStatus+code);
//            extractThread ( 3 );
//        }
    }

    @Event(value = {R.id.extract_extract1_button,
            R.id.extract_extract1_button,
            R.id.extract_extract1_button,
            R.id.extract_pack_button,
            R.id.extract_soak_button},
            type = View.OnClickListener.class)
    private void btnClick(View v) {
                if(prescriptionDomain==null){
                    new CoolToast(getBaseContext()).show("请先选择处方");
                    return;
                }
        switch (v.getId()) {
            case R.id.extract_extract1_button:
                if(hasExtract) {
                    extractStatus = "一煎";
                    extractThread(4);
                }else{
                    new CoolToast(getBaseContext()).show("请先选择煎药机");
                }
                break;
            case R.id.extract_extract2_button:
                if(hasExtract) {
                extractStatus = "二煎";
                extractThread(4);
                    }else{
                        new CoolToast(getBaseContext()).show("请先选择煎药机");
                    }
                break;
            case R.id.extract_pack_button:
                if("一煎".equals(extractingDomain.getPlanStatus())||"一煎".equals(extractingDomain.getPlanStatus())) {
                    extractThread(5);
                }else{
                    new CoolToast(getBaseContext()).show("请先煎制处方");
                }
                break;
            case R.id.extract_soak_button:
                Log.e(">>", "浸泡");
                if (hasPre && hasWater) {
                    extractThread(3);
                }else{
                    new CoolToast(getBaseContext()).show("请先选择加液机");
                    return;
                }
                break;
        }
    }

    public void setWidget() {
        try {
            if ("".equals(tagDomain.getCode())) {
                Log.e("code", "code为空");
            } else {
                extract_tagCode_textView.setText(tagDomain.getCode().replace("M", ""));
            }
            if (prescriptionDomain != null) {
                extract_patientNo_textView.setText(prescriptionDomain.getPatientNo());
                extract_patientName_textView.setText(prescriptionDomain.getPatientName().length() < 4 ? prescriptionDomain.getPatientName() : prescriptionDomain.getPatientName().substring(0, 4));
                extract_category_textView.setText(prescriptionDomain.getCategory());
                extract_classification_textView.setText(prescriptionDomain.getClassification());
                extract_presNumber_textView.setText(prescriptionDomain.getPresNumber().toString() + "付");
                extract_way_textView.setText(prescriptionDomain.getWay());
            }
            if (extractingDomain != null) {
                extract_out_textView.setText(String.valueOf(extractingDomain.getOut()));
                extract_method_textView.setText(extractingDomain.getMethod());
                extract_waterQuantity1_textView.setText(String.valueOf(extractingDomain.getQuantity()));
                extract_temperature_textView.setText(extractingDomain.getTemperature().toString() + " °");
                extract_pressure_textView.setText(extractingDomain.getPressure());
                extract_presStatus_textView.setText(extractingDomain.getPlanStatus());
                extract_soakTime_textView.setText(extractingDomain.getSoakTime().toString() + " 分");
                extract_extractTime1_textView.setText(extractingDomain.getExtractTime1().toString() + " 分");
                extract_extractTime2_textView.setText(extractingDomain.getExtractTime2().toString() + " 分");
            }
            if (soakDomain != null) {
                Log.e("soakDomain", String.valueOf(hasWater));
                hasWater=true;
                extract_startTime_textView.setText(simpleDateFormat.format(soakDomain.getBeginTime()));
                extract_endTime_textView.setText(simpleDateFormat.format(soakDomain.getEndTime()));
                extract_soakEquip_textView.setText(waterEquipment.getEquipId());
                extract1_equiptype1_radioButton.setClickable(false);
                extract2_equiptype1_radioButton.setClickable(false);
            }else{
                hasWater=false;
                extract1_equiptype1_radioButton.setClickable(true);
                extract2_equiptype1_radioButton.setClickable(true);
            }
            if(extractEquipment!=null){
                extract_extractEquip_textView.setText(extractEquipment.getEquipId());
            }

            if(hasPack){
                extract_extractbutton_linearLayout.setVisibility(View.GONE);
                extract_soakbutton_linearLayout.setVisibility(View.GONE);
            }else{
                if (! "开始".equals(extractingDomain.getPlanStatus())) {
                    extract_extractbutton_linearLayout.setVisibility(View.VISIBLE);
                    extract_soakbutton_linearLayout.setVisibility(View.GONE);
                }else{
                    extract_extractbutton_linearLayout.setVisibility(View.GONE);
                    extract_soakbutton_linearLayout.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Event(value = R.id.extract_equiptype1_radioGroup,
            type = RadioGroup.OnCheckedChangeListener.class)
    private void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == extract1_equiptype1_radioButton.getId()) {
            equipType1 = String.valueOf(extract1_equiptype1_radioButton.getText());
        } else if (checkedId == extract2_equiptype1_radioButton.getId()) {
            equipType1 = String.valueOf(extract2_equiptype1_radioButton.getText());
        }
        if (hasPre) {
            Log.e("equipType1", equipType1);
            extractThread(2);
        }
    }

    public void resetObject() {
        tagDomain = new TagDomain();
        prescriptionDomain = new PrescriptionDomain();
        extractingDomain = new ExtractingDomain();
        waterEquipment = new EquipmentDomain();
        extractEquipment = new EquipmentDomain();
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case - 1:
                    reset();
                    resetObject();
                    resetView();
                    new CoolToast(getBaseContext()).show("异常：" + msg.obj);
//                    setWidget ();
                    break;
                case 0:
                    break;
                case 1:
                    resetView();
                    setWidget();
                    break;
                case 2:
                    if (quantity != null) {
                        extractingDomain.setQuantity(quantity);
                        extract_waterQuantity1_textView.setText(String.valueOf(quantity));
                    }
                    break;
                case 3:
                    new CoolToast(getBaseContext()).show((String) msg.obj);
                    reset();
                    resetObject();
                    resetView();
                    break;
                case 4:
                    new CoolToast(getBaseContext()).show("开始"+extractStatus);
                    reset();
                    resetObject();
                    resetView();
                    break;
                case 5:
                    new CoolToast(getBaseContext()).show("开始包装");
                    reset();
                    resetObject();
                    resetView();
                    break;
                case 11:
                    Log.e(">>", waterEquipment.getEquipId());
                    hasWater=true;
                    extract_soakEquip_textView.setText(waterEquipment.getEquipId());
                    break;
                case 12:
                    Log.e(">>", extractEquipment.getEquipId());
                    hasExtract=true;
                    extract_extractEquip_textView.setText(extractEquipment.getEquipId());
                    break;
            }
        }
    };

    public void extractThread(int what) {
        message = new Message();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            tagDomain = new TagUtil().getTagByTagId(tagId);
                            judgeTag();
//                            message.what = 0;
//                            handler.sendMessage ( message );
                            break;
                        //查询煎制、处方信息
                        case 1:
                            extractingDomain = new ExtractingUtil().importExtracting(tagId, Application.getUsers().getId(), "");
                            prescriptionDomain = new PrescriptionUtil().getPrescriptionByPlanId(extractingDomain.getPlanId());
                            if (! "开始".equals(extractingDomain.getPlanStatus())) {
                                soakDomain = soakUtil.getSoakByPlanId(extractingDomain.getPlanId());
                                waterEquipment=new EquipmentUtil().getEquipment(soakDomain.getEquipId());
                                if(!"浸泡".equals(extractingDomain.getPlanStatus())){
                                    ExtractList=new ExtractUtil().getExtractByPlanId(extractingDomain.getPlanId());
                                    extractEquipment=new EquipmentUtil().getEquipByEquipId(String.valueOf(((Map)ExtractList.get(0)).get("equipId")));
                                }
                                if("包装".equals(extractingDomain.getPlanStatus())){
                                    hasPack=true;
                                }
                            }
                            message.obj = "";
                            message.what = 1;
//                            }
                            handler.sendMessage(message);
                            break;
                        case 2:
                            quantity = new ExtractingUtil().calcWater(extractingDomain.getPlanId(), equipType1);
                            message.what = 2;
                            handler.sendMessage(message);
                            break;
                        //开始浸泡
                        case 3:
                            new ExtractingUtil().subExtracting(extractingDomain,false);
                            message.obj = soakUtil.insert(extractingDomain.getId(), waterEquipment.getId(), Application.getUsers().getId());
                            message.what = 3;
                            handler.sendMessage(message);
                            break;
                        //开始煎制
                        case 4:
                            Log.e(">>","煎制");
                            SoakDomain tempSoak=soakUtil.getSoakByPlanId(extractingDomain.getPlanId());
                           if(new Date(Long.parseLong(new ServerUtil().getServerTime())).getTime()<tempSoak.getEndTime().getTime()){
                               message.obj = HerbalUtil.formatDate(tempSoak.getEndTime(),"HH:mm") + "后方可进行一煎";
                               message.what = -1;
                               handler.sendMessage(message);
                           }else {
                               startExtract();
                           }
//                            IsPre(planId);
                            message.what = 4;
                            handler.sendMessage(message);
                            break;
                        case 5:
                            Date endTime=HerbalUtil.String2Date(String.valueOf(((Map)ExtractList.get(ExtractList.size()-1)).get("endTime")),null);
                            Log.e("endTime"+new Date(Long.parseLong(new ServerUtil().getServerTime())).getTime(), String.valueOf(endTime.getTime()));
                            if(new Date(Long.parseLong(new ServerUtil().getServerTime())).getTime()<endTime.getTime()){
                                message.obj = HerbalUtil.formatDate(endTime,"HH:mm") + "后方可进行包装";
                                message.what = -1;
                                handler.sendMessage(message);
                            }else{
                            message.obj = new PackUtil().insert(extractingDomain.getPlanId());
                            message.what = 5;
                            handler.sendMessage(message);
                            }
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

    private void startExtract() throws Exception {
        new ExtractUtil().insert(extractingDomain.getPlanId(), extractEquipment.getEquipId(), extractStatus);
    }
//    private void IsPre(String PlanId) throws Exception {
//        tagDomain = new TagUtil().getTagByTagId(tagId);
//        extractingDomain = new ExtractingUtil().getExtractingByPlanId(PlanId);
//        String planStatus = extractingDomain.getPlanStatus();
//        if ("浸泡".equals(planStatus)) {
//            extract_extract1_button.setClickable(true);
//            extract_pack_button.setClickable(false);
//        }
//        if ("一煎".equals(planStatus)) {
//            extract_extract1_button.setClickable(false);
//            extract_pack_button.setClickable(true);
//        }
//        if ("二煎".equals(planStatus)) {
//            extract_extract2_button.setClickable(false);
//            extract_pack_button.setClickable(true);
//        }
//        extractEquipment = new EquipmentUtil().getEquipByPlanId(PlanId);
//        prescriptionDomain = new PrescriptionUtil().getPrescriptionByPresId(extractingDomain.getPresId());
//        hasPre = true;
//    }

    /**
     * 判断处方调剂
     *
     * @param disId
     */
    private void isDispensing(String disId) throws Exception {
        dispensing = new DispensingUtil().getDispensing(disId);
        if (dispensing != null) {
            prescriptionDomain = new PrescriptionUtil().getPrescriptionByPresId(dispensing.getPlanId());

        }
    }

    /**
     * 识别
     *
     * @throws Exception
     */
    private void judgeTag() throws Exception {
        if ("处方".equals(tagDomain.getType())) {
            reset();
            extractThread(1);
            hasPre = true;
            Log.e("judgeTag", "处方");
        } else {
            if (hasPre) {
                if ("加液机".equals(tagDomain.getType())) {
                    waterTag = tagDomain;
                    waterEquipment = new EquipmentUtil().getEquipByTagId(tagId);
                    hasWater = true;
                    Log.e("judgeTag", "加液机");
                    handlerMessage(11);
                }
                if (hasWater) {
                    if ("煎药机".equals(tagDomain.getType())) {
                        extractTag=tagDomain;
                        Log.e("judgeTag", "煎药机");
                        extractEquipment=new EquipmentUtil().getEquipByTagId(tagId);
                        handlerMessage(12);
                    }
                } else {
                    throw new Exception("请先选择加液机");
                }
            } else {
                throw new Exception("请先读取处方");
            }
        }

    }
    private void reset(){
        hasPre=false;
        hasPack=false;
        hasWater=false;
        hasExtract=false;

    }
    private void resetView(){
        extract_patientNo_textView.setText("");
        extract_patientName_textView.setText("");
        extract_category_textView.setText("");
        extract_classification_textView.setText("");
        extract_presNumber_textView.setText("");
        extract_way_textView.setText("");
        extract_out_textView.setText("");
        extract_method_textView.setText("");
        extract_waterQuantity1_textView.setText("");
        extract_temperature_textView.setText("");
        extract_pressure_textView.setText("");
        extract_presStatus_textView.setText("");
        extract_soakTime_textView.setText("");
        extract_extractTime1_textView.setText("");
        extract_extractTime2_textView.setText("");
        extract_extractEquip_textView.setText("");
        extract_endTime_textView.setText("");
        extract_startTime_textView.setText("");
        extract_soakEquip_textView.setText("");
        extract_tagCode_textView.setText("");
        extract1_equiptype1_radioButton.setClickable(true);
        extract2_equiptype1_radioButton.setClickable(true);
    }
    private void handlerMessage(int what) {
        Log.e("handlerMessage", String.valueOf(what));
        message = null;
        message = new Message();
        message.what = what;
        handler.sendMessage(message);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tagId = getNfc().readID(intent);
        extractThread(0);
    }

    @Override
    protected void onDestroy() {
        if (speech != null) {
            speech.destroy();
        }
        super.onDestroy();
    }
}
