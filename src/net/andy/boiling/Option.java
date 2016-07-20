package net.andy.boiling;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.ChineseToSpeech;
import net.andy.com.CoolToast;

/**
 * Created by Administrator on 2014-11-10.
 * 设置系统参数
 */
public class Option extends Activity {
    EditText server;
    Button button;
    Button speech;
    CheckBox checkBox;
    AppOption appOption = new AppOption();
    ChineseToSpeech chineseToSpeech = new ChineseToSpeech();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.option);

        server = (EditText) findViewById(R.id.option_server_editText);
        button = (Button) findViewById(R.id.option_server_button);
        checkBox = (CheckBox) findViewById(R.id.option_state_check);
        server.setText(appOption.getOption(appOption.APP_OPTION_SERVER));
//        server.setText(Application.getServerIP());
        checkBox.setChecked(appOption.getOption(AppOption.APP_OPTION_STATE).equals("YES"));

        button.setOnClickListener(new ButtonOnClick());
        speech = (Button) findViewById(R.id.option_speech_button);
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chineseToSpeech.speech("成功朗读");
            }
        });
    }

    public class ButtonOnClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            Application.setServerIP(String.valueOf(server.getText()));
            appOption.setOption(AppOption.APP_OPTION_SERVER,String.valueOf(server.getText()) );
            appOption.setOption(AppOption.APP_OPTION_STATE,checkBox.isChecked()?"YES":"NO");
            new CoolToast(getBaseContext()).show("保存成功");
        }
    }

    @Override
    protected void onDestroy() {
        chineseToSpeech.destroy();
        super.onDestroy();
    }
}
