package net.andy.boiling.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.R;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.domain.TagDomain;
import net.andy.com.CoolToast;
import net.andy.com.Http;
import net.andy.com.NFCActivity;
import net.andy.dispensing.util.ColorItem;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014-11-15.
 * 标签管理
 */

public class TagUI extends NFCActivity {
    private TagDomain tagDomain;
    private ReturnDomain returnDomain;
    @ViewInject(R.id.tag_code_button)
    private Button tag_code_button;
    @ViewInject(R.id.tag_tagId_editText)
    private EditText tag_tagId_editText;
    @ViewInject(R.id.tag_code_editText)
    private EditText tag_code_editText;
    @ViewInject(R.id.tag_bindId_editText)
    private EditText tag_bindId_editText;
    @ViewInject(R.id.tag_type1_radioGroup)
    private RadioGroup tag_type1_radioGroup;
    @ViewInject(R.id.tag_type2_radioGroup)
    private RadioGroup tag_type2_radioGroup;
    @ViewInject(R.id.tag_status_radioGroup)
    private RadioGroup tag_status_radioGroup;
    @ViewInject(R.id.tag_type1_radioButton)
    private RadioButton tag_type1_radioButton;
    @ViewInject(R.id.tag_type2_radioButton)
    private RadioButton tag_type2_radioButton;
    @ViewInject(R.id.tag_type3_radioButton)
    private RadioButton tag_type3_radioButton;
    @ViewInject(R.id.tag_status1_radioButton)
    private RadioButton tag_status1_radioButton;
    @ViewInject(R.id.tag_status2_radioButton)
    private RadioButton tag_status2_radioButton;
    @ViewInject(R.id.tag_status3_radioButton)
    private RadioButton tag_status3_radioButton;
    @ViewInject(R.id.tag_type4_radioButton)
    private RadioButton tag_type4_radioButton;
    @ViewInject(R.id.tag_type5_radioButton)
    private RadioButton tag_type5_radioButton;
    @ViewInject(R.id.tag_ok_button)
    private Button tag_ok_button;
    private List<ColorItem> colorItemList = new ArrayList<>();
    private Spinner spinner;
    private ArrayAdapter<ColorItem> adapter;
    private String color;
    private String value;
    private final static int SCANNIN_GREQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);
        x.view().inject(this);
        colorItemList.add(new ColorItem("红色", "#ffFF0000"));
        colorItemList.add(new ColorItem("绿色", "#ff00FF00"));
        colorItemList.add(new ColorItem("蓝色", "#ff0099FF"));
        colorItemList.add(new ColorItem("黄色", "#ffFFFF00"));
        colorItemList.add(new ColorItem("棕色", "#ff8a6f20"));
        colorItemList.add(new ColorItem("粉色", "#ffFF1CAE"));
        spinner = (Spinner) findViewById(R.id.tag_color_spinner);
        adapter = new ArrayAdapter<ColorItem>(this, android.R.layout.simple_spinner_item, colorItemList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new ItemSelectedListenerImpl());

    }

    /**
     * 读取二维码并返回数据
     */
    private void jump() {
        Intent intent = new Intent();
        intent.setClass(TagUI.this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    private class ItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
            color = ((ColorItem) spinner.getSelectedItem()).getColor();
            value = ((ColorItem) spinner.getSelectedItem()).getValue();
            spinner.setBackgroundColor(Color.parseColor(value));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getTag(this.getNfc().readID(intent));
    }

    public void reset() {
        tag_tagId_editText.setText("");
        tag_code_editText.setText("");
        tag_bindId_editText.setText("");
//        tag_type_radioGroup.clearCheck();
//        tag_status_radioGroup.clearCheck();
//        spinner.setSelection(0);
    }

    /*   接收回传值         */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == MipcaActivityCapture.Capture_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    tag_code_editText.setText(bundle.getString("result"));
                }
                break;
        }
    }
    @Event(value = R.id.tag_type1_radioGroup,type = RadioGroup.OnCheckedChangeListener.class)
    private void onChecked1Changed(RadioGroup group, int checkedId) {
        tag_type3_radioButton.setChecked(false);
        tag_type4_radioButton.setChecked(false);
        tag_type5_radioButton.setChecked(false);
    }
    @Event(value = R.id.tag_type2_radioGroup,type = RadioGroup.OnCheckedChangeListener.class)
    private void onChecked2Changed(RadioGroup group, int checkedId) {
        tag_type1_radioButton.setChecked(false);
        tag_type2_radioButton.setChecked(false);
    }
    public void setValue(String tagId) {
        tag_tagId_editText.setText(tagId);
        tag_code_editText.setText(tagDomain.getCode());
        tag_bindId_editText.setText(tagDomain.getBindId());
        if (tagDomain.getType() == null) {
            tag_type1_radioGroup.clearCheck();
            tag_type2_radioGroup.clearCheck();
        } else {
            tag_type1_radioButton.setChecked(tag_type1_radioButton.getText().equals(tagDomain.getType()));
            tag_type2_radioButton.setChecked(tag_type2_radioButton.getText().equals(tagDomain.getType()));
            tag_type3_radioButton.setChecked(tag_type3_radioButton.getText().equals(tagDomain.getType()));
            tag_type4_radioButton.setChecked(tag_type4_radioButton.getText().equals(tagDomain.getType()));
            tag_type5_radioButton.setChecked(tag_type5_radioButton.getText().equals(tagDomain.getType()));
        }
        if (tagDomain.getStatus() == null) {
            tag_status_radioGroup.clearCheck();
        } else {
            tag_status1_radioButton.setChecked(tag_status1_radioButton.getText().equals(tagDomain.getStatus()));
            tag_status2_radioButton.setChecked(tag_status2_radioButton.getText().equals(tagDomain.getStatus()));
            tag_status3_radioButton.setChecked(tag_status3_radioButton.getText().equals(tagDomain.getStatus()));
        }
        if (tagDomain.getColor() == null) {
            spinner.setSelection(0);
        } else {
            setSpinnerItemSelectedByValue(spinner, tagDomain.getColor());
        }
    }

    public String getType() {
        if (tag_type1_radioButton.isChecked())
            return (String) tag_type1_radioButton.getText();
        if (tag_type2_radioButton.isChecked())
            return (String) tag_type2_radioButton.getText();
        if (tag_type3_radioButton.isChecked())
            return (String) tag_type3_radioButton.getText();
        if (tag_type4_radioButton.isChecked())
            return (String) tag_type4_radioButton.getText();
        if (tag_type5_radioButton.isChecked())
            return (String) tag_type5_radioButton.getText();
        return "";
    }

    public String getStatus() {
        if (tag_status1_radioButton.isChecked())
            return (String) tag_status1_radioButton.getText();
        if (tag_status2_radioButton.isChecked())
            return (String) tag_status2_radioButton.getText();
        if (tag_status3_radioButton.isChecked())
            return (String) tag_status3_radioButton.getText();
        return "";
    }

    public void getTag(final String tagId) {
        final Message message = new Message();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        // reset();
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        tag_tagId_editText.setText(tagId);
                        break;
                    case 0:
                        setValue(tagId);
                        break;
                    case 2:
                        tag_tagId_editText.setText(tagId);
                        break;
                }
            }
        };
        new Thread(tagId) {
            @Override
            public void run() {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("tagId", tagId));
                try {
                    returnDomain = (ReturnDomain) (new Http().post("tag/getTagByTagId.do", pairs, ReturnDomain.class));
                    if (returnDomain.getSuccess()) {
                        tagDomain = JSON.parseObject(returnDomain.getObject().toString(), TagDomain.class);
                        if ("".equals(tagDomain.getCode())) {
                            message.what = 2;
                            handler.sendMessage(message);
                        } else {
                            message.what = 0;
                            handler.sendMessage(message);
                        }
                    } else {
                        message.what = -1;
                        message.obj = returnDomain.getException();
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param value
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

        @Event(value ={R.id.tag_ok_button,R.id.tag_code_button},type = View.OnClickListener.class)
        private void OKClick(View v) {
            switch (v.getId()){
                case R.id.tag_ok_button:
                    submit();
                    break;
                case R.id.tag_code_button:
                    jump();
                    break;
            }

        }
        private void submit(){
            if (("".equals(tag_code_editText.getText().toString())) || ("".equals(tag_tagId_editText.getText().toString())) || tag_tagId_editText.getText().toString() == "" || tag_code_editText.getText().toString() == "") {
                new CoolToast(getBaseContext()).show("标签ID或号码不能为空");
                return;
            }

            final Message message = new Message();
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    reset();
                    new CoolToast(getBaseContext()).show((String) msg.obj);
                }
            };

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    if (tagDomain == null) {
                        tagDomain = new TagDomain();
                        tagDomain.setId(0);
                    } else {
                        pairs.add(new BasicNameValuePair("id", String.valueOf(tagDomain.getId())));
                    }
                    pairs.add(new BasicNameValuePair("tagId", String.valueOf(tag_tagId_editText.getText())));
                    pairs.add(new BasicNameValuePair("code", String.valueOf(tag_code_editText.getText())));
                    pairs.add(new BasicNameValuePair("color", color));
                    pairs.add(new BasicNameValuePair("colorValue", value));
                    pairs.add(new BasicNameValuePair("type", getType()));
                    pairs.add(new BasicNameValuePair("status", getStatus()));
                    pairs.add(new BasicNameValuePair("bindId", String.valueOf(tag_bindId_editText.getText())));
                    try {
                        returnDomain = (ReturnDomain) new Http().post(tagDomain.getId() == 0 ? "tag/insert.do" : "tag/update.do", pairs, ReturnDomain.class);
                        message.obj = "保存成功";
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        message.obj = e.getMessage();
                        handler.sendMessage(message);
                    }
                }
            }.start();
        }
}
