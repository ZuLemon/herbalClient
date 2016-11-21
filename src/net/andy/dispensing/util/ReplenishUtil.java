package net.andy.dispensing.util;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.AppOption;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * ReplenishUtil
 *
 * @author RongGuang
 * @date 2016/03/09
 */
public class ReplenishUtil {
    private ReturnDomain returnDomain;

    /**
     * 获取未接收上药信息
     */
    public List getReplenishNoAccept() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        returnDomain = (ReturnDomain) new Http().post("replenish/getReplenishNoAccept.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }


    /**
     * 获取上药信息
     */
    public List getReplenishByStatus(String url,String userId,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("status", status));
        pairs.add(new BasicNameValuePair("userId", userId));
        returnDomain = (ReturnDomain) new Http().post(url, pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }

    /**
     * 完成上药操作
     */
    public String setStatus(String url,String id) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", id));
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        returnDomain = (ReturnDomain) new Http().post(url, pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return "上药成功！";
        } else {
            throw new Exception(returnDomain.getException());
        }
    }

    /**
     * 提醒上药
     */
    public String request(Integer stationId, String herbId,String type) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("stationId", String.valueOf(stationId)));
        pairs.add(new BasicNameValuePair("herbId", herbId));
        pairs.add(new BasicNameValuePair("type", type));
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        returnDomain = (ReturnDomain) new Http().post("replenish/request.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return returnDomain.getObject().toString();
        } else {
            throw new Exception(returnDomain.getException());
        }
    }

    /**
     *  获取请求上药列表
     * @param type
     * @return
     * @throws Exception
     */
    public List getReplenishListByUserAndType(String type,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        pairs.add(new BasicNameValuePair("type", type));
        pairs.add(new BasicNameValuePair("status", status));
        returnDomain = (ReturnDomain) new Http().post("replenish/getReplenishListByUserAndType.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
}
