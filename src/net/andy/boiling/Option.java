package net.andy.boiling;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import net.andy.com.*;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2014-11-10.
 * 设置系统参数
 */
public class Option extends Activity {
    @ViewInject(R.id.option_server_editText)
    private EditText option_server_editText;
    @ViewInject(R.id.option_server_button)
    private Button option_server_button;
    @ViewInject(R.id.option_speech_button)
    private Button option_speech_button;
    @ViewInject(R.id.option_state_check)
    private CheckBox option_state_check;
    private AppOption appOption = new AppOption();
    private ChineseToSpeech chineseToSpeech = new ChineseToSpeech();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);
        x.view().inject(this);
        option_server_editText.setText(appOption.getOption(appOption.APP_OPTION_SERVER));
        option_state_check.setChecked(appOption.getOption(AppOption.APP_OPTION_STATE).equals("YES"));
    }
    @Event(value = {R.id.option_server_button, R.id.option_speech_button}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.option_server_button:
                Application.setServerIP(String.valueOf(option_server_editText.getText()));
                Http.setUri(String.valueOf(option_server_editText.getText()).trim());
                appOption.setOption(AppOption.APP_OPTION_SERVER, String.valueOf(option_server_editText.getText()));
                appOption.setOption(AppOption.APP_OPTION_STATE, option_state_check.isChecked() ? "YES" : "NO");
                new CoolToast(getBaseContext()).show("保存成功");
                break;
            case R.id.option_speech_button:
                chineseToSpeech.speech("成功朗读");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        chineseToSpeech.destroy();
        super.onDestroy();
    }
}
