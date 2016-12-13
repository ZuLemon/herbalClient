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
     * 获取紧急上药信息
     */
    public List getReplenishByUserAndStatus(String type,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        pairs.add(new BasicNameValuePair("type", type));
        pairs.add(new BasicNameValuePair("status", status));
        returnDomain = (ReturnDomain) Http.post("replenish/getReplenishByUserAndStatus.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }

//
//    /**
//     * 获取上药信息
//     */
//    public List getReplenishByStatus(String url,String userId,String status) throws Exception {
//        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        pairs.add(new BasicNameValuePair("status", status));
//        pairs.add(new BasicNameValuePair("userId", userId));
//        returnDomain = (ReturnDomain) Http.post(url, pairs, ReturnDomain.class);
//        if (returnDomain.getSuccess()) {
//            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
//        } else {
//            throw new Exception(returnDomain.getException());
//        }
//    }

    /**
     * 完成上药操作
     */
    public String setStatus(Integer id,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", String.valueOf(id)));
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        pairs.add(new BasicNameValuePair("status", status));
        returnDomain = (ReturnDomain) Http.post("replenish/setStatus.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return "操作成功！";
        } else {
            throw new Exception(returnDomain.getException());
        }
    }

//    /**
//     * 完成上药操作
//     */
//    public String finish(Integer id) throws Exception {
//        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        pairs.add(new BasicNameValuePair("id", String.valueOf(id)));
//        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
//        returnDomain = (ReturnDomain) Http.post("replenish/finish.do", pairs, ReturnDomain.class);
//        if (returnDomain.getSuccess()) {
//            return "上药完成！";
//        } else {
//            throw new Exception(returnDomain.getException());
//        }
//    }
    /**
     * 提醒上药
     */
    public String request(Integer stationId, String herbId,String type) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("stationId", String.valueOf(stationId)));
        pairs.add(new BasicNameValuePair("herbId", herbId));
        pairs.add(new BasicNameValuePair("type", type));
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        returnDomain = (ReturnDomain) Http.post("replenish/request.do", pairs, ReturnDomain.class);
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
    public List getReplenishListByUserAndType(String type,String status,String startTime,String endTime) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        pairs.add(new BasicNameValuePair("type", type));
        pairs.add(new BasicNameValuePair("status", status));
        pairs.add(new BasicNameValuePair("startTime", startTime));
        pairs.add(new BasicNameValuePair("endTime", endTime));
        returnDomain = (ReturnDomain) Http.post("replenish/getReplenishListByUserAndType.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
    /**
     * 获取紧急上药信息
     */
    public List getReplenishByRequestUser(String type,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        pairs.add(new BasicNameValuePair("type", type));
        pairs.add(new BasicNameValuePair("status", status));
        returnDomain = (ReturnDomain) Http.post("replenish/getReplenishByRequestUser.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
}
