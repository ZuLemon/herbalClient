package net.andy.com;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.domain.Users;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录用户
 *
 * @author RongGuang
 * @date 2016/2/4.
 */
public class LogUserInfo {
    public void setLogUsers() {
        String userid = new AppOption().getOption(AppOption.APP_OPTION_USER);
        if (userid == null || "".equals(userid)) {
            Log.i("LogUsers","空");
        } else {
            Application.setUsers(getUsersInfoById(userid));
            Log.i("LogUsers","用户设置成功");
        }
    }

    private Users getUsersInfoById(String id) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", id));
        try {
            String info= (String) new Http().post("user/findById.do", pairs, String.class);
            return JSON.parseObject(info,Users.class);
//            if (returnDomain.getSuccess()) {
//                System.out.println(">>" + returnDomain.getObject().toString());
//                return JSON.parseObject(returnDomain.getObject().toString(), Users.class);
//            } else {
//                throw new Exception(returnDomain.getException());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
