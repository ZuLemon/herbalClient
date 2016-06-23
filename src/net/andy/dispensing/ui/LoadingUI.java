package net.andy.dispensing.ui;

import android.app.Activity;
import android.os.Bundle;
import net.andy.boiling.R;

/**
 * Created by Guang on 2016/6/22.
 */
public class LoadingUI extends Activity {

    public static LoadingUI instance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        instance=this;
//        try {
//            Thread.sleep(1000);
//            finish();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
