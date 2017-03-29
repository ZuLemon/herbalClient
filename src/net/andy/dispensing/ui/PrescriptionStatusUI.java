package net.andy.dispensing.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.PrescriptionStatusUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 处方流程状态（待调、调剂、检验）
 * User: Guang
 * Date: 2016/7/1
 */
public class PrescriptionStatusUI extends Activity {
    @ViewInject(R.id.prescriptionStatus_linearLayout)
    private LinearLayout prescriptionStatus_linearLayout;
    @ViewInject(R.id.prescriptionStatus_status_textView)
    private TextView prescriptionStatus_status_textView;
    @ViewInject(R.id.prescriptionStatus_did_textView)
    private TextView prescriptionStatus_did_textView;
    @ViewInject(R.id.prescriptionStatus_waitCount_textView)
    private TextView prescriptionStatus_waitCount_textView;
    @ViewInject(R.id.prescriptionStatus_waitTime_textView)
    private TextView prescriptionStatus_waitTime_textView;
    @ViewInject(R.id.prescriptionStatus_dispensingTime_textView)
    private TextView prescriptionStatus_dispensingTime_textView;
    @ViewInject(R.id.prescriptionStatus_tagCode_textView)
    private TextView prescriptionStatus_tagCode_textView;
    @ViewInject(R.id.prescriptionStatus_insTime_textView)
    private TextView prescriptionStatus_insTime_textView;
    @ViewInject(R.id.prescriptionStatus_dispensingPerson_textView)
    private TextView prescriptionStatus_dispensingPerson_textView;
    @ViewInject(R.id.prescriptionStatus_insPerson_textView)
    private TextView prescriptionStatus_insPerson_textView;
    @ViewInject(R.id.prescriptionStatus_image_button)
    private Button prescriptionStatus_image_button;
    private Integer pId;
    private String presId;
    private Map statusMap;
    private Map prescriptionMap;
    private Map mainMap;
    private Map attachMap;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescriptionstatus);
        x.view().inject(this);
        Intent in = getIntent();
        pId = in.getIntExtra("id", 0);
        presId=in.getStringExtra("presId");
        PrescriptionStausThread(0);
        Log.e("id", String.valueOf(pId));
    }

    @Event(value = {R.id.prescriptionStatus_linearLayout,
            R.id.prescriptionStatus_image_button,
            R.id.prescriptionStatus_detail_button}, type = View.OnClickListener.class)
    private void btnClick(View view) {
        switch (view.getId()) {
            case R.id.prescriptionStatus_linearLayout:
                finish();
                break;
            case R.id.prescriptionStatus_image_button:
                Intent intent = new Intent(PrescriptionStatusUI.this, PhotoViewUI.class);
                intent.putExtra("pId", pId);
                startActivity(intent);
                break;
            case R.id.prescriptionStatus_detail_button:
                Intent in = new Intent(PrescriptionStatusUI.this, AlreadyDisDetailUI.class);
                in.putExtra("presId", presId);
                startActivity(in);
                break;
        }
    }

    private void praseData() {
        prescriptionMap = (Map) statusMap.get("prescription");
        if (prescriptionMap == null) {
            new CoolToast(getBaseContext()).show("未查询到处方信息");
            finish();
        } else {
            //有细药
            if (Integer.parseInt(prescriptionMap.get("valuablesCnt").toString()) > 0) {
                mainMap = (Map) statusMap.get("attach");
            }
            //有主处方
            if (! prescriptionMap.get("herbCnt").toString().equals(prescriptionMap.get("valuablesCnt").toString()) && ! prescriptionMap.get("herbCnt").toString().equals("0")) {
                mainMap = (Map) statusMap.get("main");
                String mainStatus = mainMap.get("status").toString();
                if ("新处方".equals(mainStatus)) {
                    prescriptionStatus_status_textView.setText("未调剂");
                    prescriptionStatus_image_button.setVisibility(View.GONE);
                    prescriptionStatus_waitCount_textView.setVisibility(View.VISIBLE);
                    prescriptionStatus_waitCount_textView.setText("前面有 " + mainMap.get("waitCt").toString() + " 个处方等候");
                } else if ("调剂中".equals(mainStatus) || "暂停".equals(mainStatus)) {
                    prescriptionStatus_image_button.setVisibility(View.GONE);
                    Map dispensingMap = (Map) mainMap.get("dispensing");
                    Date subTime = null;
                    try {
                        subTime = dateFormat.parse(prescriptionMap.get("subTime").toString());
                        Date beginTime = dateFormat.parse(dispensingMap.get("beginTime").toString());
                        prescriptionStatus_status_textView.setText(mainStatus + " 已完成" + mainMap.get("progress"));
                        prescriptionStatus_waitTime_textView.setVisibility(View.VISIBLE);
                        prescriptionStatus_dispensingPerson_textView.setVisibility(View.VISIBLE);
                        prescriptionStatus_did_textView.setVisibility(View.VISIBLE);
                        prescriptionStatus_did_textView.setText("  调剂 ID ：" + dispensingMap.get("id").toString());
                        prescriptionStatus_waitTime_textView.setText("候方用时：" + (beginTime.getTime() - subTime.getTime()) / (1000 * 60) + " 分钟");
                        prescriptionStatus_dispensingPerson_textView.setText("   调剂人：" + dispensingMap.get("uname").toString());
                    } catch (ParseException e) {
                        new CoolToast(getBaseContext()).show("时间转换错误~!");
                    }

                } else if ("完成".equals(mainStatus)) {
                    if (Integer.parseInt(String.valueOf(prescriptionMap.get("imgCount"))) > 0) {
                        prescriptionStatus_image_button.setVisibility(View.VISIBLE);
                    }
                    Map dispensingMap = (Map) mainMap.get("dispensing");
                    Map tagMap = (Map) mainMap.get("tag");
                    Map inspectionMap = (Map) mainMap.get("inspection");
                    try {
                        Date subTime = dateFormat.parse(prescriptionMap.get("subTime").toString());
                        Date beginTime = dateFormat.parse(dispensingMap.get("beginTime").toString());
                        Date endTime = dateFormat.parse(dispensingMap.get("endTime").toString());
                        prescriptionStatus_status_textView.setText("调剂完成");
                        prescriptionStatus_waitTime_textView.setVisibility(View.VISIBLE);
                        prescriptionStatus_dispensingTime_textView.setVisibility(View.VISIBLE);
                        prescriptionStatus_dispensingPerson_textView.setVisibility(View.VISIBLE);
                        prescriptionStatus_did_textView.setVisibility(View.VISIBLE);
                        prescriptionStatus_did_textView.setText("  调剂 ID ：" + dispensingMap.get("id").toString());
                        prescriptionStatus_waitTime_textView.setText("候方用时：" + (beginTime.getTime() - subTime.getTime()) / (1000 * 60) + " 分钟");
                        prescriptionStatus_dispensingPerson_textView.setText("   调剂人：" + dispensingMap.get("uname").toString());
                        prescriptionStatus_dispensingTime_textView.setText("调剂用时：" + (endTime.getTime() - beginTime.getTime()) / (1000 * 60) + " 分钟");

                        if (tagMap != null) {
                            prescriptionStatus_tagCode_textView.setVisibility(View.VISIBLE);
                            prescriptionStatus_tagCode_textView.setText("绑定标签：" + tagMap.get("color").toString() + tagMap.get("code").toString().replace("M", ""));
                        }
                        if (inspectionMap != null) {
                            prescriptionStatus_insPerson_textView.setVisibility(View.VISIBLE);
                            prescriptionStatus_insTime_textView.setVisibility(View.VISIBLE);
                            prescriptionStatus_insPerson_textView.setText("  发药人：" + inspectionMap.get("uname"));
                            prescriptionStatus_insTime_textView.setText("发药时间：" + inspectionMap.get("insTime"));
                        }
                    } catch (ParseException e) {
                        new CoolToast(getBaseContext()).show("时间转换错误~!");
                    }

                }
            }
        }
        mainMap = (Map) statusMap.get("main");
        attachMap = (Map) statusMap.get("attach");
    }

    private void PrescriptionStausThread(int what) {
        final Message message = new Message();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case - 1:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                    case 0:
                        praseData();
//                        new CoolToast( getBaseContext () ).show ( ((Map)statusMap.get("main")).get("status").toString());
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            message.what = 0;
                            statusMap = new PrescriptionStatusUtil().getPrescriptionStatus(Integer.valueOf(pId));
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
}
