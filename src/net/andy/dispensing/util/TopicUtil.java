package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.AppOption;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/12/1.
 */
public class TopicUtil {
    private ReturnDomain returnDomain;

    /**
     *
     */
    public List getTopicByUserId() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        returnDomain = (ReturnDomain) Http.post("topic/getTopicByUserId.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
    /**
     *
     */
    public String insert(String topic) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        pairs.add(new BasicNameValuePair("topic",topic));
        returnDomain = (ReturnDomain) Http.post("topic/insert.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return "订阅成功";
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
    /**
     *
     */
    public String delete(String topic) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        pairs.add(new BasicNameValuePair("topic",topic));
        returnDomain = (ReturnDomain) Http.post("topic/delete.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return "取消订阅";
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
}
