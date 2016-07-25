package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.R;
import net.andy.boiling.util.UserUtil;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.AssignUtil;
import net.andy.dispensing.util.DateTimePickDialogUtil;
import net.andy.dispensing.util.HerbalUtil;
import net.andy.dispensing.util.PersonalEffortUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 分配工作量
 */

public class AssignOtherWayUI extends Activity {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @ViewInject(R.id.assignotherway_gridView)
    private GridView assignotherway_gridView;
    @ViewInject(R.id.assignotherway_startTime_editText)
    private EditText assignotherway_startTime_editText;
    @ViewInject(R.id.assignotherway_endTime_editText)
    private EditText assignotherway_endTime_editText;
    @ViewInject(R.id.assignotherway_category_radioGroup)
    private RadioGroup assignotherway_category_radioGroup;
    @ViewInject(R.id.assignotherway_classification_radioGroup)
    private RadioGroup assignotherway_classification_radioGroup;
    @ViewInject(R.id.assignotherway_search_button)
    private Button assignotherway_search_button;
    @ViewInject(R.id.assignotherway_assign_button)
    private Button assignotherway_assign_button;
    @ViewInject(R.id.assignotherway_result_button)
    private Button assignotherway_result_button;
    @ViewInject(R.id.assignotherway_total_textView)
    private TextView assignotherway_total_textView;
    @ViewInject(R.id.assignotherway_assign_textView)
    private TextView assignotherway_assign_textView;
    private String assignCategory="门诊";
    private String assignClassification="免煎";
    private String nowCategory="";
    private String nowClassification="";
    private String beginDate;
    private String endDate;
    private AssignUtil assignUtil=new AssignUtil();
    private Integer total;
    private Integer count;
    private String assignJson;
    private List list=new ArrayList<Map>();
    private List<Line> listLine;
    private List<Assign> listAssgin;
    private GridAdapter gridAdapter;
    private boolean firstIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignotherway);
        x.view().inject(this);
        assignotherway_startTime_editText.setText(dateFormat.format(HerbalUtil.getTime("Month")));
        assignotherway_endTime_editText.setText(dateFormat.format(new Date()));
        assignotherway_category_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                assignCategory = ( String.valueOf ( ( ( RadioButton ) findViewById ( checkedId ) ).getText () ) );
            }
        });
        assignotherway_classification_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                assignClassification = ( String.valueOf ( ( ( RadioButton ) findViewById ( checkedId ) ).getText () ) );
            }
        });
    }
    @Event(value = {
            R.id.assignotherway_startTime_editText,
            R.id.assignotherway_endTime_editText,
            R.id.assignotherway_search_button,
            R.id.assignotherway_assign_button,
            R.id.assignotherway_result_button
    },type = View.OnClickListener.class)
    private void btnClick(View view) {
            switch (view.getId()) {
                case R.id.assignotherway_startTime_editText:
                    DateTimePickDialogUtil startdateTimePicKDialog = new DateTimePickDialogUtil(
                            AssignOtherWayUI.this, String.valueOf(assignotherway_startTime_editText.getText()));
                    startdateTimePicKDialog.dateTimePicKDialog(assignotherway_startTime_editText);
                    break;
                case R.id.assignotherway_endTime_editText:
                    DateTimePickDialogUtil enddateTimePicKDialog = new DateTimePickDialogUtil(
                            AssignOtherWayUI.this, String.valueOf(assignotherway_endTime_editText.getText()));
                    enddateTimePicKDialog.dateTimePicKDialog(assignotherway_endTime_editText);
                    break;
                case R.id.assignotherway_search_button:
                    beginDate=assignotherway_startTime_editText.getText().toString().trim();
                    endDate=assignotherway_endTime_editText.getText().toString().trim();
                    nowCategory=assignCategory;
                    nowClassification=assignClassification;
                    if("".equals(nowCategory)||"".equals(nowClassification)||"".equals(beginDate)||"".equals(endDate)){
                        new CoolToast(getBaseContext()).show("查询条件不能为空！");
                        return;
                    }
                    startActivity(new Intent(AssignOtherWayUI.this,LoadingUI.class));
                    list.clear();
//                    gridAdapter.notifyDataSetChanged();
                    AssignThread(0);
                    break;
                case R.id.assignotherway_assign_button:
                    if(count==null||count==0){
                       new  CoolToast(getBaseContext()).show("分配数为0");
                       return;
                    }
                    assignJson= JSON.toJSONString(listAssgin,true);
                    startActivity(new Intent(AssignOtherWayUI.this,LoadingUI.class));
                    AssignThread(1);
                    break;
                case R.id.assignotherway_result_button:
                    startActivity(new Intent(AssignOtherWayUI.this,LoadingUI.class));
                    AssignThread(2);
                    break;
            }
    }
    private boolean compare(){
        listAssgin=new ArrayList<>();
        count=0;
        Assign assign;
        for(int i=0;i<list.size();i++){
            Integer temp=listLine.get(i).getNum();
            if(temp!=0) {
                count += temp;
                assign = new Assign();
                assign.setUserId(listLine.get(i).getId());
                assign.setNum(listLine.get(i).getNum());
                listAssgin.add(assign);
            }
        }
        if(count>total){
            return false;
        }else{
            return true;
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1:
                    LoadingUI.instance.finish();
                    new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                    break;
                case 0:
                    if(total==0){
                        list.clear();
                    }
                    listLine=new ArrayList();
                    count=0;
                    assignotherway_assign_textView.setText("分配：0张");
                    listAssgin=new ArrayList<>();
                    Line line;
                    for(int i=0;i<list.size();i++){
                        line=new Line();
                        line.setName(String.valueOf(((Map)list.get(i)).get("uname")));
                        line.setId((Integer) ((Map)list.get(i)).get("id"));
                        listLine.add(line);
                    }
                    firstIn=true;
                    gridAdapter=new GridAdapter(listLine,AssignOtherWayUI.this);
                    assignotherway_gridView.setAdapter(gridAdapter);
//                    gridAdapter.notifyDataSetChanged();
                    assignotherway_total_textView.setText("合计："+total+"张");
                    LoadingUI.instance.finish();
                    break;
                case 1:
                    new CoolToast(getBaseContext()).show(String.valueOf(msg.obj));

                    AssignThread(0);
                    break;
                case 2:
//                    Log.e("<<", String.valueOf(msg.obj));
                    LoadingUI.instance.finish();
                    if(msg.obj==null||"[]".equals(String.valueOf(msg.obj))){
                        new CoolToast(getBaseContext()).show("结果为空");
                        return;
                    }
                    Intent in = new Intent(AssignOtherWayUI.this, AssignResultUI.class);
                    in.putExtra("result", String.valueOf(msg.obj));
                    startActivity(in);
                    break;
            }
        }
    };

    private void setCount(){
        assignotherway_assign_textView.setText("分配："+count+"张");
    }
    public void AssignThread(int what) {
        final Message message = new Message ();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            total = assignUtil.getOtherWayCount(beginDate,endDate, Application.getUsers().getId(),nowCategory,nowClassification);
                            list=new UserUtil().getUserByDept(Application.getUsers().getId());
                            message.what = 0;
                            handler.sendMessage ( message );
                            break;
                        case 1:
                            message.obj=assignUtil.assignOtherWay(beginDate,endDate, Application.getUsers().getId(),nowCategory,nowClassification,assignJson);
                            message.what = 1;
                            handler.sendMessage ( message );
                            break;
                        case 2:
                            message.obj=assignUtil.getAssignResult(assignotherway_startTime_editText.getText().toString().trim(),assignotherway_endTime_editText.getText().toString().trim(), Application.getUsers().getId());
                            message.what=2;
                            handler.sendMessage(message);
                            break;
                    }
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage ();
                    handler.sendMessage ( message );
                }
            }
        }.start();
    }
    private class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Line> lines;
        private Context context;
        public GridAdapter(List<Line> list,Context context) {
            this.inflater = LayoutInflater.from(context);
            this.lines = list;
            this.context = context;
        }
        @Override
        public int getCount() {
            return lines.size();
        }
        @Override
        public Object getItem(int i) {
            return lines.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            UserItem userItem;
            if (view == null) {
                view = inflater.inflate(R.layout.assignitem, viewGroup, false);
                userItem = new UserItem();
                userItem.assignItem__name_textView= (TextView) view.findViewById(R.id.assignItem__name_textView);
                userItem.assignItem__num_editText= (EditText) view.findViewById(R.id.assignItem__num_editText);
                userItem.assignItem__id_textView= (TextView) view.findViewById(R.id.assignItem__id_textView);
                view.setTag(userItem);
            } else {
                userItem = (UserItem) view.getTag();
            }
            Log.e("line:", lines.get(position).getName());
            userItem.assignItem__num_editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_text,
                            (ViewGroup) findViewById(R.id.dialog_text));
                    EditText textEditText= (EditText) layout.findViewById(R.id.text);
                    String tm=String.valueOf(userItem.assignItem__num_editText.getText());
                    if("0".equals(tm)){
                        textEditText.setText("");
                    }else {
                        textEditText.setText(tm);
                    }
                    textEditText.setFocusable(true);
                    textEditText.setFocusableInTouchMode(true);
//                    textEditText.requestFocus();
//                    textEditText.setSelection(textEditText.getText().length());
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(textEditText,InputMethodManager.SHOW_FORCED);

                    new AlertDialog.Builder(AssignOtherWayUI.this).setTitle("请输入张数").setView(layout)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String temp= String.valueOf(textEditText.getText());
                                    if(HerbalUtil.isNumeric(temp)&&!"".equals(temp)) {
                                        lines.get(position).setNum(Integer.parseInt(temp));
                                        if(compare()){
                                            setCount();
                                            userItem.assignItem__num_editText.setText(temp);
                                        }else{
                                            lines.get(position).setNum(0);
                                            userItem.assignItem__num_editText.setText("0");
                                            new CoolToast(getBaseContext()).show("已超过可分配张数");
                                        }


                                    }else{
                                        lines.get(position).setNum(0);
                                        Log.e(">>","非数字");
                                    }
                                }
                            }) .setNegativeButton("取消", null).show();
                }
            });
            userItem.assignItem__name_textView.setText(""+lines.get(position).getName());
            userItem.assignItem__id_textView.setText(""+lines.get(position).getId());
            userItem.assignItem__num_editText.setText(""+lines.get(position).getNum());
            return view;
        }
        private class UserItem {
            private TextView assignItem__name_textView;
            private EditText assignItem__num_editText;
            private TextView assignItem__id_textView;
        }
    }
    private class Line{
        private Integer num=0;
        private Integer id;
        private String name;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }
    private class Assign{
        private Integer userId;
        private Integer num;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }
    }
}
