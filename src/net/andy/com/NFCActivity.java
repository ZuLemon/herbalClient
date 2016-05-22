package net.andy.com;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2014-11-20.
 * nfc基础控件
 */
public class NFCActivity extends Activity {
    private NFC nfc;
    private PendingIntent pendingIntent;

    public NFC getNfc() {
        return nfc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            nfc = new NFC(this);
        } catch (Exception e) {
            new CoolToast(getBaseContext()).show(e.getMessage());
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        nfc.enableForegroundDispatch(pendingIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        nfc.disableForegroundDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
