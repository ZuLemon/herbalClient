package net.andy.boiling.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.domain.*;
import net.andy.boiling.util.*;
import net.andy.com.CoolToast;
import net.andy.com.Http;
import net.andy.com.NFCActivity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014-11-17.
 * 浸泡操作
 */
public class SoakUI extends NFCActivity {
    private TagDomain tagDomain;
    private TagDomain tagDomain1;
    private PrescriptionDomain prescriptionDomain;
    private ExtractingDomain extractingDomain;
    private EquipmentDomain equipmentDomain;
    private SoakDomain soakDomain;
    private ReturnDomain returnDomain;
    private ExtractUtil extractUtil;
    private EquipmentUtil equipmentUtil;
    private String eId;
    private String equipId;
    private String tagNo;
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
    @ViewInject(R.id.soak_waterQuantity1_textView)
    private TextView soak_waterQuantity1_textView;
    @ViewInject(R.id.soak_soakTime_textView)
    private TextView soak_soakTime_textView;
    @ViewInject(R.id.soak_soakBeginTime_textView)
    private TextView soak_soakBeginTime_textView;
    @ViewInject(R.id.soak_soakEndTime_textView)
    private TextView soak_soakEndTime_textView;
    @ViewInject(R.id.soak_submit_button)
    private Button soak_submit_button;
//    @ViewInject(R.id.equipId_textView)
//    private TextView equipId_textView;
    private RadioGroup soak_equiptype1_radioGroup;
    private RadioButton soak1_equiptype1_radioButton;
    private RadioButton soak2_equiptype1_radioButton;
    private String equipType;
    private RadioButton[] soak_equiptype1_radioButtonList;
    private int[] soak_equiptype1_valueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.soak );
        x.view().inject(this);
//        soak_patientNO_textView = ( TextView ) findViewById ( R.id.soak_patientNO_textView );
//        soak_patientName_textView = ( TextView ) findViewById ( R.id.soak_patientName_textView );
//        soak_category_textView = ( TextView ) findViewById ( R.id.soak_category_textView );
//        soak_classification_textView = ( TextView ) findViewById ( R.id.soak_classification_textView );
//        soak_diagnosis_textView = ( TextView ) findViewById ( R.id.soak_diagnosis_textView );
//        soak_presNumber_textView = ( TextView ) findViewById ( R.id.soak_presNumber_textView );
//        soak_method_textView = ( TextView ) findViewById ( R.id.soak_method_textView );
//        soak_planStatus_textView = ( TextView ) findViewById ( R.id.soak_planStatus_textView );
//        soak_waterQuantity1_textView = ( TextView ) findViewById ( R.id.soak_waterQuantity1_textView );
//        soak_soakTime_textView = ( TextView ) findViewById ( R.id.soak_soakTime_textView );
//        soak_soakBeginTime_textView = ( TextView ) findViewById ( R.id.soak_soakBeginTime_textView );
//        soak_soakEndTime_textView = ( TextView ) findViewById ( R.id.soak_soakEndTime_textView );
//        soak_equiptype1_radioGroup = ( RadioGroup ) findViewById ( R.id.soak_equiptype1_radioGroup );
        soak_equiptype1_radioButtonList = new RadioButton[]{soak1_equiptype1_radioButton, soak2_equiptype1_radioButton};
//        soak_equiptype1_valueList = new int[]{R.id.soak1_equiptype1_radioButton, R.id.soak2_equiptype1_radioButton};
//        for (int i = 0; i < soak_equiptype1_radioButtonList.length; i++) {
//            soak_equiptype1_radioButtonList[i] = ( RadioButton ) findViewById ( soak_equiptype1_valueList[i] );
//            soak_equiptype1_radioButtonList[i].setClickable ( false );
//        }
        extractUtil = new ExtractUtil ();
        equipmentUtil = new EquipmentUtil ();
//        Button soak_submit_button = ( Button ) findViewById ( R.id.soak_submit_button );
//        soak_submit_button.setOnClickListener ( new Submit () );
//        soak_equiptype1_radioGroup.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener () {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                equipType = String.valueOf ( ( ( RadioButton ) findViewById ( checkedId ) ).getText () );
//                ;
//                getWater ( tagNo );
//            }
//        } );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent ( intent );
        tagNo = this.getNfc ().readID ( intent );
        GetTagType ( tagNo );
    }

    private void check(String tagType) {
        if ( "处方".equals ( tagType ) ) {
            if ( "开始".equals ( planStatus ) ) {
//                Intent intentEquip = new Intent ();
//                intentEquip.setClass ( SoakUI.this, EquipmentUI.class );
//                startActivityForResult ( intentEquip, 1000 );
                getSoak ( tagNo );
                isPresc = true;
            } else {
                new CoolToast ( getBaseContext () ).show ( "处方处于" + planStatus + "状态" );
                getSoak ( tagNo );
            }
        } else if ( "加液机".equals ( tagType ) ) {
            if ( isPresc ) {
                new CoolToast ( getBaseContext () ).show ( "已选择加液机" );
            } else {
                new CoolToast ( getBaseContext () ).show ( "请先选择处方" );
            }
        } else {
            new CoolToast ( getBaseContext () ).show ( "请选择处方或者加液机" );
        }
    }

    /*   接收回传值         */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        System.out.println ( requestCode + "进入" + resultCode );
        if ( requestCode == 1000 && resultCode == 1001 ) {
            eId = data.getStringExtra ( "eId" );
            equipId = data.getStringExtra ( "equipId" );
            getSoak ( tagNo );
//                System.out.println (eId+"<>"+equipId);
        } else {
            new CoolToast ( getBaseContext () ).show ( "没有选择设备" );
        }
    }

    /**
     * 获取标签属性
     **/
    public void GetTagType(final String tagId) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        check ( tagType );
                        break;
                }
            }
        };
        new Thread ( tagId ) {
            @Override
            public void run() {
                try {
                    TagDomain tagDomain1 = new TagUtil ().getTagByTagId ( tagId );
                    tagType = tagDomain1.getType ();
                    String bindId = tagDomain1.getBindId ();
                    if ( "处方".equals ( tagType ) ) {
                        ExtractingDomain extractingDomain0 = extractUtil.getExtractingByPlanId ( bindId );
                        planStatus = extractingDomain0.getPlanStatus ();
                        quantity1 = extractingDomain0.getQuantity ();
                        message.what = 0;
                        handler.sendMessage ( message );
                    } else if ( "加液机".equals ( tagType ) ) {
                        Liquid = String.valueOf ( equipmentUtil.getEquipByTagId ( tagId ).getId () );
                        message.what = 0;
                        handler.sendMessage ( message );
                    } else {
                        message.what = -1;
                        message.obj = "标签无效";
                        handler.sendMessage ( message );
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }

    /**
     * 获得加液量
     *
     * @param tagId
     */
    public void getWater(final String tagId) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        System.out.println ( "$>>" + quantity1 );
                        soak_waterQuantity1_textView.setText ( String.valueOf ( quantity1 ) );
                        break;
                }
            }
        };
        new Thread ( tagId ) {
            @Override
            public void run() {

                try {
                    quantity1 = new ExtractUtil ().getWater ( tagDomain.getBindId (), equipType );
                    message.what = 0;
                    handler.sendMessage ( message );
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }

    public void getSoak(final String tagId) {
        reset ();
        final Message message = new Message ();
        final Handler handler = new Handler () {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                        reset ();
                        break;
                    case 0:
                        setValue ();
                        break;
                }
            }
        };
        new Thread ( tagId ) {
            @Override
            public void run() {

                try {
                    tagDomain = new TagUtil ().getTagByTagId ( tagId );
                    extractingDomain = extractUtil.getExtractingByPlanId ( tagDomain.getBindId () );
                    prescriptionDomain = new PrescriptionUtil ().getPrescriptionByPresId ( extractingDomain.getPresId () );
                    soakDomain = new SoakUtil ().getSoakByPlanId ( extractingDomain.getPlanId () );
                    equipmentDomain = new EquipmentUtil ().getEquipByEquipId ( equipId );
//                    if ( "开始".equals ( planStatus ) )
//                        quantity1 = new ExtractUtil ().getWater ( tagDomain.getBindId (), equipType );
                    message.what = 0;
                    handler.sendMessage ( message );
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }

    public void setValue() {
        soak_patientNO_textView.setText ( String.valueOf ( prescriptionDomain.getPatientNo () ) );
        soak_patientName_textView.setText ( String.valueOf ( prescriptionDomain.getPatientName () ) );
        soak_category_textView.setText ( String.valueOf ( prescriptionDomain.getCategory () ) );
        soak_classification_textView.setText ( String.valueOf ( prescriptionDomain.getClassification () ) );
        soak_diagnosis_textView.setText ( String.valueOf ( prescriptionDomain.getDiagnosis () ) );
        soak_presNumber_textView.setText ( String.valueOf ( prescriptionDomain.getPresNumber () ) );
        soak_method_textView.setText ( String.valueOf ( extractingDomain.getMethod () ) );
        soak_planStatus_textView.setText ( String.valueOf ( extractingDomain.getPlanStatus () ) );
        soak_waterQuantity1_textView.setText ( String.valueOf ( quantity1 ) );
        soak_soakTime_textView.setText ( String.valueOf ( extractingDomain.getSoakTime () ) );
        soak_soakBeginTime_textView.setText ( soakDomain == null ? "" : new SimpleDateFormat ( "HH:mm" ).format ( soakDomain.getBeginTime () ) );
        soak_soakEndTime_textView.setText ( soakDomain == null ? "" : new SimpleDateFormat ( "HH:mm" ).format ( soakDomain.getEndTime () ) );
//        equipId_textView.setText ( String.valueOf ( equipmentDomain.getEquipId () + equipmentDomain.getEquipPurpose () ) );
    }

    public void reset() {
        soak_patientNO_textView.setText ( "" );
        soak_patientName_textView.setText ( "" );
        soak_category_textView.setText ( "" );
        soak_classification_textView.setText ( "" );
        soak_diagnosis_textView.setText ( "" );
        soak_presNumber_textView.setText ( "" );
        soak_method_textView.setText ( "" );
        soak_planStatus_textView.setText ( "" );
        soak_waterQuantity1_textView.setText ( "" );
        soak_soakTime_textView.setText ( "" );
        soak_soakBeginTime_textView.setText ( "" );
        soak_soakEndTime_textView.setText ( "" );
        tagDomain = null;
        prescriptionDomain = null;
        extractingDomain = null;
        soakDomain = null;
    }

    @Event(R.id.soak_submit_button)
        private void onClick(View v) {
            final Message message = new Message ();
            final Handler handler = new Handler () {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case -1:
                            new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                            break;
                        case 0:
                            reset ();
                            break;
                    }
                }
            };
            new Thread () {
                @Override
                public void run() {
                    super.run ();
                    List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
                    pairs.add ( new BasicNameValuePair ( "id1", String.valueOf ( extractingDomain.getId () ) ) );
                    pairs.add ( new BasicNameValuePair ( "id2", Liquid ) );
                    try {
                        returnDomain = ( ReturnDomain ) ( new Http ().post ( "soak/insert.do", pairs, ReturnDomain.class ) );
                        if ( returnDomain.getSuccess () ) {
                            message.what = 0;
                            message.obj = "提交成功";
                            handler.sendMessage ( message );
                        } else {
                            message.what = -1;
                            message.obj = returnDomain.getException ();
                            handler.sendMessage ( message );
                        }
                    } catch (Exception e) {
                        message.what = -1;
                        message.obj = e.getMessage ();
                        handler.sendMessage ( message );
                    }
                }
            }.start ();
        }
}
