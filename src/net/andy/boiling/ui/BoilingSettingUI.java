package net.andy.boiling.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.R;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.util.BoilingEffortUtil;
import net.andy.boiling.util.LineEditText;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;
import net.andy.com.Http;
import net.andy.dispensing.domain.StationDomain;
import net.andy.dispensing.ui.AlreadyDisUI;
import net.andy.dispensing.ui.PersonalEffortUI;
import net.andy.dispensing.ui.StationRuleUI;
import net.andy.dispensing.ui.WaitDispenUI;
import net.andy.dispensing.util.SpinnerItem;
import net.andy.dispensing.util.StationUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/2/24.
 */
@ContentView(R.layout.boiling_setting)
public class BoilingSettingUI extends Activity {
    @ViewInject(R.id.boilingsetting_effort_linearLayout)
    private LinearLayout boilingsetting_effort_linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

    }
    @Event(value = R.id.boilingsetting_effort_linearLayout,type = View.OnClickListener.class)
    private void OnClick(View v){
        switch (v.getId()){
            case R.id.boilingsetting_effort_linearLayout:
                Intent intent = new Intent(BoilingSettingUI.this, BoilingEffortUI.class);
                startActivity(intent);
                break;
        }
    }
}
