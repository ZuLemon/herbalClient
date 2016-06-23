package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.attr.password;

/**
 * UserUtil
 *
 * @author RongGuang
 * @date 2015/12/8
 */
public class UserUtil {
    private ReturnDomain returnDomain;
    /**
     * 修改密码
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws Exception
     */
    public String changePassword(String id, String oldPassword, String newPassword) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", id ) );
        pairs.add ( new BasicNameValuePair ( "oldPassword", oldPassword ) );
        pairs.add ( new BasicNameValuePair ( "newPassword", newPassword ) );
        try {
            return ( String ) ( ( Map ) ( new Http ().post ( "/user/changePassword.do", pairs, Map.class ) ) ).get ( "returnInfo" );
        } catch (Exception e) {
            throw new Exception ( e.getMessage () );
        }
    }

    /**
     * 验证密码
     * @param userId
     * @param password
     * @return
     */
    public String confirmPasswd(String userId,String password) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", userId));
        pairs.add(new BasicNameValuePair("password", password));
        pairs.add(new BasicNameValuePair("browser", "HttpClient"));
        pairs.add(new BasicNameValuePair("version", "mobile"));
        try {
            return (String) ((Map) (new Http().post("login1.do", pairs, Map.class))).get("info");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception ("网络异常:" + e.getMessage () );
        }
    }

    /**
     *  获取用户列表
     * @param userId
     * @return
     * @throws Exception
     */
    public List getUserByDept(Integer userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "userId", String.valueOf(userId) ) );
        returnDomain = (ReturnDomain) new Http().post ( "user/getUserByDept.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
