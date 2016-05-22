package net.andy.com;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2014-11-10.
 * 应用程序配置信息
 */
public class AppOption {
    public static final String APP_DEVICE_ID="deviceid";
    public static final String APP_OPTION_VERSION="version";
    public static final String APP_OPTION_SERVER="server";
    public static final String APP_OPTION_STATE="state";
    public static final String APP_OPTION_USER="user";
    public static final String APP_OPTION_PASSWORD="password";
    public static final String APP_OPTION_WAITTIME="waittime";
    public static final String APP_OPTION_HERSPEC="herbspec";
    public static final String APP_OPTION_STATION="station";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AppOption() {
        this.sharedPreferences = net.andy.com.Application.getContext().getSharedPreferences("boiling.option", Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public String getOption(String key){
        return sharedPreferences.getString(key,"");
    }

    public void setOption(String key,String value){
        editor.putString(key, value);
        editor.apply();
    }

}
