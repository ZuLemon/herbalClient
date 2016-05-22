package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.com.AppOption;
import net.andy.dispensing.domain.StationDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * StationUtil
 *
 * @author RongGuang
 * @date 2016/03/08
 */
public class StationUtil {
    private ReturnDomain returnDomain;

    /**
     * 获取所有工位
     */
    public List getStationAll() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        returnDomain = (ReturnDomain) new Http().post("station/getStationAll.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }

    /**
     * 通过device获取工位
     */
    public StationDomain getStationByDevice() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("device", new AppOption().getOption(AppOption.APP_DEVICE_ID)));
        returnDomain = (ReturnDomain) new Http().post("station/getStationByDevice.do", pairs, ReturnDomain.class);
        StationDomain domain = new StationDomain();
        if (returnDomain.getSuccess()) {
            try {
                domain = JSON.parseObject(returnDomain.getObject().toString(), StationDomain.class);
            } catch (Exception e) {
                e.printStackTrace();
                domain.setName("");
            }
        } else {
            throw new Exception(returnDomain.getException());
        }
        return domain;
    }

    /**
     * 更新工位
     *
     * @device 设备ID
     */
    public List updateStationByDevice(String device) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("device", device));
        returnDomain = (ReturnDomain) new Http().post("station/getStationAll.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject(returnDomain.getObject().toString(), List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }

    /**
     * 更新工位
     */
    public Object updateStation(StationDomain stationDomain, Integer arg) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (arg != 2) {
            pairs.add(new BasicNameValuePair("id", String.valueOf(stationDomain.getId())));
        }
        pairs.add(new BasicNameValuePair("name", stationDomain.getName()));
        pairs.add(new BasicNameValuePair("device", stationDomain.getDevice()));
        pairs.add(new BasicNameValuePair("rulesId", String.valueOf(stationDomain.getRulesId())));
        pairs.add(new BasicNameValuePair("shelfId", String.valueOf(stationDomain.getShelfId())));
        pairs.add(new BasicNameValuePair("deptId", String.valueOf(stationDomain.getDeptId())));

        if (arg == 0) {
            returnDomain = (ReturnDomain) new Http().post("station/update.do", pairs, ReturnDomain.class);
        } else if (arg == 1) {
            returnDomain = (ReturnDomain) new Http().post("station/updateDevice.do", pairs, ReturnDomain.class);
        } else if (arg == 2) {
            returnDomain = (ReturnDomain) new Http().post("station/insert.do", pairs, ReturnDomain.class);
        }
        if (returnDomain.getSuccess()) {
            return "修改成功";
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
}
