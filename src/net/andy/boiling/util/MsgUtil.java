package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.EquipmentDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.AppOption;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MsgtUtil
 *
 * @author RongGuang
 * @date 2016/02/16
 */
public class MsgUtil {
    private ReturnDomain returnDomain;
    protected List<Map> returnResult;
    /**
     * 获得消息
     * @return
     * @throws Exception
     */
    public List<Map> getMsg() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        try {
            returnDomain = (ReturnDomain) (new Http().post("extracting/getFinishExtracting.do", pairs, ReturnDomain.class));
            if (returnDomain.getSuccess()) {
                returnResult = (List<Map>) returnDomain.getObject();
            }
        }catch (Exception e) {

        }
        return returnResult;
    }
    public int getMsgCount() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        try {
            returnDomain = (ReturnDomain) (new Http().post("extracting/getFinishExtracting.do", pairs, ReturnDomain.class));
            if (returnDomain.getSuccess()) {
                returnResult = (List<Map>) returnDomain.getObject();
            }
        }catch (Exception e) {

        }
        return returnResult.size();
    }
}
