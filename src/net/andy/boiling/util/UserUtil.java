package net.andy.boiling.util;

import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * UserUtil
 *
 * @author RongGuang
 * @date 2015/12/8
 */
public class UserUtil {
    /**
     * 修改密码
     *
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
}
