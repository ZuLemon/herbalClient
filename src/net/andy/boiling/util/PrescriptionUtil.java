package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.PrescriptionDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.AppOption;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PrescriptionUtil
 *
 * @author RongGuang
 * @date 2015/12/9
 */
public class PrescriptionUtil {
    private ReturnDomain returnDomain;

    public PrescriptionDomain getPrescription(String id) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", id ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/getPrescription.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), PrescriptionDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public PrescriptionDomain getPrescriptionByPlanId(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/getPrescriptionByPlanId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), PrescriptionDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public PrescriptionDomain getPrescriptionByPresId(String presId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "presId", presId ) );

        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/getPrescriptionByPresId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), PrescriptionDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public Map insertDispensingByDevice(String device, String userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "device", device ) );
        pairs.add ( new BasicNameValuePair ( "userId", userId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/insertDispensingByDevice.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            if(returnDomain.getObject()!=null)
            return JSON.parseObject ( returnDomain.getObject ().toString (), Map.class );
            else return null;
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public Map cancelPauseByTagId(String tagId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "tagId", tagId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/cancelPauseByTagId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            if(returnDomain.getObject()!=null)
                return JSON.parseObject ( returnDomain.getObject ().toString (), Map.class );
            else return null;
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public String pause(Integer disId,String tagId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "disId", String.valueOf(disId) ) );
        pairs.add ( new BasicNameValuePair ( "tagId", String.valueOf(tagId) ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/pause.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
                return "暂停成功！";
        } else {
//            if(returnDomain.getException ()==null){
//                return "暂停失败 !";
//            }
            throw new Exception ( "暂停失败:"+returnDomain.getException () );
        }
    }
    public Map getPauseMap(String tagId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        pairs.add ( new BasicNameValuePair ( "tagId", String.valueOf(tagId) ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/getPauseList.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), Map.class );
        } else {
            throw new Exception ( "取暂停列表错误:"+returnDomain.getException () );
        }
    }

    /**
     * 修改处方自煎、代煎
     * @param id
     * @param process
     * @return
     * @throws Exception
     */
    public String setProcess(Integer id,String process) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", String.valueOf(id)));
        pairs.add ( new BasicNameValuePair ( "process", process) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/setProcess.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "修改成功";
        } else {
            throw new Exception (returnDomain.getException () );
        }
    }

    /**
     * 修改处方状态
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    public String setMain(Integer id,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", String.valueOf(id)));
        pairs.add ( new BasicNameValuePair ( "status", status) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/setMain.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "修改成功";
        } else {
            throw new Exception (returnDomain.getException () );
        }
    }
    /**
     * 读条码取处方
     */
    public Map insertDispensingByDevice(String device, String userId,String barcode) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "device", device ) );
        pairs.add ( new BasicNameValuePair ( "userId", userId ) );
        pairs.add ( new BasicNameValuePair ( "barcode", barcode ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "prescription/insertDispensingByDevice.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            if(returnDomain.getObject()!=null)
                return JSON.parseObject ( returnDomain.getObject ().toString (), Map.class );
            else return null;
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    /**
     * 测试
     * @return
     * @throws Exception
     */
//    public Map test() throws Exception {
//        returnDomain = ( ReturnDomain ) new Http ().get ( "mock.html", ReturnDomain.class );
//        if ( returnDomain.getSuccess () ) {
//            if(returnDomain.getObject()!=null)
//                return JSON.parseObject ( returnDomain.getObject ().toString (), Map.class );
//            else return null;
//        } else {
//            throw new Exception ( returnDomain.getException () );
//        }
//    }
}
