package net.andy.boiling.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import net.andy.boiling.R;
import net.andy.boiling.domain.EquipmentDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.util.EquipmentUtil;
import net.andy.boiling.util.TagUtil;
import net.andy.com.CoolToast;
import net.andy.com.NFCActivity;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 设备修改
 *
 * @author RongGuang
 * @date 2015/12/2
 */
public class EquipReviseUI extends NFCActivity {
    private EquipmentDomain equipmentDomain;
    private EquipmentDomain equipmentDomainNew;
    @ViewInject(R.id.equip_tagId_editText)
    private EditText equip_tagId_editText;
    @ViewInject(R.id.equip_equipId_editText)
    private EditText equip_equipId_editText;
    @ViewInject(R.id.equip_equipName_editText)
    private EditText equip_equipName_editText;
    @ViewInject(R.id.equip_type_radioGroup)
    private RadioGroup equip_type_radioGroup;
    @ViewInject(R.id.equip_type1_radioGroup)
    private RadioGroup equip_type1_radioGroup;
    @ViewInject(R.id.equip_equipPurpose_radioGroup)
    private RadioGroup equip_equipPurpose_radioGroup;
    @ViewInject(R.id.equip1_equipStatus_radioGroup)
    private RadioGroup equip1_equipStatus_radioGroup;
    @ViewInject(R.id.equip2_equipStatus_radioGroup)
    private RadioGroup equip2_equipStatus_radioGroup;
    @ViewInject(R.id.equip1_type_radioButton)
    private RadioButton equip1_type_radioButton;
    @ViewInject(R.id.equip2_type_radioButton)
    private RadioButton equip2_type_radioButton;
    @ViewInject(R.id.equip3_type_radioButton)
    private RadioButton equip3_type_radioButton;
    @ViewInject(R.id.equip4_type_radioButton)
    private RadioButton equip4_type_radioButton;
    @ViewInject(R.id.equip1_type1_radioButton)
    private RadioButton equip1_type1_radioButton;
    @ViewInject(R.id.equip2_type1_radioButton)
    private RadioButton equip2_type1_radioButton;
    @ViewInject(R.id.equip1_equipPurpose_radioButton)
    private RadioButton equip1_equipPurpose_radioButton;
    @ViewInject(R.id.equip2_equipPurpose_radioButton)
    private RadioButton equip2_equipPurpose_radioButton;
    @ViewInject(R.id.equip1_equipStatus_radioButton)
    private RadioButton equip1_equipStatus_radioButton;
    @ViewInject(R.id.equip2_equipStatus_radioButton)
    private RadioButton equip2_equipStatus_radioButton;
    @ViewInject(R.id.equip3_equipStatus_radioButton)
    private RadioButton equip3_equipStatus_radioButton;
    @ViewInject(R.id.equip4_equipStatus_radioButton)
    private RadioButton equip4_equipStatus_radioButton;
    @ViewInject(R.id.equip5_equipStatus_radioButton)
    private RadioButton equip5_equipStatus_radioButton;
    @ViewInject(R.id.equip6_equipStatus_radioButton)
    private RadioButton equip6_equipStatus_radioButton;
    private String tagId;
    private String tagType;
    private String equipType;
    @ViewInject(R.id.equip_ok_button)
    private Button equip_ok_button;
    private RadioButton[] equip_type_radioButtonList;
    private int[] equip_type_valueList;
    private RadioButton[] equip_equipPurpose_radioButtonList;
    private int[] equip_equipPurpose_valueList;
    private RadioButton[] equip_type1_radioButtonList;
    private int[] equip_type1_valueList;
    private RadioButton[] equip_equipStatus_radioButtonList;
    private int[] equip_equipStatus_valueList;
    private boolean radioGroup1Checked = true;
    private boolean radioGroup2Checked = false;
    private final static String TAG="EquipReviseUI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.equiprevise );
        x.view().inject(this);
//        equip_tagId_editText = ( EditText ) findViewById ( R.id.equip_tagId_editText );
//        equip_equipId_editText = ( EditText ) findViewById ( R.id.equip_equipId_editText );
//        equip_equipName_editText = ( EditText ) findViewById ( R.id.equip_equipName_editText );
//        equip_type_radioGroup = ( RadioGroup ) findViewById ( R.id.equip_type_radioGroup );
//        equip_type1_radioGroup = ( RadioGroup ) findViewById ( R.id.equip_type1_radioGroup );
//        equip_equipPurpose_radioGroup = ( RadioGroup ) findViewById ( R.id.equip_equipPurpose_radioGroup );
//        equip1_equipStatus_radioGroup = ( RadioGroup ) findViewById ( R.id.equip1_equipStatus_radioGroup );
//        equip2_equipStatus_radioGroup = ( RadioGroup ) findViewById ( R.id.equip2_equipStatus_radioGroup );
//        equip_ok_button = ( Button ) findViewById ( R.id.equip_ok_button );
        equip_type_radioButtonList = new RadioButton[]{equip1_type_radioButton, equip2_type_radioButton, equip3_type_radioButton,equip4_type_radioButton};
        equip_type_valueList = new int[]{R.id.equip1_type_radioButton, R.id.equip2_type_radioButton, R.id.equip3_type_radioButton,R.id.equip4_type_radioButton};
        equip_type1_radioButtonList = new RadioButton[]{equip1_type1_radioButton, equip2_type1_radioButton};
        equip_type1_valueList = new int[]{R.id.equip1_type1_radioButton, R.id.equip2_type1_radioButton};
        equip_equipPurpose_radioButtonList = new RadioButton[]{equip1_equipPurpose_radioButton, equip2_equipPurpose_radioButton};
        equip_equipPurpose_valueList = new int[]{R.id.equip1_equipPurpose_radioButton, R.id.equip2_equipPurpose_radioButton};
        equip_equipStatus_radioButtonList = new RadioButton[]{equip1_equipStatus_radioButton, equip2_equipStatus_radioButton, equip3_equipStatus_radioButton, equip4_equipStatus_radioButton, equip5_equipStatus_radioButton, equip6_equipStatus_radioButton};
        equip_equipStatus_valueList = new int[]{R.id.equip1_equipStatus_radioButton, R.id.equip2_equipStatus_radioButton, R.id.equip3_equipStatus_radioButton, R.id.equip4_equipStatus_radioButton, R.id.equip5_equipStatus_radioButton, R.id.equip6_equipStatus_radioButton};
        for (int i = 0; i < equip_type_radioButtonList.length; i++)
            equip_type_radioButtonList[i] = ( RadioButton ) findViewById ( equip_type_valueList[i] );
        for (int i = 0; i < equip_type1_radioButtonList.length; i++)
            equip_type1_radioButtonList[i] = ( RadioButton ) findViewById ( equip_type1_valueList[i] );
        for (int i = 0; i < equip_equipPurpose_radioButtonList.length; i++)
            equip_equipPurpose_radioButtonList[i] = ( RadioButton ) findViewById ( equip_equipPurpose_valueList[i] );
        for (int i = 0; i < equip_equipStatus_radioButtonList.length; i++)
            equip_equipStatus_radioButtonList[i] = ( RadioButton ) findViewById ( equip_equipStatus_valueList[i] );
        equipmentDomain=new EquipmentDomain ();
        Intent intent = getIntent ();
        System.out.println ( "$>>" + intent.getStringExtra ( "id" ) );
        String id = intent.getStringExtra ( "id" );
        if ( null != id ) {
            Init ( id );
        }
        equip_type_radioGroup.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                String typeName = String.valueOf ( ( ( RadioButton ) findViewById ( checkedId ) ).getText () );
                if ( !"煎药机".equals ( typeName ) ) {
                    equip_type1_radioGroup.setVisibility ( View.GONE );
                    equip_equipPurpose_radioGroup.setVisibility ( View.GONE );
                    equipmentDomain.setEquipPurpose ( "" );
                    equipmentDomain.setEquipType1 ( "" );
                } else {
                    equip_type1_radioGroup.setVisibility ( View.VISIBLE );
                    equip_equipPurpose_radioGroup.setVisibility ( View.VISIBLE );
                }
                equipmentDomain.setEquipType ( typeName );
            }
        } );
        equip_type1_radioGroup.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                equipmentDomain.setEquipType1 ( String.valueOf ( ( ( RadioButton ) findViewById ( checkedId ) ).getText () ) );
            }
        } );
        equip_equipPurpose_radioGroup.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                equipmentDomain.setEquipPurpose ( String.valueOf ( ( ( RadioButton ) findViewById ( checkedId ) ).getText () ) );
            }
        });
    }

    /**
     * 初始化
     **/
    private void Init(String id) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        SetInitValue ( equipmentDomain );
                        break;
                }
            }
        };
        new Thread () {
            @Override
            public void run() {
                try {
                    equipmentDomain = new EquipmentUtil ().getEquipment ( id );
                    message.what = 0;
                    message.obj = "success";
                    handler.sendMessage ( message );
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }
    private void SetInitValue(EquipmentDomain equipmentDomain) {
        Log.i(TAG,"SetInitValue");
        equip_tagId_editText.setText ( equipmentDomain.getTagId () );
        equip_equipId_editText.setText ( equipmentDomain.getEquipId () );
        equip_equipName_editText.setText ( equipmentDomain.getEquipName () );
        for (RadioButton rb : equip_type_radioButtonList) {
//            rb.setClickable ( false );
            if ( rb.getText ().equals ( equipmentDomain.getEquipType () ) ) {
                rb.setChecked ( true );
                break;
            }
        }
        for (RadioButton rb : equip_type1_radioButtonList) {
            if ( rb.getText ().equals ( equipmentDomain.getEquipType1 () ) ) {
                rb.setChecked ( true );
                break;
            }
        }
        for (RadioButton rb : equip_equipPurpose_radioButtonList) {
            if ( rb.getText ().equals ( equipmentDomain.getEquipPurpose () ) ) {
                rb.setChecked ( true );
                break;
            }
        }
        for (RadioButton rb : equip_equipStatus_radioButtonList) {
            Log.i(TAG,rb.getText ()+"");
            if ( rb.getText ().equals ( equipmentDomain.getEquipStatus () ) ) {
                rb.setChecked ( true );
                break;
            }
        }
    }

    private void reset(){
//        equipmentDomain=null;
        equip_tagId_editText.setText("");
        equip_equipId_editText.setText("");
        equip_equipName_editText.setText("");
//        equip_type_radioGroup.clearCheck();
//        equip_type1_radioGroup.clearCheck();
//        equip_equipPurpose_radioGroup.clearCheck();
//        equip1_equipStatus_radioGroup.clearCheck();
//        equip2_equipStatus_radioGroup.clearCheck();
    }
         @Event(R.id.equip_ok_button)
        private void btnClick(View v) {
            GetValues ();
            if ("".equals ( equipmentDomain.getTagId () )) {
                new CoolToast ( getBaseContext () ).show ( "标签不能为空" );
                return;
            }
            final Message message = new Message();
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case -1:
                            new CoolToast(getBaseContext()).show((String) msg.obj);
                            break;
                        case 0:
                            new CoolToast(getBaseContext()).show((String) msg.obj);
                            reset();
                            break;
                    }
                }
            };
            new Thread () {
                @Override
                public void run() {
                    super.run ();
                    try {
                        new EquipmentUtil ().insertEquipment ( equipmentDomain );
                        message.what=0;
                        message.obj="保存成功";
                        handler.sendMessage ( message );
                    } catch (Exception e) {
                        message.what=-1;
                        message.obj = e.getMessage ();
                        handler.sendMessage ( message );
                    }
                }
            }.start ();
        }
    private void GetTagType(String tagId) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case 0:
                        SetInitValue ( equipmentDomainNew );
                        break;
                    case -1:
                        SetText ();
                        break;
                }
            }
        };
        new Thread () {
            @Override
            public void run() {
                super.run ();
                try {
                    tagType = new TagUtil ().getTagTypeByTagId ( tagId );
                    equipmentDomainNew = new EquipmentUtil ().getEquipByTagId ( tagId );
                    message.what = 0;
                    message.obj = "";
                    handler.sendMessage ( message );
                } catch (Exception e) {
                    Log.i(TAG,"Exception");
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start ();
    }
    private void SetText() {
        Log.i(TAG,"SetText"+tagType);
        if ( "处方".equals ( tagType ) ) {
            new CoolToast ( getBaseContext () ).show ( "该标签已绑定处方" );
            onBackPressed ();
        } else {
            for (RadioButton rb : equip_type_radioButtonList) {
//                rb.setClickable ( false );
                if ( rb.getText ().equals ( tagType ) ) {
                    rb.setChecked ( true );
                }
            }
        }
    }
    private void GetValues(){
        equipmentDomain.setEquipId (String.valueOf ( equip_equipId_editText.getText () ));
        equipmentDomain.setTagId ( String.valueOf ( equip_tagId_editText.getText () ) );
        equipmentDomain.setEquipName ( String.valueOf ( equip_equipName_editText.getText () ) );
    }
    @Event(value = R.id.equip1_equipStatus_radioGroup,type = RadioGroup.OnCheckedChangeListener.class)
    private void onChecked1Changed(RadioGroup group, int checkedId) {
        Log.i(">>","onChecked1Changed");
            if ( radioGroup2Checked ) {
                radioGroup2Checked = false;
                radioGroup1Checked = false;
                ( ( RadioButton ) findViewById ( checkedId ) ).setChecked ( true );
                equip_equipStatus_radioButtonList[3].setChecked ( false );
                equip_equipStatus_radioButtonList[4].setChecked ( false );
                equip_equipStatus_radioButtonList[5].setChecked ( false );
                radioGroup1Checked = true;
            }
            equipmentDomain.setEquipStatus ( String.valueOf ( ( ( RadioButton ) findViewById ( checkedId ) ).getText () ));
    }

        @Event(value = R.id.equip2_equipStatus_radioGroup,type = RadioGroup.OnCheckedChangeListener.class)
        private void onChecked2Changed(RadioGroup group, int checkedId) {
            Log.i(">>","onChecked2Changed");
            if ( radioGroup1Checked ) {
                radioGroup2Checked = false;
                radioGroup1Checked = false;
                ( ( RadioButton ) findViewById ( checkedId ) ).setChecked ( true );
                equip_equipStatus_radioButtonList[0].setChecked ( false );
                equip_equipStatus_radioButtonList[1].setChecked ( false );
                equip_equipStatus_radioButtonList[2].setChecked ( false );
                radioGroup2Checked = true;
            }
            equipmentDomain.setEquipStatus ( String.valueOf ( ( ( RadioButton ) findViewById ( checkedId ) ).getText () ));
        }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent ( intent );
        tagId = this.getNfc ().readID ( intent );
        GetTagType ( tagId );
        setValue ( tagId );
    }
    public void setValue(String tagId) {
        equip_tagId_editText.setText ( tagId );
    }
}
