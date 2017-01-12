package net.andy.boiling.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014-11-14.
 * 煎制操作
 */
@ContentView(R.layout.extract)
public class ExtractUI extends NFCActivity {
    private static final int requestCode = 9009;
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
    @ViewInject(R.id.extract_waterQuantity1_editText)
    private LineEditText  extract_waterQuantity1_editText;
    @ViewInject(R.id.extract_method_textView)
    private TextView extract_method_textView;
    @ViewInject(R.id.extract_soakTime_textView)
    private TextView extract_soakTime_textView;
    @ViewInject(R.id.extract_extractTime1_textView)
    private TextView extract_extractTime1_textView;
    @ViewInject(R.id.extract_extractTime2_textView)
    private TextView extract_extractTime2_textView;
    @ViewInject(R.id.extract_stewStop_textView)
    private TextView extract_stewStop_textView;
    @ViewInject(R.id.extract_stewStart_textView)
    private TextView extract_stewStart_textView;
    @ViewInject(R.id.extract1_equiptype1_radioButton)
    private RadioButton extract1_equiptype1_radioButton;
    @ViewInject(R.id.extract2_equiptype1_radioButton)
    private RadioButton extract2_equiptype1_radioButton;
    @ViewInject(R.id.extract_pack_button)
    private Button extract_pack_button;
    @ViewInject(R.id.extract_finish_button)
    private Button extract_finish_button;
    @ViewInject(R.id.extract_extract1_button)
    private Button extract_extract1_button;
    @ViewInject(R.id.extract_extract2_button)
    private Button extract_extract2_button;
    @ViewInject(R.id.extract_soak_button)
    private Button extract_soak_button;
    @ViewInject(R.id.extract_waitStew_button)
    private Button extract_waitStew_button;
    @ViewInject(R.id.extract_stew_button)
    private Button extract_stew_button;
    @ViewInject(R.id.extract_stewEquip_textView)
    private TextView extract_stewEquip_textView;
    @ViewInject(R.id.extract_equiptype1_radioGroup)
    private RadioGroup extract_equiptype1_radioGroup;
    @ViewInject(R.id.extract_soakStart_textView)
    private TextView extract_soakStart_textView;
    @ViewInject(R.id.extract_soakEnd_textView)
    private TextView extract_soakEnd_textView;
    @ViewInject(R.id.extract_extractStart_textView)
    private TextView extract_extractStart_textView;
    @ViewInject(R.id.extract_extractEnd_textView)
    private TextView extract_extractEnd_textView;
    @ViewInject(R.id.extract_packButton_linearLayout)
    private LinearLayout extract_packButton_linearLayout;
    @ViewInject(R.id.extract_out_textView)
    private TextView extract_out_textView;
    @ViewInject(R.id.extract_manufacture_textView)
    private TextView extract_manufacture_textView;
    @ViewInject(R.id.extract_extractInfo_linearLayout)
    private LinearLayout extract_extractInfo_linearLayout;
    @ViewInject(R.id.extract_soakInfo_linearLayout)
    private LinearLayout extract_soakInfo_linearLayout;
    @ViewInject(R.id.extract_stewInfo_linearLayout)
    private LinearLayout extract_stewInfo_linearLayout;
    @ViewInject(R.id.extract_pressure_linearLayout)
    private LinearLayout extract_pressure_linearLayout;
    @ViewInject(R.id.extract_other_linearLayout)
    private LinearLayout extract_other_linearLayout;
    @ViewInject(R.id.extract_presInfo_linearLayout)
    private LinearLayout extract_presInfo_linearLayout;
    @ViewInject(R.id.extract_waterEquip_linearLayout)
    private LinearLayout extract_waterEquip_linearLayout;
    @ViewInject(R.id.extract_dosage_textView)
    private TextView extract_dosage_textView;
    @ViewInject(R.id.extract_frequency_textView)
    private TextView extract_frequency_textView;
    private ReturnDomain returnDomain;
    private DispensingDomain dispensing = new DispensingDomain();
    private TagDomain tagDomain = new TagDomain();
    private TagDomain waterTag = new TagDomain();
    private TagDomain extractTag = new TagDomain();
    private TagDomain packTag=new TagDomain();
    private TagDomain StewTag = new TagDomain();
    private PrescriptionDomain prescriptionDomain = null;
    private ExtractingDomain extractingDomain = null;
    private ExtractDomain extractDomain=null;
    private ExtractDomain stewDomain=null;
    private EquipmentDomain waterEquipment = null;
    private EquipmentDomain extractEquipment =null;
    private EquipmentDomain packEquipment=null;
    private EquipmentDomain stewEquipment =null;
    private SoakDomain soakDomain = null;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SoakUtil soakUtil=new SoakUtil();
    private ChineseToSpeech speech = new ChineseToSpeech();
    private String extractStatus;
    private String equipId;
    private String tagId;
    private String code;
    private boolean hasPre;
    private boolean hasWater;
    private boolean hasExtract;
    private boolean hasExtractEquip;
    private boolean hasStewEquip;
    private boolean hasPack;
    private boolean hasStew;
    private boolean hasFinish;
    private String equipType1;
    private Integer quantity;
    private Message message;
//    private List ExtractList;
    private String[] jyj={};
    private List waterEquipList;
    private  List addList;
    private Integer waterId;
    private ChineseToSpeech chineseToSpeech=new ChineseToSpeech();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        waterEquipList=new ArrayList<EquipmentDomain>();
        extractThread ( 8 );
//        Bundle bundle = this.getIntent().getExtras();
//        if(bundle!=null) {
//            String planStatus = bundle.getString("planStatus");
//            String planId = bundle.getString("planId");
//            code = bundle.getString("code");
//            System.out.println(planId + "planStatus:" + planStatus+code);
//            extractThread ( 3 );
//        }
    }
    @Event(value = {R.id.extract_presInfo_linearLayout,
            R.id.extract_waterEquip_linearLayout},
            type = View.OnClickListener.class)
    private void btClick(View v){
        switch (v.getId()){
            case R.id.extract_presInfo_linearLayout:
                Intent affirmIntent=new Intent(ExtractUI.this,AffirmUI.class);
                Bundle bundle=new Bundle();
                bundle.putString("type","scan");
                affirmIntent.putExtras(bundle);
                startActivityForResult(affirmIntent,requestCode);
                break;
            case R.id.extract_waterEquip_linearLayout:
                if(hasPre&&!hasWater) {
                new AlertDialog.Builder(this).setTitle("请选择加液机").setIcon(
                        android.R.drawable.ic_dialog_info).setSingleChoiceItems(
                        jyj, 0,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                        extract_soakEquip_textView.setText(((Map) waterEquipList.get(which)).get("equipId").toString());
                        hasWater = true;
                        waterEquipment = new EquipmentDomain();
                        waterEquipment.setId(Integer.parseInt(((Map) waterEquipList.get(which)).get("id").toString()));
                        waterId = which;
                        }
                }).setNegativeButton("取消", null).show();
                }else {
                    new CoolToast(getBaseContext()).show("请先选择处方");
                }
                break;
        }
    }

    @Event(value = {R.id.extract_extract1_button,
            R.id.extract_extract1_button,
            R.id.extract_extract1_button,
            R.id.extract_pack_button,
            R.id.extract_soak_button,
            R.id.extract_extractInfo_linearLayout,
            R.id.extract_waterQuantity1_editText,
            R.id.extract_waitStew_button,
            R.id.extract_stew_button,
            R.id.extract_finish_button},
            type = View.OnClickListener.class)
    private void btnClick(View v) {
                if(prescriptionDomain==null){
                    new CoolToast(getBaseContext()).show("请先选择处方");
                    chineseToSpeech.speech("请先选择处方");
                    return;
                }
        switch (v.getId()) {
            case R.id.extract_extract1_button:
                if("浸泡".equals(extractingDomain.getPlanStatus())){
                    if(hasExtractEquip) {
                        extractStatus = "一煎";
                        extractThread(4);
                    }else{
                        new CoolToast(getBaseContext()).show("请先选择煎药机");
                        chineseToSpeech.speech("请先选择煎药机");
                    }
                }else{
                    new CoolToast(getBaseContext()).show("当前是"+extractingDomain.getPlanStatus()+"状态，不可一煎操作");
                }
                break;
//            case R.id.extract_extract2_button:
//                if(hasExtractEquip) {
//                extractStatus = "二煎";
//                extractThread(4);
//                    }else{
//                        new CoolToast(getBaseContext()).show("请先选择煎药机");
//                    }
//                break;
            case R.id.extract_pack_button:
                if("一煎".equals(extractingDomain.getPlanStatus())||"二煎".equals(extractingDomain.getPlanStatus())) {
                    extractThread(5);
                }else{
                    new CoolToast(getBaseContext()).show("请先煎制处方");
                    chineseToSpeech.speech("请先煎制处方");
                }
                break;
            case R.id.extract_soak_button:
                extract_soak_button.setEnabled(false);
                Log.e(">>", "浸泡");
                if (hasPre && hasWater) {
                    extractingDomain.setQuantity(Integer.parseInt(String.valueOf(extract_waterQuantity1_editText.getText())));
                    extractThread(3);
                }else{
                    waterEquipment = new EquipmentDomain();
                    //正式库
                    waterEquipment.setId(93);
                    //测试库
//                    waterEquipment.setId(21);
                    hasWater = true;
                    extractingDomain.setQuantity(Integer.parseInt(String.valueOf(extract_waterQuantity1_editText.getText())));
                    extractThread(3);
//                    new CoolToast(getBaseContext()).show("请先选择加液机");
//                    chineseToSpeech.speech("请先选择加液机");
                    return;
                }
                break;
            case R.id.extract_extractInfo_linearLayout:
//                startActivity(new Intent(this,ExtractingStatus.class));
                break;
            case R.id.extract_waterQuantity1_editText:
                setInterval(extract_waterQuantity1_editText,isWater);
                break;
            case R.id.extract_waitStew_button:
                extractStatus="待制膏";
                extractThread(10);
                break;
            case R.id.extract_stew_button:
                if(hasStewEquip){
                    extractThread(6);
                }else {
                    new CoolToast(getBaseContext()).show("请先选择煎膏设备");
                    chineseToSpeech.speech("请先选择煎膏设备");
                }
                break;
            case R.id.extract_finish_button:
                if("膏方".equals(prescriptionDomain.getClassification())) {
                    extractThread(7);
                }else {
                    extractStatus="完成";
                    extractThread(9);
                }
                break;
        }
    }
    //设置电锅、气锅
    private void setExtractType(){
        if(extractEquipment!=null){
            if("电锅".equals(extractEquipment.getEquipType1())){
                extract1_equiptype1_radioButton.setChecked(true);
                extract2_equiptype1_radioButton.setChecked(false);
            }else if("气锅".equals(extractEquipment.getEquipType1())){
                extract1_equiptype1_radioButton.setChecked(false);
                extract2_equiptype1_radioButton.setChecked(true);
            }
        }
    }

    //设置浸泡基础UI
    private void setSoakBase(){
        extract_method_textView.setText(extractingDomain.getMethod());
        extract_waterQuantity1_editText.setText(String.valueOf(extractingDomain.getQuantity()));
        extract_soakTime_textView.setText(extractingDomain.getSoakTime().toString() + " 分");
    }
    //设置浸泡UI
    private void setSoakView(){
        if (soakDomain != null) {
            Log.e("soakDomain", String.valueOf(soakDomain.getBeginTime()));
            hasWater=true;
            extract_soakStart_textView.setText(simpleDateFormat.format(soakDomain.getBeginTime()));
            extract_soakEnd_textView.setText(simpleDateFormat.format(soakDomain.getEndTime()));
            extract_soakEquip_textView.setText(waterEquipment.getEquipId());
            extract1_equiptype1_radioButton.setClickable(false);
            extract2_equiptype1_radioButton.setClickable(false);
            extract_waterQuantity1_editText.setEnabled(false);
        }else{
            hasWater=false;
            extract1_equiptype1_radioButton.setClickable(true);
            extract2_equiptype1_radioButton.setClickable(true);
            extract_waterQuantity1_editText.setEnabled(true);
        }
    }
    //设置煎制UI
    private void setExtractView(){
        extract_out_textView.setText(String.valueOf(extractingDomain.getOut()));
        extract_waterQuantity1_editText.setText(String.valueOf(extractingDomain.getQuantity()));
        extract_temperature_textView.setText(extractingDomain.getTemperature().toString() + " °");
        extract_pressure_textView.setText(extractingDomain.getPressure());
        extract_presStatus_textView.setText(extractingDomain.getPlanStatus());
        extract_extractTime1_textView.setText(extractingDomain.getExtractTime1().toString() + " 分");
        extract_extractTime2_textView.setText(extractingDomain.getExtractTime2().toString() + " 分");
        if (extractEquipment != null) {
            Log.e("extractEquipment", extractEquipment.getEquipName());
            extract_extractEquip_textView.setText(extractEquipment.getEquipId());
        } else {
            extract_extractEquip_textView.setText("");
        }
    }
    //设置煎制时间
    private void setExtractTimeView(){
//        if(ExtractList.size()>0) {
//            Map extMap = (Map) ExtractList.get(ExtractList.size() - 1);
                extract_extractStart_textView.setText(simpleDateFormat.format(extractDomain.getBeginTime()));
                extract_extractEnd_textView.setText(simpleDateFormat.format(extractDomain.getEndTime()));
        }
    private void setStewView(){
        if(stewEquipment!=null){
            extract_stewEquip_textView.setText(stewEquipment.getEquipId());
            extract_stewStart_textView.setText(simpleDateFormat.format(stewDomain.getBeginTime()));
            if(stewDomain.getEndTime()!=null&&!"".equals(stewDomain.getEndTime())) {
                extract_stewStop_textView.setText(simpleDateFormat.format(stewDomain.getEndTime()));
            }
        }
    }
    private boolean isWater;
    public void setWidget() {
//        try {
            if ("".equals(tagDomain.getCode())) {
                Log.e("code", "code为空");
            } else {
                extract_tagCode_textView.setText(tagDomain.getCode().replace("M", ""));
                extract_tagCode_textView.setBackgroundColor(HerbalUtil.HextoColor(tagDomain.getColorValue()));
            }
            if (prescriptionDomain != null) {
                //更新处方UI
                extract_patientNo_textView.setText(prescriptionDomain.getPatientNo());
                extract_patientName_textView.setText(prescriptionDomain.getPatientName().length() < 4 ? prescriptionDomain.getPatientName() : prescriptionDomain.getPatientName().substring(0, 4));
                extract_category_textView.setText(prescriptionDomain.getCategory());
                extract_classification_textView.setText(prescriptionDomain.getClassification());
                extract_presNumber_textView.setText(prescriptionDomain.getPresNumber().toString() + "付");
                extract_way_textView.setText(prescriptionDomain.getWay());
                if("膏方".equals(prescriptionDomain.getClassification())){
                    extract_pressure_linearLayout.setVisibility(View.GONE);
                    extract_other_linearLayout.setVisibility(View.VISIBLE);
                    extract_manufacture_textView.setText(prescriptionDomain.getManufacture());
                }else {
                    extract_pressure_linearLayout.setVisibility(View.VISIBLE);
                    extract_other_linearLayout.setVisibility(View.GONE);
                    extract_dosage_textView.setText(prescriptionDomain.getDosage());
                    extract_frequency_textView.setText(prescriptionDomain.getFrequency());
                }
            }
            if(extractingDomain!=null) {
                extract_presStatus_textView.setText(extractingDomain.getPlanStatus());
                //开始状态
                if ("开始".equals(extractingDomain.getPlanStatus())) {
                    extract_soakInfo_linearLayout.setVisibility(View.VISIBLE);
                    setSoakBase();
                    onCheckedChanged(extract_equiptype1_radioGroup, extract1_equiptype1_radioButton.getId());
                    extract_soak_button.setVisibility(View.VISIBLE);
                } else if ("浸泡".equals(extractingDomain.getPlanStatus())) {
                    extract_soakInfo_linearLayout.setVisibility(View.VISIBLE);
                    extract_extractInfo_linearLayout.setVisibility(View.VISIBLE);
                    setSoakBase();
                    setSoakView();
                    setExtractView();
                    extract_extract1_button.setVisibility(View.VISIBLE);
                } else if (extractingDomain.getPlanStatus().contains("煎")) {
                    hasWater=true;
                    extract_extractInfo_linearLayout.setVisibility(View.VISIBLE);
                    extract_extract2_button.setVisibility(View.VISIBLE);
                    setExtractView();
                    setExtractTimeView();
                    extract_packButton_linearLayout.setVisibility(View.VISIBLE);
                    if("膏方".equals(prescriptionDomain.getClassification())) {
                        extract_soakInfo_linearLayout.setVisibility(View.VISIBLE);
                        setSoakBase();
                        setSoakView();
                        extract_pack_button.setVisibility(View.GONE);
                        extract_waitStew_button.setVisibility(View.VISIBLE);
                        extract_stew_button.setVisibility(View.GONE);
                    }else {
                        extract_stewInfo_linearLayout.setVisibility(View.GONE);
                        extract_pack_button.setVisibility(View.VISIBLE);
                        extract_stew_button.setVisibility(View.GONE);
                    }
                }else if("待制膏".equals(extractingDomain.getPlanStatus())){
                    hasWater=true;
                    extract_extractInfo_linearLayout.setVisibility(View.VISIBLE);
                    setExtractView();
                    setExtractTimeView();
                    extract_packButton_linearLayout.setVisibility(View.VISIBLE);
                    extract_stewInfo_linearLayout.setVisibility(View.VISIBLE);

                    extract_stew_button.setVisibility(View.VISIBLE);
                }else if ("制膏".equals(extractingDomain.getPlanStatus())) {
                    if("膏方".equals(prescriptionDomain.getClassification())) {
                        extract_finish_button.setVisibility(View.VISIBLE);
                        extract_stewInfo_linearLayout.setVisibility(View.VISIBLE);
                    }
                    if(addList!=null){
                        if(addList.size()>0){
                            Intent intent=new Intent(this,AddMedicineUI.class);
                            intent.putExtra("addList",(Serializable) addList);
                            intent.putExtra("presNum",prescriptionDomain.getPresNumber());
                            startActivity(intent);
                        }
                    }
                    setStewView();
                    extract_packButton_linearLayout.setVisibility(View.GONE);
                }else if ("完成".equals(extractingDomain.getPlanStatus())) {
                    extract_extractInfo_linearLayout.setVisibility(View.VISIBLE);
                    setExtractView();
                    setExtractTimeView();
                    if("膏方".equals(prescriptionDomain.getClassification())){
                        extract_stewInfo_linearLayout.setVisibility(View.VISIBLE);
                        setStewView();
                    }else {
                        extract_soakInfo_linearLayout.setVisibility(View.VISIBLE);
                        setSoakBase();
                        setSoakView();
                    }
                    extract_finish_button.setVisibility(View.GONE);
                }else if("包装".equals(extractingDomain.getPlanStatus())){
                    extract_soakInfo_linearLayout.setVisibility(View.VISIBLE);
                    extract_extractInfo_linearLayout.setVisibility(View.VISIBLE);
                    setSoakBase();
                    setSoakView();
                    setExtractView();
                    extract_packButton_linearLayout.setVisibility(View.GONE);
                    extract_finish_button.setVisibility(View.VISIBLE);
                }
            }

            chineseToSpeech.speech(prescriptionDomain.getPresNumber()+"付 "+prescriptionDomain.getFrequency()+" "+prescriptionDomain.getDosage());

//        } catch (Exception e) {
////            e.printStackTrace();
//            new CoolToast(getBaseContext()).show("UI更新失败,请稍后重试。"+e.getMessage());
//        }
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
            extractingDomain.setSoakType(equipType1);
            extractThread(2);
        }
    }

    public void resetObject() {
//        tagDomain = null;
        soakDomain=null;
        prescriptionDomain = null;
        extractingDomain = null;
        waterEquipment = null;
        extractEquipment = null;
        stewEquipment=null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==this.requestCode && resultCode == RESULT_OK ){
                hasPre = true;
                if(tagId!=null) {
                    extractThread(1);
                }
            }
    }
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            extract_soak_button.setEnabled(true);
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
                        extract_waterQuantity1_editText.setText(String.valueOf(quantity));
                    }
                    break;
                case 3:
                    new CoolToast(getBaseContext()).show((String) msg.obj);
                    chineseToSpeech.speech(String.valueOf(msg.obj));
                    reset();
                    resetObject();
                    resetView();
//                    extractThread(0);
                    break;
                case 4:
                    new CoolToast(getBaseContext()).show("开始"+extractStatus);
                    chineseToSpeech.speech("开始"+extractStatus);
                    reset();
                    resetObject();
                    resetView();
                    break;
                case 5:
                    new CoolToast(getBaseContext()).show("开始包装");
                    chineseToSpeech.speech("开始包装");
                    reset();
                    resetObject();
                    resetView();
                    break;
                case 8:
                    jyj=new String[waterEquipList.size()];
                   for(int x=0;x<waterEquipList.size();x++){
                       jyj[x]=((Map)waterEquipList.get(x)).get("equipName").toString();
//                       Log.e(">>>>>>>",jyj[x]);
                    }
                    break;
                case 11:
                    Log.e(">>", waterEquipment.getEquipId());
                    hasWater=true;
                    extract_soakEquip_textView.setText(waterEquipment.getEquipId());
                    break;
                case 12:
                    Log.e(">>", extractEquipment.getEquipId());
                    hasExtractEquip=true;
                    extract_extractEquip_textView.setText(extractEquipment.getEquipId());
                    break;
                case 13:
                    hasPack=true;
//                    extract_packEquip_textView.setText(packEquipment.getEquipId());
                    break;
                case 14:
                    hasStewEquip=true;
                    extract_stewEquip_textView.setText(stewEquipment.getEquipId());
                    break;
                case 20:
                    new CoolToast(getBaseContext()).show(String.valueOf(msg.obj));
                    chineseToSpeech.speech(String.valueOf(msg.obj));
                    break;
                case 21:
                    reset();
                    break;
                case 22:
                    reset();
                    resetObject();
                    resetView();
                    new CoolToast(getBaseContext()).show(String.valueOf(msg.obj));
                    chineseToSpeech.speech(String.valueOf(msg.obj));
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
                            //如果未选择煎制方案则现在让选择
                            if("".equals(extractingDomain.getSolutionId())||"0".equals(extractingDomain.getSolutionId())){
                               Intent affirmIntent=new Intent(ExtractUI.this,AffirmUI.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("tagId",tagId);
                                bundle.putString("type","boiling");
                                affirmIntent.putExtras(bundle);
                                startActivityForResult(affirmIntent,requestCode);
                                message.obj = "";
                                message.what = 21;
//                            }
                                handler.sendMessage(message);
                                break;
                            }
                            prescriptionDomain = new PrescriptionUtil().getPrescriptionByPlanId(extractingDomain.getPlanId());
                            if (! "开始".equals(extractingDomain.getPlanStatus())) {
                                //获取浸泡和加液信息
                                soakDomain = soakUtil.getSoakByPlanId(extractingDomain.getPlanId());
                                waterEquipment=new EquipmentUtil().getEquipment(soakDomain.getEquipId());
                                if(!"浸泡".equals(extractingDomain.getPlanStatus())){
                                    //获取煎制信息
                                    extractDomain=new ExtractUtil().getExtractByPlanIdStatus(prescriptionDomain.getPlanId(), "一煎");;
//                                    if(ExtractList.size()>0) {
                                        extractEquipment = new EquipmentUtil().getEquipByEquipId(extractDomain.getEquipId());
//                                    }
                                    hasExtract=true;
                                }
                                if("包装".equals(extractingDomain.getPlanStatus())){
                                    hasPack=true;
                                }else{
                                    hasPack=false;
                                }
                                if("膏方".equals(prescriptionDomain.getClassification())){
                                if("制膏".equals(extractingDomain.getPlanStatus())||"完成".equals(extractingDomain.getPlanStatus())){
                                    //获取制膏信息
                                        hasStew = true;
                                        stewDomain = new ExtractUtil().getExtractByPlanIdStatus(prescriptionDomain.getPlanId(), "制膏");
                                        stewEquipment = new EquipmentUtil().getEquipByEquipId(stewDomain.getEquipId());
                                        if (stewDomain.getEndTime() == null || "".equals(stewDomain.getEndTime())) {
                                            addList = new PrescriptionDetailUtil().getAddMedicine(prescriptionDomain.getPlanId());
                                            hasFinish = false;
                                        } else {
                                            hasFinish = true;
                                        }
                                    }
                                }else {
                                    hasStew=false;
                                }
                            }
                            message.obj = "";
                            message.what = 1;
//                            }
                            handler.sendMessage(message);
                            break;
                        case 2:
                            quantity = new ExtractingUtil().calcWater(extractingDomain.getPlanId(), equipType1);
                            if(handler.obtainMessage(message.what, message.obj) != null){
                                Message _msg = new Message();
                                _msg.what = message.what;
                                message = _msg;
                            }
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
                            SoakDomain tempSoak=soakUtil.getSoakByPlanId(extractingDomain.getPlanId());
                            String serverTime=new ServerUtil().getServerTime();
//                            Log.e(">>","浸泡"+HerbalUtil.formatDate(tempSoak.getEndTime(),"yyyy-MM-dd HH:mm:ss") + ">"+HerbalUtil.formatDate(new Date(Long.parseLong(serverTime)),"yyyy-MM-dd HH:mm:ss"));
//                            Log.e(">>","浸泡"+(tempSoak.getEndTime().getTime()) + ">"+Long.parseLong(serverTime));
//                            Log.e(">>","浸泡"+(tempSoak.getEndTime().getTime()>Long.parseLong(serverTime)));
                           if(tempSoak.getEndTime().getTime()>Long.parseLong(serverTime)){
//                               new CoolToast(getBaseContext()).show(HerbalUtil.formatDate(tempSoak.getEndTime(),"HH:mm") + "后方可进行一煎");
                               handlerMessage(20,HerbalUtil.formatDate(tempSoak.getEndTime(),"HH:mm") + "后方可进行一煎");
//                               return;
//                               message.obj = HerbalUtil.formatDate(tempSoak.getEndTime(),"HH:mm") + "后方可进行一煎";
//                               message.what = -1;
//                               handler.sendMessage(message);
                           }else {
//                               handlerMessage(20,"成功");
                               startExtract();
                               message.what = 4;
                               handler.sendMessage(message);

                           }
//                            IsPre(planId);
                            break;
                        //开始包装
                        case 5:
                            Date endTime=extractDomain.getEndTime();
//                            Log.e("endTime"+new Date(Long.parseLong(new ServerUtil().getServerTime())).getTime(), String.valueOf(endTime.getTime()));
                            if(endTime.getTime()>Long.parseLong(new ServerUtil().getServerTime())){
                                handlerMessage(20,HerbalUtil.formatDate(endTime,"HH:mm") + "后方可进行包装");
//                                message.obj = HerbalUtil.formatDate(endTime,"HH:mm") + "后方可进行包装";
//                                message.what = -1;
//                                handler.sendMessage(message);
                            }else{
                            message.obj = new PackUtil().insert(extractingDomain.getPlanId());
                            message.what = 5;
                            handler.sendMessage(message);
                            }
                            break;
                        //开始煎膏
                        case 6:

                                    message.obj = new StewUtil().stewBegin(extractingDomain.getPlanId(), stewEquipment.getTagId(), Application.getUsers().getId());
                                    message.what = 22;
                                    handler.sendMessage(message);
                            break;
                        //结束煎膏
                        case 7:
                         message.obj = new StewUtil().stewEnd(extractingDomain.getPlanId(),stewEquipment.getTagId());
                         message.what = 22;
                         handler.sendMessage(message);
                         break;
                        case 8:
                            //获取所有加液机数据
                            waterEquipList = new EquipmentUtil().getEquipmentByType("加液机");
                            message.what = 8;
                            handler.sendMessage(message);
                            break;
                        case 9:
                            message.obj = new ExtractingUtil().setStatus(extractingDomain.getPlanId(),extractStatus);
                            message.what = 22;
                            handler.sendMessage(message);
                            //待制膏
                        case 10:
                            Date tempEndTime = extractDomain.getEndTime();
                            if (tempEndTime.getTime() > Long.parseLong(new ServerUtil().getServerTime())) {
                                handlerMessage(20, HerbalUtil.formatDate(tempEndTime, "HH:mm") + "完成煎制");
                            } else {
                                message.obj = new ExtractingUtil().setStatus(extractingDomain.getPlanId(),extractStatus);
                                message.what = 22;
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
        new ExtractUtil().insert(extractingDomain.getPlanId(), extractEquipment.getEquipId(), String.valueOf(Application.getUsers().getId()), extractStatus);
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
            resetObject();
            reset();
            hasPre = true;
            Log.e("judgeTag", "处方");
            extractThread(1);
        } else {
            if (hasPre) {
                if ("加液机".equals(tagDomain.getType())) {
                    waterTag = tagDomain;
                    waterEquipment = new EquipmentUtil().getEquipByTagId(tagId);
                    hasWater = true;
                    Log.e("judgeTag", "加液机");
                    handlerMessage(11,null);
                }
                if (hasWater) {
                    if ("煎药机".equals(tagDomain.getType())) {
                        extractTag=tagDomain;
                        Log.e("judgeTag", "煎药机");
                        extractEquipment=new EquipmentUtil().getEquipByTagId(tagId);
                        handlerMessage(12,null);
                    }
                    if(hasExtract){
                        if ("煎膏机".equals(tagDomain.getType())) {
                            StewTag=tagDomain;
                            Log.e("judgeTag", "煎膏机");
                            stewEquipment=new EquipmentUtil().getEquipByTagId(tagId);
                            handlerMessage(14,null);
                        }
                    }
                } else {
//                    throw new Exception("请先选择加液机");
                    handlerMessage(20,"请先选择加液机");
                }

                if("包装机".equals(tagDomain.getType())){
                    packTag=tagDomain;
                    packEquipment=new EquipmentUtil().getEquipByTagId(tagId);
                    handlerMessage(13,null);
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
        hasExtractEquip=false;
        hasExtract=false;
        hasFinish=false;
        addList=null;
    }
    private void resetView(){
        Log.e("resetView","Success");
        extract_patientNo_textView.setText("");
        extract_patientName_textView.setText("");
        extract_category_textView.setText("");
        extract_classification_textView.setText("");
        extract_presNumber_textView.setText("");
        extract_way_textView.setText("");
        extract_out_textView.setText("");
        extract_method_textView.setText("");
        extract_waterQuantity1_editText.setText("");
        extract_temperature_textView.setText("");
        extract_pressure_textView.setText("");
        extract_presStatus_textView.setText("");
        extract_soakTime_textView.setText("");
        extract_extractTime1_textView.setText("");
        extract_extractTime2_textView.setText("");
        extract_extractEquip_textView.setText("");
        extract_soakStart_textView.setText("");
        extract_soakEnd_textView.setText("");
        extract_soakEquip_textView.setText("");
        extract_tagCode_textView.setText("");
        extract_dosage_textView.setText("");
        extract_frequency_textView.setText("");
        extract_stewEquip_textView.setText("");
        extract_manufacture_textView.setText("");
        extract1_equiptype1_radioButton.setClickable(true);
        extract2_equiptype1_radioButton.setClickable(true);
        extract_soakInfo_linearLayout.setVisibility(View.GONE);
        extract_extractInfo_linearLayout.setVisibility(View.GONE);
        extract_stewInfo_linearLayout.setVisibility(View.GONE);
        extract_soak_button.setVisibility(View.GONE);
        extract_extract1_button.setVisibility(View.GONE);
        extract_extract2_button.setVisibility(View.GONE);
        extract_stew_button.setVisibility(View.GONE);
        extract_waitStew_button.setVisibility(View.GONE);
        extract_packButton_linearLayout.setVisibility(View.GONE);
        extract_finish_button.setVisibility(View.GONE);
    }
    private void handlerMessage(int what,String obj) {
//        Log.e("handlerMessage", String.valueOf(what));
//        message = null;
//        message = new Message();
//        if(handler.obtainMessage(message.what, message.obj) != null){
//            Message _msg = new Message();
//            message = _msg;
//        }
        message.what = what;
        message.obj=obj;
        handler.sendMessage(message);
    }
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
