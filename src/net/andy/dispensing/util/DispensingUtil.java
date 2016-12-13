package net.andy.dispensing.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import net.andy.com.Application;
import net.andy.dispensing.domain.DispensingDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * ExtractUtil
 *
 * @author RongGuang
 * @date 2016/03/02
 */
public class DispensingUtil {
    private static String uri="http://"+ Application.getServerIP().trim()+"/herbal/";
    private ReturnDomain returnDomain;
    /**
     获取待调剂处方
     */
    public DispensingDomain getDispensingByPlanId(String device,String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "device", device ) );
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) Http.post ( "dispensing/getDispensingByPlanId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            if(returnDomain.getObject ()==null){
                return null;
            }
            return JSON.parseObject ( returnDomain.getObject ().toString (),  DispensingDomain.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     获取处方总量
     */
    public DispensingDomain getDispensing(String disId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", disId ) );
        returnDomain = ( ReturnDomain ) Http.post ( "dispensing/getDispensing.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  DispensingDomain.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     完成处方
     */
    public String updateToFinish(String id,String tagId,String imgName) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", id ) );
        pairs.add ( new BasicNameValuePair ( "tagId", tagId ) );
        pairs.add ( new BasicNameValuePair ( "imgName", imgName ) );
        returnDomain = ( ReturnDomain ) Http.post ( "dispensing/updateToFinish.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "绑定成功";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     查看图片
     */
    public Bitmap getImageByPId(Integer pId) throws Exception {
        //创建一个url对象
        //打开URL对应的资源输入流
        InputStream is= null;
        Bitmap bitmap=null;
        try {
            URL urlx=new URL(uri+"dispensingImage/getImageByPId.do?pId="+pId);
            Log.e("URL",uri+"dispensingImage/getImageByPId.do?pId="+pId);
            is = urlx.openStream();
            bitmap= BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
//            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                return null;
//                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
