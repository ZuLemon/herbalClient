package net.andy.boiling.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import net.andy.boiling.Login;

/**
 * 开机启动
 *@author RongGuang
 * @date 2016/02/03
 */
public class BootUtil extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
         * 开机启动服务*
         * Intent service=new Intent(context, MyService.class);
         * context.startService(service);
         */


//        开机启动的Activity
         Intent activity=new Intent(context,Login.class);
         activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );//不加此句会报错。
         context.startActivity(activity);
        /* 开机启动的应用 */
//        Intent appli = context.getPackageManager().getLaunchIntentForPackage("com.test");
//        context.startActivity(appli);
    }
}