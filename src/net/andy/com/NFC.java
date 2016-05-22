package net.andy.com;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Parcelable;

/**
 * Created by Administrator on 2014-11-12.
 * NFC工具
 */
public class NFC {
    private Activity activity;
    private NfcAdapter nfcAdapter;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    public NFC(Activity activity) throws Exception {
        this.activity = activity;
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity.getBaseContext());
        if (nfcAdapter == null) {
            throw new Exception("该设备不支持NFC功能");
        } else if (!nfcAdapter.isEnabled()) {
            throw new Exception("NFC功能没有打开");
        }

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        ndef.addCategory("*/*");
        mFilters = new IntentFilter[]{ndef};// 过滤器
        mTechLists = new String[][]{
//                new String[]{MifareClassic.class.getName()},
                new String[]{NfcA.class.getName()}
        };// 允许扫描的标签类型
    }

    public void enableForegroundDispatch(PendingIntent pendingIntent) {
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, mFilters, mTechLists);
    }

    public void disableForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(activity);
    }

    /**
     * 读取标签ID
     *
     * @param intent 意图
     * @return 字符串
     */
    public String readID(Intent intent) {
        return bytesToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
    }

    /**
     * 读取标签内容
     *
     * @param intent 意图
     * @return 字符串
     */
    public String readContent(Intent intent) {
        String resultStr = "";
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {

            NdefMessage[] msgs = new NdefMessage[rawMsgs.length];

            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
                NdefRecord[] records = msgs[i].getRecords();
                for (NdefRecord record : records) {
                    resultStr = new String(record.getPayload()) + resultStr;
                }
            }
        }

        return resultStr;
    }

    /**
     * Convert byte[] to hex string. 把字节数组转化为字符串
     * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
