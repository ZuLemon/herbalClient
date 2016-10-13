package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * DispensingDetailUtil
 *
 * @author RongGuang
 * @date 2016/03/02
 */
public class DispensingDetailUtil {
    private ReturnDomain returnDomain;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取待调剂处方
     * @param planId
     * @return
     * @throws Exception
     */
    public List getListByPlanId(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensingDetail/getDispensingDetailByPlanId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public void start(String id) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", id ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensingDetail/start.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public String finish(String id) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", id ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensingDetail/finish.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    /**
     * 更新预调剂明细
     * @param disId
     * @param readyId
     * @throws Exception
     */
    public void updateReady(Integer disId,Integer readyId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "disId", String.valueOf(disId) ) );
        pairs.add ( new BasicNameValuePair ( "readyId", String.valueOf(readyId)) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensingDetail/updateReady.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    /**
     * 更新协定处方明细
     * @param disId
     * @throws Exception
     */
    public void updateDue(Integer disId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "disId", String.valueOf(disId) ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensingDetail/updateDue.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    /**
     * 通过disId获取调剂明细
     * @param disId
     * @return List
     * @throws Exception
     */
    public List getDispensingDetailByDisId(Integer disId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "disId", String.valueOf(disId) ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensingDetail/getDispensingDetailByDisId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    /**
     * 获取总量
     * @param disId
     * @return
     * @throws Exception
     */
    public List getQuantityForUnit(Integer disId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "disId", String.valueOf(disId) ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensingDetail/getQuantityForUnit.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
//    public void update(DispensingDetailDomain dis) throws Exception {
//        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
//        pairs.add ( new BasicNameValuePair ( "id", String.valueOf(dis.getId()) ) );
//        pairs.add ( new BasicNameValuePair ( "planId", String.valueOf(dis.getPlanId()) ) );
//        pairs.add ( new BasicNameValuePair ( "herbId", String.valueOf(dis.getHerbId()) ) );
//        pairs.add ( new BasicNameValuePair ( "herbName", String.valueOf(dis.getHerbName()) ) );
//        pairs.add ( new BasicNameValuePair ( "herbUnit", String.valueOf(dis.getHerbUnit()) ) );
//        pairs.add ( new BasicNameValuePair ( "quantity", String.valueOf(dis.getQuantity()) ) );
//        pairs.add ( new BasicNameValuePair ( "special", String.valueOf(dis.getSpecial()) ) );
//        pairs.add ( new BasicNameValuePair ( "valuables", String.valueOf(dis.getValuables()) ) );
//        pairs.add ( new BasicNameValuePair ( "userId", String.valueOf(dis.getUserId()) ) );
//        pairs.add ( new BasicNameValuePair ( "deptId", String.valueOf(dis.getDeptId()) ) );
//        pairs.add ( new BasicNameValuePair ( "beginTime", sdf.format(dis.getBeginTime()) ) );
//        pairs.add ( new BasicNameValuePair ( "endTime", sdf.format(dis.getEndTime()) ) );
//        pairs.add ( new BasicNameValuePair ( "status", String.valueOf(dis.getStatus()) ) );
//        pairs.add ( new BasicNameValuePair ( "shelf", String.valueOf(dis.getShelf()) ) );
//        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensingDetail/update.do", pairs, ReturnDomain.class );
//        if ( returnDomain.getSuccess () ) {
//        } else {
//            throw new Exception ( returnDomain.getException () );
//        }
//    }
}
