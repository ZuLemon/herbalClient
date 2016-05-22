package net.andy.boiling.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import net.andy.boiling.R;
import net.andy.boiling.util.UserUtil;
import net.andy.com.AppOption;
import net.andy.com.CoolToast;

/**
 * 修改密码
 */
public class MidifyPassUI extends Activity {
    private EditText userId_editText;
    private EditText oldPassword_editText;
    private EditText newPassword_editText;
    private EditText repeatPassword_editText;
    AppOption appOption = new AppOption ();
    Message message = new Message ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.midifypass );
        userId_editText = ( EditText ) this.findViewById ( R.id.midify_userId_editText );
        oldPassword_editText = ( EditText ) this.findViewById ( R.id.old_password_editText );
        newPassword_editText = ( EditText ) this.findViewById ( R.id.new_password_editText );
        repeatPassword_editText = ( EditText ) this.findViewById ( R.id.repeat_password_editText );
        Button verify_ok_button = ( Button ) this.findViewById ( R.id.verify_ok_button );
        if ( appOption.getOption ( AppOption.APP_OPTION_STATE ).equals ( "YES" ) ) {
            userId_editText.setText ( appOption.getOption ( AppOption.APP_OPTION_USER ) );
        }
        verify_ok_button.setOnClickListener ( new SubmitOnclick () );
    }
    public class SubmitOnclick implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            String userid = userId_editText.getText ().toString ().trim ();
            String oldPassword = oldPassword_editText.getText ().toString ().trim ();
            String newPassword = newPassword_editText.getText ().toString ().trim ();
            String repeatPassword = repeatPassword_editText.getText ().toString ().trim ();

            if ( "".equals ( newPassword ) || "".equals ( oldPassword ) ) {
                new CoolToast ( getBaseContext () ).show ( "密码不能为空，请重新输入" );
                oldPassword_editText.requestFocus ();
                return;
            }
            if ( !newPassword.equals ( repeatPassword ) ) {
                newPassword_editText.setText ( "" );
                repeatPassword_editText.setText ( "" );
                new CoolToast ( getBaseContext () ).show ( "两次输入密码不一致，请重新输入" );
                newPassword_editText.requestFocus ();
                return;
            }
            final Handler handler = new Handler () {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case -1:
                            reset ();
                            new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                            oldPassword_editText.requestFocus ();
                            break;
                        case 0:
                            new CoolToast ( getBaseContext () ).show ( ( String ) msg.obj );
                            onBackPressed ();
                    }
                }
            };
            new Thread () {
                @Override
                public void run() {
                    try {
                        String returnInfo = new UserUtil ().changePassword ( userid, oldPassword, newPassword );
                        if ( "success".equals ( returnInfo ) ) {
                            message.what = 0;
                            message.obj = "修改成功";
                            handler.sendMessage ( message );
                        } else {
                            message.what = -1;
                            message.obj = returnInfo;
                            handler.sendMessage ( message );
                        }
                    } catch (Exception e) {
                        message.what = -1;
                        message.obj = e.getMessage ();
                        handler.sendMessage ( message );
                    }
                }
            }.start ();
        }
    }
    private void reset() {
        oldPassword_editText.setText ( "" );
        newPassword_editText.setText ( "" );
        repeatPassword_editText.setText ( "" );
    }
}
