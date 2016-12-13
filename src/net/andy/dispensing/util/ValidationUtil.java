package net.andy.dispensing.util;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.AppOption;
import net.andy.com.Http;
import net.andy.dispensing.domain.DispensingValidationDomain;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * ReplenishUtil
 *
 * @author RongGuang
 * @date 2016/05/26
 */
public class ValidationUtil {
    private ReturnDomain returnDomain;

    /**
     * 获取已选择符合列表
     */
    public DispensingValidationDomain getValidationByDis(String disId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("disId", disId));
        returnDomain = (ReturnDomain) Http.post("validation/getValidationByDis.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            if(returnDomain.getObject()==null){
                Log.i("object","null");
                return null;
            }
            return JSON.parseObject(returnDomain.getObject().toString(), DispensingValidationDomain.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
    /**
     * 获取可复核列表
     */
    public List getWaitValidation(String userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", userId));
        returnDomain = (ReturnDomain) Http.post("validation/getWaitValidation.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
    /**
     * 获取已选择符合列表
     */
    public List getValidationByStatus(String begin,String end,String userId,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("begin", begin));
        pairs.add(new BasicNameValuePair("end", end));
        pairs.add(new BasicNameValuePair("userId", userId));
        pairs.add(new BasicNameValuePair("status", status));
        returnDomain = (ReturnDomain) Http.post("validation/getValidationByStatus.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
    /**
     * 确认复核记录
     */
    public String insert(String disId,String userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("disId", disId));
        pairs.add(new BasicNameValuePair("userId", userId));
        returnDomain = (ReturnDomain) Http.post("validation/insert.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return "已选中复核";
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
    /**
     * 通过复核记录
     */
    public String pass(String id) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", id));
        returnDomain = (ReturnDomain) Http.post("validation/pass.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return "完成复核";
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
}
