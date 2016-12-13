package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.CoolToast;
import net.andy.dispensing.domain.ReplenishDomain;
import net.andy.dispensing.util.DatePickDialogUtil;
import net.andy.dispensing.util.ReplenishUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 上药
 * Created by Guang on 2016/3/16.
 */
public class ReplenishUI extends Activity {
    private static final int REPLENISH_POST_CODE=9010;
    @ViewInject(R.id.replenish_title)
    private TextView replenish_title;
    @ViewInject(R.id.replenish_list)
    private ListView replenish_list;
    @ViewInject(R.id.replenish_new)
    private Button replenish_new;
    @ViewInject(R.id.replenish_accept)
    private Button replenish_accept;
    @ViewInject(R.id.replenish_finish)
    private Button replenish_finish;
    @ViewInject(R.id.replenish_dateTime_linearLayout)
    private LinearLayout replenish_dateTime_linearLayout;
    @ViewInject(R.id.replenish_startTime_editText)
    private EditText replenish_startTime_editText;
    @ViewInject(R.id.replenish_endTime_editText)
    private EditText replenish_endTime_editText;
    private List list = new ArrayList<>();
    private Adapter adapter;
    private replenishAdapter replenishAdapter;
    private ReplenishUtil util = new ReplenishUtil();
    private int what = 0;
    private List listAll=null;
    private SimpleDateFormat formatx = new SimpleDateFormat("yyyy-MM-dd");
    private String status;
    private Integer id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replenish);
        x.view().inject(this);
        adapter = new Adapter(this);
        replenishAdapter=new replenishAdapter(this);
        setToday();
        setMonitor();
        getList(what);
        replenish_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listAll!=null&&listAll.size()!=0){
                    Map replenishMap = (Map) (listAll.get(position));
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("replenishMap", (Serializable) replenishMap);
                    bundle.putString("status",status);
                    intent.putExtras(bundle);
                    intent.setClass(ReplenishUI.this, ReplenishDetailUI.class);
                    startActivityForResult(intent,REPLENISH_POST_CODE);
                }
            }
        });
    }
    private void setMonitor() {
        replenish_startTime_editText.addTextChangedListener(textWatcher);
        replenish_endTime_editText.addTextChangedListener(textWatcher);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REPLENISH_POST_CODE) {
            if (data != null) {
                String dataStatus = data.getStringExtra("status");
                if (dataStatus == null) {

                } else if ("".equals(dataStatus)) {
                } else if ("申请".equals(dataStatus)) {
                    getList(3);
                } else if ("完成".equals(dataStatus)) {
                    getList(4);
                }
            }
        }
    }

    @Event(value = {
            R.id.replenish_finish,
            R.id.replenish_accept,
            R.id.replenish_new,
            R.id.replenish_normal,
            R.id.replenish_startTime_editText,
            R.id.replenish_endTime_editText
    },type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()){
            case R.id.replenish_new:
                what = 0;
                getList(what);
                replenish_title.setText(((TextView)view).getText());
                break;
            case R.id.replenish_accept:
                what = 1;
                getList(what);
                replenish_title.setText(((TextView)view).getText());
//                Intent intent=new Intent(this,ReplenishListUI.class);
//                startActivity(intent);
                break;
            case R.id.replenish_finish:
                what =4;
                getList(what);
                replenish_title.setText(((TextView)view).getText());
                break;
            case R.id.replenish_normal:
                    what =3;
                    getList(what);
                    replenish_title.setText(((TextView)view).getText()) ;
                    break;
            case R.id.replenish_startTime_editText:
                DatePickDialogUtil startdateTimePicKDialog = new DatePickDialogUtil(
                        ReplenishUI.this,String.valueOf( replenish_startTime_editText.getText()));
                startdateTimePicKDialog.dateTimePicKDialog(replenish_startTime_editText);
                break;
            case R.id.replenish_endTime_editText:
                DatePickDialogUtil enddateTimePicKDialog = new DatePickDialogUtil(
                        ReplenishUI.this, String.valueOf( replenish_endTime_editText.getText()));
                enddateTimePicKDialog.dateTimePicKDialog(replenish_endTime_editText);
                break;
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    replenish_dateTime_linearLayout.setVisibility(View.GONE);
                    listAll=null;
                    replenish_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    replenish_dateTime_linearLayout.setVisibility(View.GONE);
                    list=null;
                    replenish_list.setAdapter(replenishAdapter);
                    replenishAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    replenish_dateTime_linearLayout.setVisibility(View.VISIBLE);
                    listAll=null;
                    replenish_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 4:
                    replenish_dateTime_linearLayout.setVisibility(View.VISIBLE);
                    list=null;
                    replenish_list.setAdapter(replenishAdapter);
                    replenishAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public void getList(int what) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    switch (what) {
                        case 0:
                            status="申请";
                            list = util.getReplenishByUserAndStatus("紧急",status);
                            handler.sendEmptyMessage(0);
                            break;
                        case 1:
                            status="接收";
                            list = util.getReplenishByUserAndStatus("紧急",status);
                            handler.sendEmptyMessage(0);
                            break;
                        case 2:
                            status="完成";
                            list = util.getReplenishByUserAndStatus("常规",status);
                            handler.sendEmptyMessage(2);
                            break;
                        case 3:
                            status="申请";
                            listAll = util.getReplenishListByUserAndType("常规",status,null,null);
                            handler.sendEmptyMessage(1);
                            break;
                        case 4:
                            status="完成";
                            listAll = util.getReplenishListByUserAndType("%",status,replenish_startTime_editText.getText().toString(),replenish_endTime_editText.getText().toString());
                            handler.sendEmptyMessage(4);
                            break;
                        case 5:
                            util.setStatus(id,"接收");
                            list = util.getReplenishByUserAndStatus("紧急",status);
                            handler.sendEmptyMessage(0);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Application.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    public class Adapter extends BaseAdapter {
        private LayoutInflater inflater;
        private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private SimpleDateFormat format2 = new SimpleDateFormat("d日 HH:mm");

        public Adapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
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
            final Hold hold;

            if (view == null) {
                view = inflater.inflate(R.layout.replenishitem, viewGroup, false);
                hold = new Hold();
                hold.id = (TextView) view.findViewById(R.id.id);
                hold.userName = (TextView) view.findViewById(R.id.userName);
                hold.sendTime = (TextView) view.findViewById(R.id.sendTime);
                hold.status = (TextView) view.findViewById(R.id.status);
                hold.herbName = (TextView) view.findViewById(R.id.herbName);
                hold.shelf = (TextView) view.findViewById(R.id.shelf);
                hold.replenish_button = (Button) view.findViewById(R.id.replenish_button);
                view.setTag(hold);
            } else {
                hold = (Hold) view.getTag();
            }
            Map map = (Map) list.get(i);
//            Log.e("map", map.toString());
            hold.id.setText(String.valueOf(map.get("id")));

            try {
                if("申请".equals(status)){
                    hold.userName.setText((CharSequence) map.get("puser"));
                    hold.sendTime.setText(format2.format(format1.parse(String.valueOf(map.get("sendTime")))));
                }else if("接收".equals(status)){
                    hold.userName.setText((CharSequence) map.get("ruser"));
                    hold.sendTime.setText(format2.format(format1.parse(String.valueOf(map.get("finishTime")))));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            hold.status.setText((CharSequence) map.get("status"));
            hold.herbName.setText((CharSequence) map.get("herbName"));
            hold.shelf.setText((CharSequence) map.get("shelf"));
            switch (what) {
                case 0:
                    hold.replenish_button.setText("接收");
                    hold.replenish_button.setVisibility(View.VISIBLE);
                    hold.replenish_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            id=Integer.valueOf(hold.id.getText().toString());
                            getList(5);
                        }
                    });
                    break;
                case 1:
                    hold.replenish_button.setVisibility(View.GONE);
                    break;
                case 2:
                    hold.replenish_button.setVisibility(View.GONE);
                    break;
            }
            return view;
        }
//        private void request(Integer id) {
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    Looper.prepare();
//                    try {
//                        util.setStatus(id,"接收");
//                        handler.sendEmptyMessage(what);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(Application.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                    Looper.loop();
//                }
//            }.start();
//        }
        private class Hold {
            private TextView id;
            private TextView userName;
            private TextView sendTime;
            private TextView status;
            private TextView herbName;
            private TextView shelf;
            private Button replenish_button;
        }
    }
    private class replenishAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public replenishAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listAll.size();
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
            final ReplenishView replenishView;
            if (view == null) {
                view = inflater.inflate(R.layout.replenishlist_item, viewGroup, false);
                replenishView = new ReplenishView();
                replenishView.replenishitem_id  = (TextView) view.findViewById(R.id.replenishitem_id);
                replenishView.replenishitem_name= (TextView) view.findViewById(R.id.replenishitem_name);
                replenishView.replenishitem_count = (TextView) view.findViewById(R.id.replenishitem_count);
                view.setTag(replenishView);
            } else {
                replenishView = (ReplenishView) view.getTag();
            }
            Map map = (Map) listAll.get(i);
            Map herbMap = (Map) map.get("herb");
            replenishView.replenishitem_id.setText(String.valueOf(herbMap.get("herbId")));
            replenishView.replenishitem_name.setText(""+ herbMap.get("herbName"));
            replenishView.replenishitem_count.setText(""+ herbMap.get("ct"));
            return view;
        }
        private class ReplenishView {
            private TextView replenishitem_id;
            private TextView replenishitem_name;
            private TextView replenishitem_count;
        }
    }
    private void setToday(){
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        replenish_startTime_editText.setText(formatx.format(cal_1.getTime()));
        replenish_endTime_editText.setText(formatx.format(cal_1.getTime()));
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            getList(4);
        }
    };
}
