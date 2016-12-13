package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.domain.TagDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TagUtil
 *
 * @author RongGuang
 * @date 2015/12/7
 */
public class TagUtil {
    private ReturnDomain returnDomain;

    /**
     * 通过标签ID获得标签
     * @param tagId
     * @return
     * @throws Exception
     */
    public TagDomain getTagByTagId(String tagId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "tagId", tagId ) );
        returnDomain = ( ReturnDomain ) Http.post ( "tag/getTagByTagId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), TagDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    /**
     * 通过标签ID获得标签类型
     * @param tagId
     * @return
     * @throws Exception
     */
    public String getTagTypeByTagId(String tagId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "tagId", tagId ) );
        Map tagMap = ( Map ) ( ( ReturnDomain ) ( Http.post ( "tag/getTagByTagId.do", pairs, ReturnDomain.class ) ) ).getObject ();
        String tag = ( String ) tagMap.get ( "tagId" );
        String type=( String ) tagMap.get ( "type" );
        if ( tag != null && !"".equals(tag) ) {
            return type;
        } else {
           new Exception("无设备");
        }
        return null;
    }
}
