package net.andy.dispensing.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.TagDomain;
import net.andy.boiling.util.TagUtil;
import net.andy.com.Application;
import net.andy.dispensing.domain.*;
import net.andy.dispensing.util.*;
import net.andy.boiling.R;
import net.andy.boiling.domain.PrescriptionDomain;
import net.andy.boiling.ui.MipcaActivityCapture;
import net.andy.boiling.util.PrescriptionUtil;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.com.NFCActivity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Guang on 2016/2/18.
 */
public class DispensingUI extends NFCActivity {
    private PowerManager.WakeLock mWakeLock;
    public static final String ACTION_DISPENSING= "net.andy.com.MqttNotification";
    private  UpdateUIBroadcastReceiver broadcastReceiver;
    private final static int SCANNIN_GREQUEST_CODE = 1000;
    private final static int PAUSE_PRESITION_CODE = 2000;
    private final static int PAUSE_GETPRESITION_CODE = 3000;
    private LinearLayout dispensing_patientInfo_linearLayout;
    private LinearLayout dispensing_medicineInfo_linearLayout;
    private LinearLayout dispensing_readNextMedicine_linearLayout;
    private LinearLayout dispensing_banding_linearLayout;
    private LinearLayout dispensing_tagInfo_linearLayout;
    private LinearLayout dispensing_herspecAndTotal_linearLayout;
    private LinearLayout dispensing_otherButton_linearLayout;
    private LinearLayout dispensing_warning_linearLayout;
    private LinearLayout dispensing_adjust_linearLayout;
    private Button dispensing_adjust_button;
    private TextView dispensing_sum_textView;
    private TextView dispensing_category_textView;
    private TextView dispensing_patientNo_textView;
    private TextView dispensing_patientName_textView;
    private TextView dispensing_drugstoreName_textView;
    private TextView dispensing_doctorName_textView;
    private TextView dispensing_nowCount_textView;
    private TextView dispensing_presName_textView;
    private TextView dispensing_nextMedicine_textView;
    private TextView dispensing_way_textView;
    private TextView dispensing_special_textView;
    private TextView dispensing_herbSpec_textView;
    private TextView dispensing_total_textView;
    private TextView dispensing_preNumber_textView;
    private TextView dispensing_tagInfo_textView;
    private TextView dispensing_sysinfo_textView;
    private TextView dispensing_tagColor_textView;
    private TextView dispensing_timeText_textView;
    private TextView dispensing_noDispensing_textView;
    private TextView dispensing_alreadyDispensing_textView;
    private GridView dispensing_image_gridView;
    private LinearLayout dispensing_grid_linearLayout;
    //    private ImageView dispensing_category_imageView;
//    private ImageView dispensing_process_imageView;
//    private ImageView dispensing_classification_imageView;
//    private ImageView dispensing_xiyao_imageView;
//    private ImageView dispensing_heshi_imageView;
    private ImageView dispensing_stop_imageView;
    private ImageView dispensing_warning_imageView;
    private Button dispensing_readNextMedicine_button;
    private Button dispensing_applyMedicine_button;
    private Button dispensing_history_button;
    private Button dispensing_pause_button;
    private MyButtonListener myButtonListener;
    private PrescriptionDomain prescriptionDomain;
    private DispensingDomain dispensingDomain;
    private ReadyDomain readyDomain;
    private DispensingDetailDomain dispensingDetailDomain;
    private DispensingDetailDomain nowDis;
    private DispensingDetailDomain nextDis;
    private List<DispensingDetailDomain> dispensingDetailDomainList;
    private List<DispensingDetailDomain> historyDisDetailList;
    private DispensingDetailUtil dispensingDetailUtil = new DispensingDetailUtil();
    private DispensingUtil dispensingUtil = new DispensingUtil();
    private ReportUtil reportUtil=new ReportUtil();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ImageButton dispensing_show_button;
    private List<HashMap<String, Object>> historyData;
    private List listDis;
    private String barcode = "";
    private SimpleAdapter imageAdapter;
    private GridView dispensing_history_gridView;
    private GridAdapter gridAdapter;
    private Integer count;
    private long minInt;
    private String minString;
    private long socInt;
    private String socString;
    private String tagId;
    private Integer alreadyDisCount;
    private Integer restCount;
    private int disCount;
    private BigDecimal totalWeight;
    private int valCount=0;
    private boolean isEnd;
    private boolean isFinish;
    private boolean isRead;
    private boolean hasPre;
    private boolean isHer;
    private boolean hasHistory;
    private boolean isShow;
    private boolean hasDownTimer;
    private boolean hasValidateion;
    private boolean isGetPresTime=true;
    private long serverTime;
    private boolean hasReady;
    private List<Map> sumList=new ArrayList();
    private int clickCount=0;
    private TagDomain tagDomain;
    private DispensingValidationDomain dispensingValidationDomain=new DispensingValidationDomain();
    private String settingHerbspec = new AppOption().getOption(AppOption.APP_OPTION_HERSPEC);
    private DecimalFormat df1 = new DecimalFormat("#.##");
    private List<HashMap<String, Object>> imageData = new ArrayList<HashMap<String, Object>>();
    private CountDownTimer getPresTime = new CountDownTimer(5 * 1000, 200) {
        @Override
        public void onTick(long l) {
            isGetPresTime=false;
        }

        @Override
        public void onFinish() {
            isGetPresTime=true;
        }
    };
    private CountDownTimer cDTimer = new CountDownTimer(Integer.parseInt(new AppOption().getOption(AppOption.APP_OPTION_WAITTIME)) * 1000, 1000) {
        @Override
        public void onTick(long l) {
//            dispensing_medicineInfo_linearLayout.setClickable(false);
            dispensing_stop_imageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinish() {
            dispensing_medicineInfo_linearLayout.setClickable(true);
            dispensing_stop_imageView.setVisibility(View.INVISIBLE);
        }
    };
    private CountDownTimer showButtonTimer = new CountDownTimer(5 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            dispensing_banding_linearLayout.setVisibility(View.GONE);
            dispensing_tagInfo_linearLayout.setVisibility(View.VISIBLE);
            dispensing_readNextMedicine_button.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinish() {
            dispensing_tagInfo_linearLayout.setVisibility(View.GONE);
            setStatus(false);
        }
    };
    private CountDownTimer downTimer = new CountDownTimer(10000 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            socInt++;
            if (socInt < 10) {
                socString = "0" + socInt;
            } else if (socInt >= 10) {
                minInt = socInt / 60;
                if (socInt % 60 < 10) {
                    socString = "0" + socInt % 60;
                } else {
                    socString = "" + socInt % 60;
                }
            }
            if (minInt == 0) {
                minString = "00";
            } else if (minInt < 10) {
                minString = "0" + minInt;
            } else {
                minString = "" + minInt;
            }
            dispensing_timeText_textView.setText(minString + ":" + socString);
//            dispensing_medicineInfo_linearLayout.setClickable(false);
        }

        @Override
        public void onFinish() {
            dispensing_medicineInfo_linearLayout.setClickable(true);
        }
    };
//    public boolean run;
    private final Handler handler = new Handler();
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
//            if (run) {
//                handler.postDelayed(this, 30000);
//               herbalUtil(12);
//            } else {
//                handler.removeCallbacks(this);
//            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispensing);
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DISPENSING);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
        //屏幕常亮
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        myButtonListener = new MyButtonListener();
        dispensing_patientInfo_linearLayout = (LinearLayout) findViewById(R.id.dispensing_patientInfo_linearLayout);
        dispensing_medicineInfo_linearLayout = (LinearLayout) findViewById(R.id.dispensing_medicineInfo_linearLayout);
        dispensing_banding_linearLayout = (LinearLayout) findViewById(R.id.dispensing_banding_linearLayout);
        dispensing_readNextMedicine_linearLayout = (LinearLayout) findViewById(R.id.dispensing_readNextMedicine_linearLayout);
        dispensing_tagInfo_linearLayout = (LinearLayout) findViewById(R.id.dispensing_tagInfo_linearLayout);
        dispensing_herspecAndTotal_linearLayout = (LinearLayout) findViewById(R.id.dispensing_herspecAndTotal_linearLayout);
        dispensing_warning_linearLayout = (LinearLayout) findViewById(R.id.dispensing_warning_linearLayout);
        dispensing_adjust_linearLayout= (LinearLayout) findViewById(R.id.dispensing_adjust_linearLayout);
        dispensing_adjust_button = (Button) findViewById(R.id.dispensing_adjust_button);
        dispensing_applyMedicine_button = (Button) findViewById(R.id.dispensing_applyMedicine_button);
        dispensing_category_textView = (TextView) findViewById(R.id.dispensing_category_textView);
        dispensing_patientNo_textView = (TextView) findViewById(R.id.dispensing_patientNo_textView);
        dispensing_patientName_textView = (TextView) findViewById(R.id.dispensing_patientName_textView);
        dispensing_drugstoreName_textView = (TextView) findViewById(R.id.dispensing_drugstoreName_textView);
        dispensing_doctorName_textView = (TextView) findViewById(R.id.dispensing_doctorName_textView);
        dispensing_nowCount_textView = (TextView) findViewById(R.id.dispensing_nowCount_textView);
        dispensing_presName_textView = (TextView) findViewById(R.id.dispensing_presName_textView);
        dispensing_nextMedicine_textView = (TextView) findViewById(R.id.dispensing_nextMedicine_textView);
        dispensing_preNumber_textView = (TextView) findViewById(R.id.dispensing_preNumber_textView);
        dispensing_way_textView = (TextView) findViewById(R.id.dispensing_way_textView);
        dispensing_special_textView = (TextView) findViewById(R.id.dispensing_special_textView);
        dispensing_image_gridView = (GridView) findViewById(R.id.dispensing_image_gridView);
        dispensing_herbSpec_textView = (TextView) findViewById(R.id.dispensing_herbSpec_textView);
        dispensing_total_textView = (TextView) findViewById(R.id.dispensing_total_textView);
        dispensing_timeText_textView = (TextView) findViewById(R.id.dispensing_timeText_textView);
        dispensing_tagInfo_textView = (TextView) findViewById(R.id.dispensing_tagInfo_textView);
        dispensing_sysinfo_textView = (TextView) findViewById(R.id.dispensing_sysinfo_textView);
        dispensing_alreadyDispensing_textView= (TextView) findViewById(R.id.dispensing_alreadyDispensing_textView);
        dispensing_noDispensing_textView= (TextView) findViewById(R.id.dispensing_noDispensing_textView);
        dispensing_sum_textView= (TextView) findViewById(R.id.dispensing_sum_textView);
        dispensing_grid_linearLayout = (LinearLayout) findViewById(R.id.dispensing_grid_linearLayout);
        dispensing_otherButton_linearLayout = (LinearLayout) findViewById(R.id.dispensing_otherButton_linearLayout);
        dispensing_show_button = (ImageButton) findViewById(R.id.dispensing_show_button);
        dispensing_history_gridView = (GridView) findViewById(R.id.dispensing_history_gridView);
//        dispensing_category_imageView = (ImageView) findViewById(R.id.dispensing_category_imageView);
//        dispensing_process_imageView = (ImageView) findViewById(R.id.dispensing_process_imageView);
//        dispensing_classification_imageView = (ImageView) findViewById(R.id.dispensing_classification_imageView);
//        dispensing_xiyao_imageView = (ImageView) findViewById(R.id.dispensing_xiyao_imageView);
//        dispensing_heshi_imageView = (ImageView) findViewById(R.id.dispensing_heshi_imageView);
        dispensing_stop_imageView = (ImageView) findViewById(R.id.dispensing_stop_imageView);
        dispensing_warning_imageView = (ImageView) findViewById(R.id.dispensing_warning_imageView);
        dispensing_readNextMedicine_button = (Button) findViewById(R.id.dispensing_readNextMedicine_button);
        dispensing_history_button = (Button) findViewById(R.id.dispensing_history_button);
        dispensing_pause_button = (Button) findViewById(R.id.dispensing_pause_button);
        dispensing_medicineInfo_linearLayout.setOnClickListener(myButtonListener);
        dispensing_patientInfo_linearLayout.setOnClickListener(myButtonListener);
        dispensing_tagInfo_linearLayout.setOnClickListener(myButtonListener);
        dispensing_adjust_button.setOnClickListener(myButtonListener);
        dispensing_readNextMedicine_button.setOnClickListener(myButtonListener);
        dispensing_history_button.setOnClickListener(myButtonListener);
        dispensing_applyMedicine_button.setOnClickListener(myButtonListener);
        dispensing_pause_button.setOnClickListener(myButtonListener);
        dispensing_show_button.setOnClickListener(myButtonListener);
        prescriptionDomain = new PrescriptionDomain();
        readyDomain = new ReadyDomain();
        dispensingDomain = new DispensingDomain();
        dispensingDetailDomain = new DispensingDetailDomain();
        tagDomain = new TagDomain();
        imageAdapter = new SimpleAdapter(this,
                imageData, R.layout.dis_imageitem, new String[]{"imageItem"}, new int[]{R.id.dispensing_all_imageView});
        dispensing_image_gridView.setAdapter(imageAdapter);
        historyDisDetailList = new ArrayList<DispensingDetailDomain>();
        historyData = new ArrayList<HashMap<String, Object>>();
        gridAdapter = new GridAdapter(this);
        dispensing_history_gridView.setAdapter(gridAdapter);
        hasHistory=false;
        herbalUtil(12);
        reset();
        getReadyPre();
//        setImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
//        run = true;
        handler.removeCallbacks(task);
        handler.postDelayed(task, 10);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销广播
        unregisterReceiver(broadcastReceiver);
//        run = false;
    }
    @Override
    public void onPause() {
        super.onPause();
        mWakeLock.release();
//        run = false;
    }

    @Override
    protected void onStart() {
//        Intent intent = new Intent(DispensingUI.this, FloatViewService.class);
//        //启动FloatViewService
//        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
//        run=true;
        handler.postDelayed(task, 10);
    }

    private void setImage() {
        dispensing_way_textView.setVisibility(View.VISIBLE);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = new HashMap<String, Object>();
        if ("住院".equals(prescriptionDomain.getCategory())) {
            map.put("imageItem", R.drawable.zhuyuan);
        } else if ("门诊".equals(prescriptionDomain.getCategory())) {
            map.put("imageItem", R.drawable.menzhen);
        }
        imageData.add(map);
        map = new HashMap<String, Object>();
        switch (prescriptionDomain.getClassification()) {
            case "饮片":
                map.put("imageItem", R.drawable.yinpian);
                imageData.add(map);
                break;
            case "膏方":
                map.put("imageItem", R.drawable.gaofang);
                imageData.add(map);
                break;
            case "免煎":
                map.put("imageItem", R.drawable.mianjian);
                imageData.add(map);
                break;
        }
        map = new HashMap<String, Object>();
        if ("自煎".equals(prescriptionDomain.getProcess())) {
            map.put("imageItem", R.drawable.zijian);
            imageData.add(map);
        } else if ("代煎".equals(prescriptionDomain.getProcess())) {
            map.put("imageItem", R.drawable.daijian);
            imageData.add(map);
        }
        map = new HashMap<String, Object>();
        if (prescriptionDomain.getValuablesCnt() > 0) {
            map.put("imageItem", R.drawable.xiyao);
            imageData.add(map);
        }
        imageAdapter.notifyDataSetChanged();
    }
    private void showValidationImage(){
        if(dispensingValidationDomain!=null) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("imageItem", R.drawable.fuhe);
            hasValidateion=true;
            imageData.add(map);
            imageAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 无预调剂处方时
     */

    private void setValue() {
        nowDis = dispensingDetailDomainList.get(disCount);
        if (!"".equals(nowDis.getSpecial())) {
            dispensing_special_textView.setVisibility(View.VISIBLE);
            dispensing_special_textView.setText((++valCount)+""+nowDis.getSpecial());
            dispensing_presName_textView.setTextColor(Color.RED);
        } else {
            dispensing_presName_textView.setTextColor(Color.BLACK);
            dispensing_special_textView.setVisibility(View.INVISIBLE);
        }
        if ("饮片".equals(prescriptionDomain.getClassification()) || "膏方".equals(prescriptionDomain.getClassification())) {
            dispensing_nowCount_textView.setText("第" + (disCount + 1) + "味  共" + dispensingDetailDomainList.size() + "味  计" + df1.format(Arith.mul(totalWeight , prescriptionDomain.getPresNumber())) + nowDis.getHerbUnit());
            dispensing_presName_textView.setText(nowDis.getHerbName() + " " + Arith.mul( nowDis.getQuantity(), prescriptionDomain.getPresNumber()) + nowDis.getHerbUnit());
            dispensing_total_textView.setText(nowDis.getQuantity() + nowDis.getHerbUnit());
        } else if ("免煎".equals(prescriptionDomain.getClassification())) {
            dispensing_nowCount_textView.setText("第" + (disCount + 1) + "味  共" + dispensingDetailDomainList.size() + "味  计" + df1.format(totalWeight) + nowDis.getHerbUnit());
            dispensing_presName_textView.setText(nowDis.getHerbName() + " " + nowDis.getQuantity() + nowDis.getHerbUnit());
            dispensing_total_textView.setText(Arith.mul(nowDis.getQuantity(), prescriptionDomain.getPresNumber()) + nowDis.getHerbUnit());
        }

        if (!Boolean.parseBoolean(settingHerbspec)) {
            // 显示规格
            dispensing_herspecAndTotal_linearLayout.setVisibility(View.GONE);
        }
        dispensing_herbSpec_textView.setText(nowDis.getHerbSpec());
        dispensing_nextMedicine_textView.setVisibility(View.VISIBLE);
        if (disCount == (dispensingDetailDomainList.size() - 1)) {
            disCount = 0;
            isEnd = true;
            dispensing_nextMedicine_textView.setTextColor(Color.RED);
            dispensing_nextMedicine_textView.setText("恭喜你，调剂完毕了！");
        } else {
            nextDis = dispensingDetailDomainList.get(++disCount);
            dispensing_nextMedicine_textView.setTextColor(Color.BLACK);
            dispensing_nextMedicine_textView.setText("下一味: " + nextDis.getHerbName());
        }
        //显示警告符号
        if ("true".equals(nowDis.getWarning())) {
            dispensing_warning_imageView.setVisibility(View.VISIBLE);
        } else {
            dispensing_warning_imageView.setVisibility(View.INVISIBLE);
        }
        //添加到调剂历史队列
        historyDisDetailList.add(nowDis);
        historyPre();
    }

    /**
     * 有预调剂处方
     */
    private void setReadyValue() {
        dispensing_nowCount_textView.setText("第 1 味  共" + dispensingDetailDomainList.size() + "味");
        dispensing_presName_textView.setText(readyDomain.getTitle());
        dispensing_herspecAndTotal_linearLayout.setVisibility(View.GONE);
        dispensing_nextMedicine_textView.setVisibility(View.GONE);
    }

    /**
     * 刷新待调处方数
     *
     */
    private void getReadyPre(){
//        clickCount++;
//        if(clickCount%3==0){
//            herbalUtil(12);
//        }
    }
    class MyButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dispensing_medicineInfo_linearLayout:
                    getReadyPre();
                    dispensing_medicineInfo_linearLayout.setClickable(false);
                    Log.e("dis tagId", dispensingDomain.getTagId());
                    if (hasReady) {
                        herbalUtil(11);
                    } else {
                        if (!isEnd) {
                            //提交调剂处方状态
                            herbalUtil(2);
                        } else {
//                        if("".equals(dispensingDomain.getTagId())) {
                            herbalUtil(3);

//                        }else{
//                            herbalUtil(10);
//                            isEnd = false;
//                            isFinish = false;
//                        }
                        }
                    }
                    break;
                case R.id.dispensing_readNextMedicine_button:
                    isFinish = false;
                    //正常流程
                    herbalUtil(0);
                    //开启读条码功能
//                    Intent intent = new Intent();
//                    intent.setClass(DispensingUI.this, MipcaActivityCapture.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                    break;
                case R.id.dispensing_tagInfo_linearLayout:
                    if(isGetPresTime) {
                        getPresTime.start();
                        isFinish = false;
                        //正常流程
                        herbalUtil(0);
                    }else{
                        new CoolToast(getBaseContext()).show("点击无效，请不要频繁点击！");
                    }
                    //开启读条码功能
//                    Intent i = new Intent();
//                    i.setClass(DispensingUI.this, MipcaActivityCapture.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivityForResult(i, SCANNIN_GREQUEST_CODE);
//                            .setVisibility(View.VISIBLE);
                    break;
                case R.id.dispensing_applyMedicine_button:
                    if (hasPre) {
                        //上药
                        Intent reIntent = new Intent();
                        reIntent.setClass(DispensingUI.this, AckReplenishUI.class);
                        reIntent.putExtra("dis", nowDis);
                        startActivity(reIntent);
//                        herbalUtil(7);
                    } else {
                        Intent reIntent = new Intent();
                        reIntent.setClass(DispensingUI.this, AckReplenishUI.class);
                        startActivity(reIntent);
                    }
                    break;
                case R.id.dispensing_patientInfo_linearLayout:
                    if (hasHistory) {
                        Intent in = new Intent(DispensingUI.this, PatientInfoUI.class);
                        in.putExtra("pre", prescriptionDomain);
                        startActivity(in);
                    } else {
                        new CoolToast(getBaseContext()).show("请先获取处方");
                    }
                    break;
                case R.id.dispensing_history_button:
                    if (hasHistory) {
                        Intent in = new Intent(DispensingUI.this, DisHistoryUI.class);
                        in.putExtra("dises", (Serializable) historyDisDetailList);
                        in.putExtra("pre", (Serializable)prescriptionDomain);
                        startActivity(in);
                    } else {
                        new CoolToast(getBaseContext()).show("请先获取处方");
                    }
                    break;
                case R.id.dispensing_show_button:
                    if (isShow) {
                        isShow = false;
                        dispensing_otherButton_linearLayout.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dispensing_show_button.getLayoutParams();
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        dispensing_show_button.setLayoutParams(layoutParams);
                    } else {
                        isShow = true;
                        dispensing_otherButton_linearLayout.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dispensing_show_button.getLayoutParams();
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
                        dispensing_show_button.setLayoutParams(layoutParams);
                    }
                    break;
                /**
                 * 暂停调剂
                 */
                case R.id.dispensing_pause_button:
                    if (isHer) {
                        Intent im = new Intent();
                        im.setClass(DispensingUI.this, PausePresUI.class);
                        im.putExtra("sign", "set");
                        im.putExtra("disId", nowDis.getDisId());
                        startActivityForResult(im, PAUSE_PRESITION_CODE);
                    } else {
                        Intent im = new Intent();
                        im.putExtra("sign", "get");
                        im.setClass(DispensingUI.this, PausePresUI.class);
                        startActivityForResult(im, PAUSE_GETPRESITION_CODE);
                    }
                    break;
                /*按味调剂*/
                case R.id.dispensing_adjust_button:
                    Log.e("按味调剂按钮","点击");
                    dispensing_adjust_linearLayout.setVisibility(View.GONE);
                    dispensing_warning_linearLayout.setVisibility(View.VISIBLE);
                    hasReady=false;
                    setValue();
                    break;
            }
        }
    }

    //忽略已经调剂过的药
    private void setNowAdjust() {
        int now = 0;
        for (; now < dispensingDetailDomainList.size(); now++
                ) {
            System.out.println("setNowAdjust:" +dispensingDetailDomainList.get(now).getStatus());
            if ("未调剂".equals(dispensingDetailDomainList.get(now).getStatus())) {
                disCount = now;
                break;
            }
            System.out.println("setNowAdjust:" + now);
            nowDis = dispensingDetailDomainList.get(now);
            nowDis.setStatus("完成");
            historyDisDetailList.add(nowDis);
        }
        Log.e("Now:",now+"");
        System.out.println("<>" + historyDisDetailList.size());
        if (now == dispensingDetailDomainList.size()) {
//            dispensing_medicineInfo_linearLayout.setVisibility(View.GONE);
//            dispensing_banding_linearLayout.setVisibility(View.VISIBLE);
            new CoolToast(getBaseContext()).show("已完成处方调剂,请绑定标签！");
            herbalUtil(3);
            isFinish = true;
        }
        System.out.println((dispensingDetailDomainList.size() - 1) + "setNowAdjust:" + now);
        historyPre();
    }

    /**
     * 设置按钮显示隐藏
     **/
    private void setStatus(boolean b) {
        if (b) {
//            dispensing_readNextMedicine_linearLayout.animate()
//                    .alpha(0f)
//                    .setDuration(2000)
//                    .setListener(null);
            dispensing_readNextMedicine_linearLayout.setVisibility(View.GONE);
            dispensing_medicineInfo_linearLayout.setVisibility(View.VISIBLE);
//            dispensing_medicineInfo_linearLayout.setAlpha(0f);
//            dispensing_medicineInfo_linearLayout.animate()
//                    .alpha(1f)
//                    .setDuration(2000)
//                    .setListener(null);
        } else {
//            dispensing_medicineInfo_linearLayout.animate()
//                    .alpha(0f)
//                    .setDuration(1000)
//                    .setListener(null);
            dispensing_medicineInfo_linearLayout.setVisibility(View.GONE);
            dispensing_readNextMedicine_linearLayout.setVisibility(View.VISIBLE);
//            dispensing_readNextMedicine_linearLayout.setAlpha(0f);
//            dispensing_readNextMedicine_linearLayout.animate()
//                    .alpha(1f)
//                    .setDuration(1000)
//                    .setListener(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    barcode = bundle.getString("result");
                    Log.e("条码", barcode);
                    herbalUtil(5);
//                    new CoolToast(getBaseContext()).show(barcode);
                }
                break;
            case PAUSE_PRESITION_CODE:
                if (resultCode == RESULT_OK) {
                    reset();
                    setStatus(false);
                    dispensing_banding_linearLayout.setVisibility(View.GONE);
                    historyData.clear();
                    gridAdapter.notifyDataSetChanged();
                    socInt = 0;
                    downTimer.cancel();
                    isHer = false;
                    isEnd = false;
                    isFinish = false;
                }
                break;
            case PAUSE_GETPRESITION_CODE:
                if (resultCode == RESULT_OK) {
                    tagId = data.getStringExtra("tagId");
                    Log.e("tagId", tagId);
                    herbalUtil(9);
                    break;
                }
        }
    }

    /**
     * 将十六进制 颜色代码 转换为 int
     *
     * @return
     */
    public static int HextoColor(String color) {

        String reg = "#[a-f0-9A-F]{8}";
        if (!Pattern.matches(reg, color)) {
            color = "#00ffffff";
        }
        return Color.parseColor(color);
    }

private  void setGlobalView(){
    hasPre = true;
    isHer = true;
    hasHistory=true;
    historyDisDetailList.clear();
    setStatus(true);
    setView();
    dispensing_sysinfo_textView.setVisibility(View.GONE);
    dispensing_grid_linearLayout.setVisibility(View.VISIBLE);
    parseObject(listDis);
    setNowAdjust();
    long temp = serverTime - dispensingDomain.getBeginTime().getTime();
    socInt = temp / 1000;
    Log.e("socInt", serverTime + ""+dispensingDomain.getBeginTime().getTime());
        cDTimer.start();
    if(!hasDownTimer) {
        downTimer.start();
        hasDownTimer=true;
    }
}
    /**
     * 获取待调剂处方
     **/
    private void herbalUtil(int what) {
        /**
         * 子线程消息处理
         */
        final Message message = new Message();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        isRead=false;
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                    case 0:
                        cDTimer.start();
                        reset();
                        setGlobalView();
                        dispensing_adjust_linearLayout.setVisibility(View.GONE);
                        dispensing_warning_linearLayout.setVisibility(View.VISIBLE);
                        if (!isFinish) {
                            setValue();
                        }
                        herbalUtil(1);
                        break;
                    case 1:
                        cDTimer.start();
                        break;
                    case 2:
                        cDTimer.start();
                        dispensing_adjust_linearLayout.setVisibility(View.GONE);
                        dispensing_warning_linearLayout.setVisibility(View.VISIBLE);
                        setValue();
                        if(!hasValidateion) {
                            herbalUtil(14);
                        }
                        break;
                    case 3:
                        cDTimer.start();
                        String sum="";
                        for(int i=0;i<sumList.size();i++) {
                            if(i>0){
                                sum =sum +"\n"+sumList.get(i).get("sum")+sumList.get(i).get("herbUnit");
                            }else {
                                sum = sum + sumList.get(i).get("sum") + sumList.get(i).get("herbUnit");
                            }
                        }
                        dispensing_sum_textView.setText(sum);
                        dispensing_medicineInfo_linearLayout.setVisibility(View.GONE);
                        dispensing_banding_linearLayout.setVisibility(View.VISIBLE);
                        isEnd = false;
                        isFinish = true;
                        break;
                    case 4:
                        hasPre = false;
                        isHer = false;
                        isRead=false;
                        isFinish = false;
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        dispensing_tagInfo_textView.setText(tagDomain.getCode().replace("M",""));
                        dispensing_tagInfo_textView.setBackgroundColor(HextoColor(tagDomain.getColorValue()));
                    //自动隐藏标签绑定信息
//                        showButtonTimer.start();
                        dispensing_banding_linearLayout.setVisibility(View.GONE);
                        dispensing_tagInfo_linearLayout.setVisibility(View.VISIBLE);
                        downTimer.cancel();
                        downTimer.onFinish();
                        socInt = 0;
                        minInt = 0;
                        reset();
                        break;
                    case 7:
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        break;
                    case 8:
                        hasPre = false;
                        isHer = false;
                        new CoolToast(getBaseContext()).show((String) msg.obj);
                        dispensing_medicineInfo_linearLayout.setVisibility(View.GONE);
                        dispensing_readNextMedicine_linearLayout.setVisibility(View.VISIBLE);
                        dispensing_readNextMedicine_button.setVisibility(View.VISIBLE);
                        dispensing_banding_linearLayout.setVisibility(View.GONE);
                        historyData.clear();
                        gridAdapter.notifyDataSetChanged();
                        setStatus(false);
                        downTimer.cancel();
                        downTimer.onFinish();
                        socInt = 0;
                        minInt = 0;
                        reset();
                        break;
                    case 10:
                        hasPre = false;
                        isHer = false;
                        new CoolToast(getBaseContext()).show("已绑定标签");
                        dispensing_medicineInfo_linearLayout.setVisibility(View.GONE);
                        dispensing_tagInfo_textView.setText(tagDomain.getCode().replace("M",""));
                        dispensing_tagInfo_textView.setBackgroundColor(HextoColor(tagDomain.getColorValue()));
                        //自动隐藏标签绑定信息
//                        showButtonTimer.start();
                        dispensing_banding_linearLayout.setVisibility(View.GONE);
                        dispensing_tagInfo_linearLayout.setVisibility(View.VISIBLE);
                        downTimer.cancel();
                        downTimer.onFinish();
                        socInt = 0;
                        minInt = 0;
                        reset();
                        break;
                    case 11:
                        setGlobalView();
                        dispensing_adjust_linearLayout.setVisibility(View.VISIBLE);
                        dispensing_warning_linearLayout.setVisibility(View.GONE);
                        setReadyValue();
                        break;
                    case 12:
                        dispensing_noDispensing_textView.setText("待调："+restCount);
                        dispensing_alreadyDispensing_textView.setText("完成："+alreadyDisCount);
                        break;
                    case 13:
                        dispensing_alreadyDispensing_textView.setText("完成："+alreadyDisCount);
                        break;
                    case 14:
                        showValidationImage();
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    switch (what) {
                        //获取处方和待调剂处方
                        case 0:
                            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                            if (wifiManager == null) {
                                message.what = -1;
                                message.obj = "请先打开无线网络";
                                handler.sendMessage(message);
                            }
                            String deviceid = wifiManager.getConnectionInfo().getMacAddress();
                            Map alldis = new PrescriptionUtil().insertDispensingByDevice(deviceid, new AppOption().getOption(AppOption.APP_OPTION_USER));
                            serverTime = Long.parseLong(new ServerUtil().getServerTime());
                            if (alldis == null) {
                                message.what = -1;
                                message.obj = "暂无处方可调剂，请稍后再试！";
                                handler.sendMessage(message);
                            } else {
                                hasDownTimer=false;
                                prescriptionDomain = JSON.parseObject(alldis.get("prescription").toString(), PrescriptionDomain.class);
                                dispensingDomain = JSON.parseObject(alldis.get("dispensing").toString(), DispensingDomain.class);
                                listDis= JSON.parseObject(alldis.get("dispensingDetail").toString(), List.class);
                                if (prescriptionDomain.getReady() != 0) {
                                    Log.e("预调剂", String.valueOf(prescriptionDomain.getReady()));
                                    readyDomain = JSON.parseObject(alldis.get("ready").toString(), ReadyDomain.class);
                                    hasReady = true;
                                    message.what = 11;
                                    message.obj = "";
                                    handler.sendMessage(message);
                                    break;
                                }
                                message.what = 0;
                                message.obj = listDis;
                                handler.sendMessage(message);
                            }
                            break;
                        //提交第一味药开始调剂时间
                        case 1:
                            Log.e("1",nowDis.getHerbName());
                            dispensingDetailUtil.start(String.valueOf(nowDis.getId()));
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        //更新调剂药的状态
                        case 2:
                            Log.e("22",nowDis.getHerbName());
                            dispensingDetailUtil.finish(String.valueOf(nowDis.getId()));
                            Log.e("2",nowDis.getHerbName());
                            dispensingDetailUtil.start(String.valueOf(nextDis.getId()));

                            message.what = 2;
                            handler.sendMessage(message);
                            break;
                        //提交最后一味结束
                        case 3:
                            Log.e("3",nowDis.getHerbName());
                            dispensingDetailUtil.finish(String.valueOf(nowDis.getId()));
                            sumList =dispensingDetailUtil.getQuantityForUnit(dispensingDomain.getId());
                            message.what = 3;
                            handler.sendMessage(message);
                            break;
                        //绑定标签，本处方调剂结束
                        case 4:
                            tagDomain = new TagUtil().getTagByTagId(tagId);
                            message.obj = dispensingUtil.updateToFinish(String.valueOf(dispensingDomain.getId()), tagId);
                            message.what = 4;
                            handler.sendMessage(message);
                            break;
                        case 5:
                            Map all = new PrescriptionUtil().insertDispensingByDevice(new AppOption().getOption(AppOption.APP_DEVICE_ID), new AppOption().getOption(AppOption.APP_OPTION_USER), barcode);
                            serverTime = Long.parseLong(new ServerUtil().getServerTime());
                            if (all == null) {
                                message.what = -1;
                                message.obj = "暂无处方可用，请稍后再试！";
                                handler.sendMessage(message);
                            } else {
                                hasDownTimer=false;
                                prescriptionDomain = JSON.parseObject(all.get("prescription").toString(), PrescriptionDomain.class);
                                dispensingDomain = JSON.parseObject(all.get("dispensing").toString(), DispensingDomain.class);
                                listDis= JSON.parseObject(all.get("dispensingDetail").toString(), List.class);
                                if (prescriptionDomain.getReady() != 0) {
                                    readyDomain = JSON.parseObject(all.get("ready").toString(), ReadyDomain.class);
                                    hasReady = true;
                                    message.what = 11;
                                    message.obj = "";
                                    handler.sendMessage(message);
                                    break;
                                }
                                message.what = 0;
                                message.obj = listDis;
                                handler.sendMessage(message);
                            }
                            break;
//                        //提交最后一味结束
//                        case 6:
//                            dispensingDetailUtil.finish(String.valueOf(nowDis.getId()));
//                            message.what = 1;
//                            handler.sendMessage(message);
//                            break;
                        //提醒上药
                        case 7:
                            StationDomain stationDomain = new StationUtil().getStationByDevice();
                            message.obj = new ReplenishUtil().request(stationDomain.getId(), nowDis.getHerbId());
                            message.what = 7;
                            handler.sendMessage(message);
                            break;
                        /**
                         * 暂停调剂
                         */
                        case 8:
                            message.obj = new PrescriptionUtil().pause(nowDis.getDisId(), tagId);
                            message.what = 8;
                            handler.sendMessage(message);
                            break;
                        /**
                         * 获取暂停处方
                         */
                        case 9:
                            Map map = new PrescriptionUtil().cancelPauseByTagId(tagId);
                            serverTime = Long.parseLong(new ServerUtil().getServerTime());
                            if (map == null) {
                                message.what = -1;
                                message.obj = "无匹配处方！";
                                handler.sendMessage(message);
                            } else {
                                prescriptionDomain = JSON.parseObject(map.get("prescription").toString(), PrescriptionDomain.class);
                                dispensingDomain = JSON.parseObject(map.get("dispensing").toString(), DispensingDomain.class);
                                listDis = JSON.parseObject(map.get("dispensingDetail").toString(), List.class);
                                message.what = 0;
                                message.obj = listDis;
                                handler.sendMessage(message);
                            }
                            break;
                        case 10:
                            tagDomain = new TagUtil().getTagByTagId(dispensingDomain.getTagId());
                            message.obj = dispensingUtil.updateToFinish(String.valueOf(dispensingDomain.getId()), dispensingDomain.getTagId());
                            message.what = 10;
                            handler.sendMessage(message);
                            break;
                        case 11:
                            dispensingDetailUtil.updateReady(dispensingDomain.getId(), readyDomain.getId());
                            listDis = dispensingDetailUtil.getDispensingDetailByDisId(dispensingDomain.getId());
                            message.obj = "";
                            message.what = 0;
                            handler.sendMessage(message);
                            break;
                        case 12:
                            new OnlineUtil().online(Application.getUsers().getId(),Application.getRulesDomain().getId());
                            StationDomain tempSd=new StationUtil().getStationByDevice();
                            restCount=reportUtil.getNoDispensingByRule(tempSd.getRulesId());
                            alreadyDisCount=reportUtil.getDispensingByUser(String.valueOf(Application.getUsers().getId()));
                            message.obj = "";
                            message.what = 12;
                            handler.sendMessage(message);
                            break;
                        case 13:
                            alreadyDisCount=reportUtil.getDispensingByUser(String.valueOf(Application.getUsers().getId()));
                            message.obj = "";
                            message.what = 13;
                            handler.sendMessage(message);
                            break;
                        case 14:
                            dispensingValidationDomain=new ValidationUtil().getValidationByDis(String.valueOf(nowDis.getDisId()));
                            message.what = 14;
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

    private void reset() {
        herbalUtil(13);
        dispensing_grid_linearLayout.setVisibility(View.GONE);
        imageData.clear();
        imageAdapter.notifyDataSetChanged();
//        dispensing_patientName_textView.setText("");
//        dispensing_patientNo_textView.setText("");
//        dispensing_drugstoreName_textView.setText("");
//        dispensing_doctorName_textView.setText("");
        dispensing_way_textView.setVisibility(View.GONE);
        dispensing_sysinfo_textView.setVisibility(View.VISIBLE);
        hasPre = false;
        hasReady = false;
        totalWeight = new BigDecimal(0);
        valCount=0;
        hasValidateion=false;
    }

    private void setView() {
        dispensing_category_textView.setText(prescriptionDomain.getCategory() + "号：");
        dispensing_patientNo_textView.setText(prescriptionDomain.getPatientNo());
        dispensing_patientName_textView.setText(prescriptionDomain.getPatientName());
        dispensing_drugstoreName_textView.setText(prescriptionDomain.getDeptName());
        dispensing_doctorName_textView.setText(prescriptionDomain.getDoctorName());
        dispensing_way_textView.setText(prescriptionDomain.getWay());
        dispensing_preNumber_textView.setText("" + prescriptionDomain.getPresNumber());
        dispensing_tagInfo_linearLayout.setVisibility(View.GONE);
        //图片显示控制
        setImage();
    }

    //ListMap 转为 List<Bean>
    private void parseObject(List listDis) {
        dispensingDetailDomainList = new ArrayList<DispensingDetailDomain>();
        if(listDis.size()!=0) {
            for (Object obj : listDis) {
                dispensingDetailDomain = new DispensingDetailDomain();
                dispensingDetailDomain.setId((Integer) ((Map) obj).get("id"));
                dispensingDetailDomain.setDisId((Integer) ((Map) obj).get("disId"));
                dispensingDetailDomain.setPlanId((String) ((Map) obj).get("planId"));
                dispensingDetailDomain.setHerbId((String) ((Map) obj).get("herbId"));
                dispensingDetailDomain.setHerbName((String) ((Map) obj).get("herbName"));
                dispensingDetailDomain.setHerbUnit((String) ((Map) obj).get("herbUnit"));
                dispensingDetailDomain.setQuantity(new BigDecimal(df1.format(((Map) obj).get("quantity"))));
                dispensingDetailDomain.setSpecial((String) ((Map) obj).get("special"));
                dispensingDetailDomain.setValuables((String) ((Map) obj).get("valuables"));
                dispensingDetailDomain.setUserId((Integer) ((Map) obj).get("userId"));
                dispensingDetailDomain.setDeptId((String) ((Map) obj).get("deptId"));
                dispensingDetailDomain.setHerbSpec((String) ((Map) obj).get("herbSpec"));
                dispensingDetailDomain.setWarning((String) ((Map) obj).get("warning"));
                if ((((Map) obj).get("beginTime") != null) && (((Map) obj).get("endTime") != null))
                    try {
                        dispensingDetailDomain.setBeginTime(sdf.parse((String) ((Map) obj).get("beginTime")));
                        dispensingDetailDomain.setEndTime(sdf.parse((String) ((Map) obj).get("endTime")));
                    } catch (ParseException e) {
//                e.printStackTrace();
                        new CoolToast(getBaseContext()).show((String) e.getMessage());
                    }
                dispensingDetailDomain.setStatus((String) ((Map) obj).get("status"));
                dispensingDetailDomain.setShelf((String) ((Map) obj).get("shelf"));
                totalWeight = Arith.add((BigDecimal) ((Map) obj).get("quantity"), totalWeight);
                dispensingDetailDomainList.add(dispensingDetailDomain);
            }
        }
//
//        Collections.sort(dCollections.sort(dispensingDetailDomainList, new ListShelfComparator());
//        for (DispensingDetailDomain dis : dispensingDetailDomainList
//                ) {
//            System.out.println(">>" + dis.getId() + " " + dis.getHerbName() + " " + dis.getShelf() + " " + dis.getSpecial());
//        }ispensingDetailDomainList, new ListSpecialComparator());
//        for (DispensingDetailDomain dis : dispensingDetailDomainList
//                ) {
//            System.out.println(">>" + dis.getId() + " " + dis.getHerbName() + " " + dis.getShelf()+" "+dis.getSpecial());
//        }
    }

    class ListShelfComparator implements Comparator<DispensingDetailDomain> {
        @Override
        public int compare(DispensingDetailDomain t1, DispensingDetailDomain t2) {
            String pre1 = t1.getShelf();
            String pre2 = t2.getShelf();
            //逆序排列
            if (!t1.getSpecial().isEmpty()) {
                return 2;
            }
            if (pre1.compareTo(pre2) > 0)
                return 1;
            else if (pre1.compareTo(pre2) == 0)
                return 0;
            else return -1;
        }
    }

    class ListSpecialComparator implements Comparator<DispensingDetailDomain> {
        @Override
        public int compare(DispensingDetailDomain t1, DispensingDetailDomain t2) {
            String special = t1.getSpecial();
            if ("".equals(special)) return -1;
            else return 1;
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        Log.e("Dispensing", isFinish + "");
        tagId = getNfc().readID(intent);
        if (isFinish) {
            if(!isRead) {
                isRead=true;
                herbalUtil(4);
            }
        } else {
            new CoolToast(getBaseContext()).show("请先完成处方调剂！");
        }
    }

    private void historyPre() {
        boolean isQ = true;
        historyData.clear();
        HashMap<String, Object> map;
        int count = historyDisDetailList.size();
        int m = 0;
        int x = 0;
        if (count % 3 == 0) {
            m = count / 3;
        } else {
            m = count / 3 + 1;
        }
        if ((count - 1) % 3 == 0) {
            x = m * 2 - 1;
        } else {
            x = m * 2;
        }
        int soc = count - 1 - m;
        int thi = count - 1 - x;
        System.out.println((count - 1) + ">0>#" + thi);
        for (int i = count - 1; i > soc; i--) {
            map = new HashMap<String, Object>();
            map.put("ItemNameView", historyDisDetailList.get(i).getHerbName());
            map.put("ItemNumView", (historyDisDetailList.get(i).getQuantity()) + dispensingDetailDomainList.get(i).getHerbUnit());
            map.put("ItemWaringView", (historyDisDetailList.get(i).getWarning()));
            map.put("ItemSpecialView", (historyDisDetailList.get(i).getSpecial()));
            historyData.add(map);
            gridAdapter.notifyDataSetChanged();
            if ((i - m) < 0) {
                continue;
            }
            if (thi == i - m) {
                continue;
            }
            map = new HashMap<String, Object>();
            map.put("ItemNameView", historyDisDetailList.get((i - m)).getHerbName());
            map.put("ItemNumView", (historyDisDetailList.get((i - m)).getQuantity()) + dispensingDetailDomainList.get((i - m)).getHerbUnit());
            map.put("ItemWaringView", (historyDisDetailList.get((i - m)).getWarning()));
            map.put("ItemSpecialView", (historyDisDetailList.get((i - m)).getSpecial()));
            historyData.add(map);
            gridAdapter.notifyDataSetChanged();
            if ((i - x) < 0) {
                continue;
            }
            map = new HashMap<String, Object>();
            map.put("ItemNameView", historyDisDetailList.get((i - x)).getHerbName());
            map.put("ItemNumView", (historyDisDetailList.get((i - x)).getQuantity()) + dispensingDetailDomainList.get((i - x)).getHerbUnit());
            map.put("ItemWaringView", (historyDisDetailList.get((i - x)).getWarning()));
            map.put("ItemSpecialView", (historyDisDetailList.get((i - x)).getSpecial()));

            historyData.add(map);
            gridAdapter.notifyDataSetChanged();
        }
    }
    private class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return historyData.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Dis dis;
            if (view == null) {
                view = inflater.inflate(R.layout.dis_historyitem, viewGroup, false);
                dis = new Dis();
                dis.historyItem_linearLayout= (LinearLayout) view.findViewById(R.id.historyItem_linearLayout);
                dis.historyItem_name_textView= (TextView) view.findViewById(R.id.historyItem_name_textView);
                dis.historyItem_waring_imageView = (ImageView) view.findViewById(R.id.historyItem_waring_imageView);
                dis.historyItem_num_textView = (TextView) view.findViewById(R.id.historyItem_num_textView);
                view.setTag(dis);
            } else {
                dis = (Dis) view.getTag();
            }
            Map map = (Map) historyData.get(i);
//            Log.e("map", map.toString());
            if("true".equals(map.get("ItemWaringView"))){
                dis.historyItem_waring_imageView.setVisibility(View.VISIBLE);
            }else{
                dis.historyItem_waring_imageView.setVisibility(View.INVISIBLE);
            }
            if(map.get("ItemSpecialView").toString().length()>0){
                dis.historyItem_linearLayout.setBackgroundColor(Color.parseColor("#D2691E"));
                dis.historyItem_name_textView.setTextColor(Color.WHITE);
                dis.historyItem_num_textView.setTextColor(Color.WHITE);
                dis.historyItem_name_textView .setText(""+map.get("ItemNameView")+map.get("ItemSpecialView"));
            }else {
                dis.historyItem_linearLayout.setBackgroundColor(Color.WHITE);
                dis.historyItem_name_textView.setTextColor(Color.BLACK);
                dis.historyItem_num_textView.setTextColor(Color.BLACK);
                dis.historyItem_name_textView.setText("" + map.get("ItemNameView"));
            }
            dis.historyItem_num_textView .setText(""+map.get("ItemNumView"));
            return view;
        }


        private class Dis {
            private LinearLayout historyItem_linearLayout;
            private ImageView historyItem_waring_imageView;
            private TextView historyItem_name_textView;
            private TextView historyItem_num_textView;
        }
    }
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String bindId=intent.getStringExtra("bindId");
            String message=intent.getStringExtra("message");
            if(Application.getRulesDomain().getId().equals(Integer.parseInt(bindId))){
                dispensing_noDispensing_textView.setText("待调："+message);
                Log.i("待调更新",message);
            }
        }
    }
}
