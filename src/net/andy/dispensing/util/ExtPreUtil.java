package net.andy.dispensing.util;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Guang on 2016/5/18.
 */
public class ExtPreUtil {
    private ReturnDomain returnDomain;
    /**
     提取处方
     */
    public Map importPres(Integer interval) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair( "interval", String.valueOf(interval ) ));
        returnDomain = ( ReturnDomain ) Http.post ( "extPres/importPres.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Map.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     提取处方
     */
    public Map importPresByTime(String beginTime,String endTime) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair( "begin", beginTime ));
        pairs.add ( new BasicNameValuePair( "end", endTime ));
        returnDomain = ( ReturnDomain ) Http.post ( "extPres/importPresByTime.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Map.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     提取处方
     */
    public Map importPresBypresId(String presId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair( "presId", presId ));
        returnDomain = ( ReturnDomain ) Http.post ( "extPres/importPresByPresId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Map.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     提取处方时间
     */
    public Map getInterval() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        returnDomain = ( ReturnDomain ) Http.post ( "interval/getInterval.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Map.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     设置处方时间
     */
    public String setInterval(Integer id,Integer interval) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair( "id", String.valueOf(id ) ));
        pairs.add ( new BasicNameValuePair( "interval", String.valueOf(interval ) ));
        returnDomain = ( ReturnDomain ) Http.post ( "interval/update.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "修改成功";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
