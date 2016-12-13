package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.EquipmentDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EquipmentUtil
 *
 * @author RongGuang
 * @date 2015/12/7
 */
public class EquipmentUtil {
    private ReturnDomain returnDomain;

    /**
     * 获得设备信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public EquipmentDomain getEquipment(String id) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", id ) );
        try {
            returnDomain = ( ReturnDomain ) ( Http.post ( "equipment/getEquipment.do", pairs, ReturnDomain.class ) );
            if ( returnDomain.getObject () != null ) {
                return JSON.parseObject ( returnDomain.getObject ().toString (), EquipmentDomain.class );

            } else {
                throw new Exception ( "ID不存在" );
            }
        } catch (Exception e) {
            throw new Exception ( e.getMessage () );
        }
    }

    /**
     * 获得设备信息
     * @param equipId
     * @return
     * @throws Exception
     */
    public EquipmentDomain getEquipByEquipId(String equipId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "equipId", equipId ) );
        try {
            returnDomain = ( ReturnDomain ) ( Http.post ( "equipment/getEquipmentByEquipId.do", pairs, ReturnDomain.class ) );
            if ( returnDomain.getSuccess () ) {
                if ( returnDomain.getObject () != null ) {
                    return JSON.parseObject ( returnDomain.getObject ().toString (), EquipmentDomain.class );
                } else {
                    throw new Exception ( "未查到设备信息" );
                }
            } else {
                throw new Exception ( returnDomain.getException () );
            }
        } catch (Exception e) {
            throw new Exception ( e.getMessage () );
        }
    }

    /**
     * 获得所有的设备
     *
     * @return
     */
    public List<EquipmentDomain> getAllEquip() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        returnDomain = ( ReturnDomain ) ( Http.post ( "equipment/getEquipmentOfAll.do", pairs, ReturnDomain.class ) );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), List.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }


    /**
     * 获得设备信息
     * @param tagId
     * @return
     * @throws Exception
     */
    public EquipmentDomain getEquipByTagId(String tagId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "tagId", tagId ) );
        try {
            returnDomain = ( ReturnDomain ) ( Http.post ( "equipment/getEquipmentByTagId.do", pairs, ReturnDomain.class ) );
            if ( returnDomain.getSuccess () ) {
                if ( returnDomain.getObject () != null ) {
                    return JSON.parseObject ( returnDomain.getObject ().toString (), EquipmentDomain.class );
                } else {
                    throw new Exception ( "未查到设备信息" );
                }
            } else {
                throw new Exception ( returnDomain.getException () );
            }
        } catch (Exception e) {
            throw new Exception ( e.getMessage () );
        }
    }

    /**
     *  获取设备
     * @param type
     * @return
     * @throws Exception
     */
    public List<EquipmentDomain> getEquipmentByType(String type) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "type", type ) );
        try {
            returnDomain = ( ReturnDomain ) ( Http.post ( "equipment/getEquipmentByType.do", pairs, ReturnDomain.class ) );
            if ( returnDomain.getSuccess () ) {
                if ( returnDomain.getObject () != null ) {
                    return JSON.parseObject ( returnDomain.getObject ().toString (), List.class );
                } else {
                    throw new Exception ( "未查到设备信息" );
                }
            } else {
                throw new Exception ( returnDomain.getException () );
            }
        } catch (Exception e) {
            throw new Exception ( e.getMessage () );
        }
    }
    /**
     * 获取设备
     * @param planId
     * @return
     * @throws Exception
     */
    public EquipmentDomain getEquipByPlanId(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) Http.post ( "equipment/getEquipmentByPlanId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), EquipmentDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    /**
     * 添加设备
     *
     * @param equipmentDomain
     * @return
     * @throws Exception
     */
    public Integer insertEquipment(EquipmentDomain equipmentDomain) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        if ( equipmentDomain.getId () != null )
            pairs.add ( new BasicNameValuePair ( "id", String.valueOf ( equipmentDomain.getId () ) ) );
        pairs.add ( new BasicNameValuePair ( "equipId", String.valueOf ( equipmentDomain.getEquipId () ) ) );
        pairs.add ( new BasicNameValuePair ( "tagId", String.valueOf ( equipmentDomain.getTagId () ) ) );
        pairs.add ( new BasicNameValuePair ( "equipName", String.valueOf ( equipmentDomain.getEquipName () ) ) );
        pairs.add ( new BasicNameValuePair ( "equipType", String.valueOf ( equipmentDomain.getEquipType () ) ) );
        pairs.add ( new BasicNameValuePair ( "equipType1", String.valueOf ( equipmentDomain.getEquipType1 () ) ) );
        if ( !"".equals ( String.valueOf ( equipmentDomain.getEquipPurpose () ) ) ) {
            System.out.println ( "$$>>" );
            pairs.add ( new BasicNameValuePair ( "equipPurpose", String.valueOf ( equipmentDomain.getEquipPurpose () ) ) );
        }
        pairs.add ( new BasicNameValuePair ( "equipStatus", String.valueOf ( equipmentDomain.getEquipStatus () ) ) );
        System.out.println ( "$>>$$" + String.valueOf ( equipmentDomain.getEquipPurpose () ) );
        try {
            return ( Integer ) ( ( Map ) Http.post ( null == equipmentDomain.getId () ? "equipment/insert.do" : "equipment/update.do", pairs, Map.class ) ).get ( "id" );
        } catch (Exception e) {
            throw new Exception ( e.getMessage () );
        }
    }
}
