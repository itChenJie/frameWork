package org.basis.framework.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Description
 * 百度客户端工具类
 * @Author ChenWenJie
 * @Data 2021/10/18 11:31 上午
 **/
public class BaiduClientUtil {
    private static final Logger log = LoggerFactory.getLogger(BaiduClientUtil.class);
    /**
     * 百度开发者访问key
     */
    private static final String baiduUrl = "http://api.map.baidu.com";

    /**
     * 地理编码
     * @param address
     * @param ak
     * @return
     */
    public static JSONObject geocoding(String address ,String ak) {
        return geocoding((baiduUrl + "/geocoding/v3/?address=%s&output=json&ak=%s&callback=showLocation"), ak, address);
    }

    /**
     * 地理编码
     * @param ak
     * @param address
     * @return
     */
    public static JSONObject geocoding(String url ,String ak,String address) {
        checkParams(url, ak);
        url = String.format(url, URLEncoder.encode(address.replace(" ","")), ak);
        String responseStr = getRequestBaiDu(url);
        JSONObject object = JSON.parseObject(responseStr.replace("showLocation&&showLocation(", "").replace(")", ""));
        if (object.getJSONObject("result") == null) {
            return null;
        }
        return object.getJSONObject("result").getJSONObject("location");
    }

    /**
     * 批量反查经纬度对应地址信息，一次最多20条
     * @param addresss
     * @param url "/geocoding/v3/?address=%s&ak=%s&output=json"
     * @return
     */
    public static JSONArray batchGeocoding(List<String> addresss,String url,String ak){
        checkParams(url, ak);
        List<Map> maps = new ArrayList<>();
        for (String address : addresss) {
            Map map = new HashMap();
            map.put("method","get");
            map.put("url",String.format(url,URLEncoder.encode(address.replace(" ","")),ak));
            maps.add(map);
        }
        Map map = new HashMap();
        map.put("reqs",maps);
        String responseStr = postRequestBaiDu(baiduUrl+"/batch",JSON.toJSONString(map));
        JSONObject object = JSON.parseObject(responseStr);
        if (object.get("status").equals(0)) {
            return object.getJSONArray("batch_result");
        }
        return null;
    }

    public static JSONObject reverseGeocoding(String lat,String lng,String ak){
        return reverseGeocoding((baiduUrl + "/reverse_geocoding/v3/?output=json&location=%s&ak=%s&extensions_town=true"), ak, lat, lng);
    }
    /**
     * 根据经纬度反查地址
     * @param lat
     * @param lng
     * @return
     */
    public static JSONObject reverseGeocoding(String url ,String ak,String lat,String lng){
        checkParams(url,ak);
        url = String.format(url,(lat+","+lng), ak);
        String responseStr = getRequestBaiDu(url);
        JSONObject object = JSON.parseObject(responseStr);
        if (object.getJSONObject("result")!=null) {
            if (object.getJSONObject("result").getJSONObject("addressComponent")!=null) {
                JSONObject address = object.getJSONObject("result").getJSONObject("addressComponent");
                return address;
            }
        }
        return null;
    }

    private static String getRequestBaiDu(String url){
        String responseStr = HttpUtil.get(url,StandardCharsets.UTF_8);
        JSONObject object = JSON.parseObject(responseStr);
        int index =0;
        while (object==null&&index<10){
            responseStr = HttpUtil.get(url,StandardCharsets.UTF_8);
            object = JSON.parseObject(responseStr);
            ++index;
            log.error("baidu api error retry index：{}",index);
        }
        return responseStr;
    }

    private static String postRequestBaiDu(String url, String body){
        String responseStr = HttpUtil.post(url,body);
        JSONObject object = JSON.parseObject(responseStr);
        int index =0;
        while (object==null&&index<10){
            responseStr = HttpUtil.post(url,body);
            object = JSON.parseObject(responseStr);
            ++index;
            log.error("baidu api error retry index：{}",index);
        }
        return responseStr;
    }

    /**
     * 参数校验
     * @param url api
     * @param ak
     * @return
     */
    private static void checkParams(String url, String ak){
        if (StringUtils.isBlank(url)){
            throw new IllegalArgumentException(" baiau api request url is not null !");
        }
        if (StringUtils.isBlank(ak)){
            throw new IllegalArgumentException(" baiau api request ak is not null !");
        }
    }
}
