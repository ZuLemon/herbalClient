package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import net.andy.dispensing.domain.RulesDomain;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * AssignUtil
 *
 * @author RongGuang
 * @date 2016/06/17
 */
public class AssignUtil {
    private ReturnDomain returnDomain;

    /**
     * 查询预发放处方数量
     * @param begin
     * @param end
     * @param userId
     * @param category
     * @param classification
     * @return
     * @throws Exception
     */
    public Integer getOtherWayCount(String begin,String end,Integer userId,String category,String classification) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add(new BasicNameValuePair("begin",begin));
        pairs.add(new BasicNameValuePair("end",end));
        pairs.add(new BasicNameValuePair("userId",String.valueOf(userId)));
        pairs.add(new BasicNameValuePair("category",category));
        pairs.add(new BasicNameValuePair("classification",classification));
        returnDomain = ( ReturnDomain ) Http.post ( "prescription/getOtherWayCount.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Integer.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    /**
     * 分配
     * @param begin
     * @param end
     * @param userId
     * @param category
     * @param classification
     * @param assign
     * @return
     * @throws Exception
     */
    public String assignOtherWay(String begin,String end,Integer userId,String category,String classification,String assign) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("begin", begin));
        pairs.add(new BasicNameValuePair("end", end));
        pairs.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        pairs.add(new BasicNameValuePair("category", category));
        pairs.add(new BasicNameValuePair("classification", classification));
        pairs.add(new BasicNameValuePair("assign", assign));
        returnDomain = (ReturnDomain) Http.post("prescription/assignOtherWay.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return "分配成功";
        } else {
            throw new Exception(returnDomain.getException());
        }
    }

    /**
     * 查询分配结果
     * @param begin
     * @param end
     * @param userId
     * @return
     * @throws Exception
     */
    public List getAssignResult(String begin,String end,Integer userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add(new BasicNameValuePair("begin",begin));
        pairs.add(new BasicNameValuePair("end",end));
        pairs.add(new BasicNameValuePair("userId",String.valueOf(userId)));
        returnDomain = ( ReturnDomain ) Http.post ( "prescription/getAssignResult.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
