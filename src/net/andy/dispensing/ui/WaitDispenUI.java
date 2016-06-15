package net.andy.dispensing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.dispensing.util.OnlineUtil;
import net.andy.dispensing.util.ReportUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/6/6.
 */
public class WaitDispenUI extends Activity{
    private ListView waitdispen_listView;
    private WaitDispenAdapter waitDispenAdapter;
    private Integer userId;
    private List waitList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitdispen);
        waitdispen_listView= (ListView) findViewById(R.id.waitdispen_listView);
        waitDispenAdapter=new WaitDispenAdapter(this);
        onlineThread(0);
    }
    public void setListView() {
            waitdispen_listView.setAdapter ( waitDispenAdapter );
            waitDispenAdapter.notifyDataSetChanged();

    }
    private void onlineThread(int what) {
        final Message message = new Message ();
        final Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage ( msg );
                switch (msg.what) {
                    case -1:
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                    case 0:
                        setListView ();
                        break;
                    case 1:
                        onlineThread(0);
                        new CoolToast( getBaseContext () ).show ( ( String ) msg.obj );
                        break;
                }
            }
        };

        new Thread () {
            @Override
            public void run() {
                super.run ();
                try {
                    switch (what){
                        case 0:
                            waitList= new ReportUtil().getNoDispensing();
                            message.what = 0;
                            handler.sendMessage ( message );
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

    private class WaitDispenAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public WaitDispenAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return waitList.size();
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
            final WaitDispenView waitDispenView;
            if (view == null) {
                view = inflater.inflate(R.layout.waitdispenlist, viewGroup, false);
                waitDispenView = new WaitDispenView();
                waitDispenView.waitdispen_id_textView  = (TextView) view.findViewById(R.id.waitdispen_id_textView);
                waitDispenView.waitdispen_count_textView= (TextView) view.findViewById(R.id.waitdispen_count_textView);
                waitDispenView.waitdispen_ruleName_textView = (TextView) view.findViewById(R.id.waitdispen_ruleName_textView);
                waitDispenView.waitdispen_waitCnt_textView= (TextView) view.findViewById(R.id.waitdispen_waitCnt_textView);
                view.setTag(waitDispenView);
            } else {
                waitDispenView = (WaitDispenView) view.getTag();
            }
            Map map = (Map) waitList.get(i);
            Log.e("map", map.toString());
            waitDispenView.waitdispen_id_textView  .setText(String.valueOf(map.get("id")));
            waitDispenView.waitdispen_waitCnt_textView.setText(""+ map.get("ct"));
            waitDispenView.waitdispen_ruleName_textView .setText(""+ map.get("name"));
            waitDispenView.waitdispen_count_textView .setText(i+1+"");
            return view;
        }
        private class WaitDispenView {
            private TextView waitdispen_id_textView;
            private TextView waitdispen_count_textView;
            private TextView waitdispen_waitCnt_textView;
            private TextView waitdispen_ruleName_textView;
        }
    }
}
