package net.andy.boiling.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.domain.*;
import net.andy.boiling.util.*;
import net.andy.com.ChineseToSpeech;
import net.andy.com.CoolToast;
import net.andy.com.NFCActivity;

/**
 * Created by Administrator on 2014-11-14.
 * 浸泡操作
 */
public class ExtractUI extends NFCActivity {
    private TextView extract_equipId_textView;
    private TextView extract_equipName_textView;
    private TextView extract_equipPurpose_textView;
    private TextView extract_equipStatus_textView;
    private TextView extract_presCode_textView;
    private TextView extract_presStatus_textView;
    private TextView extract_patientNo_textView;
    private TextView extract_patientName_textView;
    private TextView extract_category_textView;
    private TextView extract_classification_textView;
    private TextView extract_presNumber_textView;
    private TextView extract_way_textView;
    private TextView extract_temperature_textView;
    private TextView extract_pressure_textView;
    private TextView extract_quantity_textView;
    private TextView extract_planStatus_textView;
    private TextView extract_waterQuantity1_textView;
    private TextView extract_soakTime_textView;
    private TextView extract_extractTime1_textView;
    private TextView extract_waterQuantity2_textView;
    private TextView extract_extractTime2_textView;
    private TextView extract_waterQuantity3_textView;
    private TextView extract_extractTime3_textView;
    private TextView extract_status_textView;
    private Button extract_pack_button;
    private Button extract_extract1_button;
    private Button extract_extract2_button;
    private Button extract_extract3_button;
    private ReturnDomain returnDomain;
    private TagDomain tagDomain = new TagDomain ();
    private PrescriptionDomain prescriptionDomain = new PrescriptionDomain ();
    private ExtractingDomain extractingDomain = new ExtractingDomain ();
    private ExtractDomain extractDomain = new ExtractDomain ();
    private EquipmentDomain equipmentDomain = new EquipmentDomain ();
    private ChineseToSpeech speech = new ChineseToSpeech ();
    private String extractStatus;
    private String equipId;
    private String tagId;
    private String code;
    private boolean isPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.extract );
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            String planStatus = bundle.getString("planStatus");
            String planId = bundle.getString("planId");
            code = bundle.getString("code");
            System.out.println(planId + "planStatus:" + planStatus+code);
            request ( 3 ,planId);
        }
        getWidget ();
        bindEvent ();
    }

    public void getWidget() {
        extract_equipId_textView = ( TextView ) findViewById ( R.id.extract_equipId_textView );
        extract_equipName_textView = ( TextView ) findViewById ( R.id.extract_equipName_textView );
        extract_equipPurpose_textView = ( TextView ) findViewById ( R.id.extract_equipPurpose_textView );
        extract_equipStatus_textView = ( TextView ) findViewById ( R.id.extract_equipStatus_textView );
        extract_presCode_textView = ( TextView ) findViewById ( R.id.extract_presCode_textView );
        extract_presStatus_textView = ( TextView ) findViewById ( R.id.extract_presStatus_textView );
        extract_patientNo_textView = ( TextView ) findViewById ( R.id.extract_patientNo_textView );
        extract_patientName_textView = ( TextView ) findViewById ( R.id.extract_patientName_textView );
        extract_category_textView = ( TextView ) findViewById ( R.id.extract_category_textView );
        extract_classification_textView = ( TextView ) findViewById ( R.id.extract_classification_textView );
        extract_presNumber_textView = ( TextView ) findViewById ( R.id.extract_presNumber_textView );
        extract_way_textView = ( TextView ) findViewById ( R.id.extract_way_textView );
        extract_temperature_textView = ( TextView ) findViewById ( R.id.extract_temperature_textView );
        extract_pressure_textView = ( TextView ) findViewById ( R.id.extract_pressure_textView );
        extract_quantity_textView = ( TextView ) findViewById ( R.id.extract_quantity_textView );
        extract_planStatus_textView = ( TextView ) findViewById ( R.id.extract_planStatus_textView );
        extract_waterQuantity1_textView = ( TextView ) findViewById ( R.id.extract_waterQuantity1_textView );
        extract_soakTime_textView = ( TextView ) findViewById ( R.id.extract_soakTime_textView );
        extract_extractTime1_textView = ( TextView ) findViewById ( R.id.extract_extractTime1_textView );
        extract_waterQuantity2_textView = ( TextView ) findViewById ( R.id.extract_waterQuantity2_textView );
        extract_extractTime2_textView = ( TextView ) findViewById ( R.id.extract_extractTime2_textView );
        extract_waterQuantity3_textView = ( TextView ) findViewById ( R.id.extract_waterQuantity3_textView );
        extract_extractTime3_textView = ( TextView ) findViewById ( R.id.extract_extractTime3_textView );
        extract_status_textView = ( TextView ) findViewById ( R.id.extract_status_textView );
        extract_pack_button = ( Button ) findViewById ( R.id.extract_pack_button );
        extract_extract1_button = ( Button ) findViewById ( R.id.extract_extract1_button );
        extract_extract2_button = ( Button ) findViewById ( R.id.extract_extract2_button );
        extract_extract3_button = ( Button ) findViewById ( R.id.extract_extract3_button );
    }

    public void bindEvent() {
        extract_extract1_button.setOnClickListener ( new Submit () );
        extract_extract2_button.setOnClickListener ( new Submit () );
        extract_pack_button.setOnClickListener ( new Submit () );
//        extract_extract3_button.setOnClickListener ( new Sub());
    }

    class Submit implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId ()) {
                case R.id.extract_extract1_button:
                    extractStatus = "一煎";
                    request ( 1,null );
                    break;
                case R.id.extract_extract2_button:
                    extractStatus = "二煎";
                    request ( 1 ,null);
                    break;
                case R.id.extract_pack_button:
                    request ( 2,null );
                    break;
//                case R.id.extract_extract3_button:
//                    extractStatus="三煎";
            }

        }
    }
    public void setWidget() {
        try {
            extract_equipId_textView.setText ( equipmentDomain.getEquipId () );
            extract_equipName_textView.setText ( equipmentDomain.getEquipName () );
            extract_equipPurpose_textView.setText ( equipmentDomain.getEquipPurpose () );
            extract_equipStatus_textView.setText ( equipmentDomain.getEquipStatus () );
            if("".equals(tagDomain.getCode())){
                extract_presCode_textView.setText(code);
            }else {
                extract_presCode_textView.setText(tagDomain.getCode());
                extract_presStatus_textView.setText(tagDomain.getStatus());
            }
                extract_patientNo_textView.setText ( prescriptionDomain.getPatientNo () );
            extract_patientName_textView.setText ( prescriptionDomain.getPatientName () );
            extract_category_textView.setText ( prescriptionDomain.getCategory () );
            extract_classification_textView.setText ( prescriptionDomain.getClassification () );
            extract_presNumber_textView.setText ( prescriptionDomain.getPresNumber ().toString () );
            extract_way_textView.setText ( prescriptionDomain.getWay () );
            extract_temperature_textView.setText ( extractingDomain.getTemperature ().toString () );
            extract_pressure_textView.setText ( extractingDomain.getPressure () );
            extract_quantity_textView.setText ( extractingDomain.getQuantity ().toString () );
            extract_planStatus_textView.setText ( extractingDomain.getPlanStatus () );
            extract_soakTime_textView.setText ( extractingDomain.getSoakTime ().toString () );
            extract_extractTime1_textView.setText ( extractingDomain.getExtractTime1 ().toString () );
            extract_extractTime2_textView.setText ( extractingDomain.getExtractTime2 ().toString () );
            extract_extractTime3_textView.setText ( extractingDomain.getExtractTime3 ().toString () );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void resetObject() {
        tagDomain = new TagDomain ();
        prescriptionDomain = new PrescriptionDomain ();
        extractingDomain = new ExtractingDomain ();
        equipmentDomain = new EquipmentDomain ();
    }

    public void request(int what,String planId) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                        resetObject ();
                        setWidget ();
                        break;
                    case 0:
                        setWidget ();
                        break;
                    case 1:
                        speech.speech ( "开始" + extractStatus );
                        break;
                    case 2:
                        speech.speech ( "开始包装" );
                        break;
                }
            }
        };

        new Thread () {
            @Override
            public void run() {
                super.run ();
                try {
                    switch (what) {
                        case 0:
                            judge ();
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                        case 1:
                            startExtract ();
                            message.what = 1;
                            message.obj = "";
                            handler.sendMessage ( message );
                            break;
                        case 2:
                            startPack ();
                            message.what = 2;
                            message.obj = "";
                            handler.sendMessage ( message );
                            break;
                        case 3:
                            IsPre(planId);
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                        case 4:
                            break;
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }

    private void startExtract() throws Exception {
        new ExtractUtil ().insert ( extractingDomain.getPlanId (), equipmentDomain.getEquipId (), extractStatus );
    }

    private void startPack() throws Exception {
        new PackUtil ().insert ( extractingDomain.getPlanId () );
    }

    private void IsPre(String PlanId) throws Exception {
        tagDomain = new TagUtil ().getTagByTagId ( tagId );
        extractingDomain = new ExtractUtil ().getExtractingByPlanId ( PlanId );
        String planStatus=extractingDomain.getPlanStatus();
        if ( "浸泡".equals ( planStatus ) ) {
            extract_extract1_button.setClickable ( true );
            extract_pack_button.setClickable ( false );
        }
        if ( "一煎".equals ( planStatus ) ) {
            extract_extract1_button.setClickable ( false );
            extract_pack_button.setClickable ( true );
        }
        if ( "二煎".equals ( planStatus ) ) {
            extract_extract2_button.setClickable ( false );
            extract_pack_button.setClickable ( true );
        }
        equipmentDomain = new EquipmentUtil ().getEquipByPlanId ( PlanId );
        prescriptionDomain = new PrescriptionUtil ().getPrescriptionByPresId ( extractingDomain.getPresId () );
        isPre = true;
    }

    /**
     * 识别
     *
     * @throws Exception
     */
    private void judge() throws Exception {
        tagDomain = new TagUtil ().getTagByTagId ( tagId );
        if ( "处方".equals ( tagDomain.getType () ) ) {
            IsPre(tagDomain.getBindId());
        }
        if ( isPre ) {
            if ( "煎药机".equals ( tagDomain.getType () ) ) {
                equipmentDomain = new EquipmentUtil ().getEquipByTagId ( tagId );
                String equipType = equipmentDomain.getEquipType1 ();
                System.out.println ( equipmentDomain.getEquipId () );
                if ( equipType.equals ( extractingDomain.getSoakType () ) && equipType != null ) {
                    speech.speech ( "可以开始煎药" );

                } else {
                    speech.speech ( "请选择正确的煎药机" );
                }
            } else {
                speech.speech ( "请选择煎药机" );
            }
        } else {
            speech.speech ( "请先选择处方" );
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent ( intent );
        tagId = getNfc ().readID ( intent );
        request ( 0 ,null);
    }

    @Override
    protected void onDestroy() {
        if ( speech != null ) {
            speech.destroy ();
        }
        super.onDestroy ();
    }
}
