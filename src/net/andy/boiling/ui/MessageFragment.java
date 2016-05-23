package net.andy.boiling.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import net.andy.boiling.R;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Guang on 2016/2/14.
 */
public class MessageFragment extends Fragment {
    private static MessageFragment messageFragment;
    private View v;
    private TextView user_info_view;
    private TextView station_view;
    public boolean run;
    private final Handler handler = new Handler();
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (run) {
                handler.postDelayed(this, 30000);
//                postService();
            } else {
                handler.removeCallbacks(this);
                System.out.println(getActivity() + "停止");
            }
        }
    };
    public static MessageFragment getInstance() {
        if (messageFragment == null)
            messageFragment = new MessageFragment();
        return messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView", "执行");
        v = inflater.inflate(R.layout.messagefragement, container, false);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        run = false;
    }

    @Override
    public void onResume() {
        Log.i("onResume", "执行");
        super.onResume();
        station_view.setText(new AppOption().getOption(AppOption.APP_OPTION_STATION));
//        run = true;
//        handler.removeCallbacks(task);
//        handler.postDelayed(task, 10);
    }
    @Override
    public void onPause() {
        Log.i("onPause", "执行");
        super.onPause();
//        run = false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("onActivityCreated", "执行");
        user_info_view = (TextView) v.findViewById(R.id.user_info_view);
        station_view=(TextView)v.findViewById(R.id.station_view);
        user_info_view.setText(Application.getUsers().getUname());
        String stationName=new AppOption().getOption(AppOption.APP_OPTION_STATION);
        if(stationName.length()>9) {
            station_view.setText(stationName.substring(0,9));
        }else{
            station_view.setText(stationName);
        }
//        run=true;
//        handler.postDelayed(task, 10);
    }
    public void postService() {
        final Message message = new Message();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        Log.e("在线请求异常", String.valueOf(msg.obj));
                        break;
                    case 0:
                        station_view.setText(new AppOption().getOption(AppOption.APP_OPTION_STATION));
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                        new Http().get("online.do?userId="+new AppOption().getOption(AppOption.APP_OPTION_USER));
                        message.what = 0;
                        handler.sendMessage(message);
                } catch (Exception e) {
                    message.what = -1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        run = false;
    }
}