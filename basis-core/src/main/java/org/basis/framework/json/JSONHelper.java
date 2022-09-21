package org.basis.framework.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
/**
 * @Description JSON格式处理类
 * 将List数据集转成JSON字符串
 * @Author ChenWenJie
 * @Data 2021/7/4 5:03 下午
 **/
public class JSONHelper {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JSONHelper.class);

    /**
     * Object对象转JSON字符串
     * @param obj
     * @return
     */
    public static String obj2JSONString(Object obj) {
        if (obj == null) {
            return "";
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 数组/列表对象转JSON字符串
     * @param o
     * @return
     */
    public static String obj2JSONArray(Object o) {
        if (o == null)
            return "[]";
        return JSONArray.toJSONString(o);
    }

    /**
     * 把JSON格式的字符串转换成对象
     *
     * @param json 字符串
     * @return Object
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            LOGGER.error("转换失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转List<String>
     * @param jsonList
     * @return
     */
    public static List<String> str2JsonList(String jsonList) {
        if (StringUtils.isBlank(jsonList))
            return new ArrayList<String>();
        if (StringUtils.equalsIgnoreCase(jsonList, "[]"))
            return new ArrayList<String>();

        List<String> list = null;
        try {
            list = JSONArray.parseArray(jsonList, String.class);
        } catch (Exception e) {
            list = new ArrayList<String>();
        }
        return list;
    }

    /**
     * JSON字符串转List<Integer>
     * @param jsonList
     * @return
     */
    public static List<Integer> str2JsonList2(String jsonList) {
        if (StringUtils.isBlank(jsonList))
            return new ArrayList<Integer>();
        if (StringUtils.equalsIgnoreCase(jsonList, "[]"))
            return new ArrayList<Integer>();

        List<Integer> list = null;
        try {
            list = JSONArray.parseArray(jsonList, Integer.class);
        } catch (Exception e) {
            list = new ArrayList<Integer>();
        }
        return list;
    }

    /**
     * 把JSON格式的字符串转换成集合
     *
     * @param json  字符串
     * @param clazz 集合中存放的对象的class
     * @return List
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        List<T> list = null;
        try {
            list = JSONArray.parseArray(json, clazz);
        } catch (Exception e) {
            LOGGER.error("转换失败", e);
            list = null;
        }
        if (list == null) {
            try {
                list = JSONObject.parseArray(json, clazz);
            } catch (Exception e) {
                LOGGER.error("转换失败", e);
                list = null;
            }
        }
        return list;
    }

    /**
     *  (解析第一层JSONObject中的key和对应的VALUE ， 并把对应的key和value放到Map中 ， 并返回Map对象)
     * @param jsonObject
     * @return
     */
    public static Map<String, String> JsonToMap(JSONObject jsonObject) {
        Map<String, String> result = new HashMap<String, String>();
        Set<String> iterator = jsonObject.keySet();
        String value = null;
        for (String key : iterator) {
            value = jsonObject.getString(key);
            result.put(key, value);
        }
        return result;
    }

    /**
     * (解析第一层JSONObject中的key和对应的VALUE ， 并把对应的key和value放到Map中 ， 并返回Map对象)
     * @param jsonObject
     * @return
     */
    public static TreeMap<String, String> JsonToTreeMap(JSONObject jsonObject) {
        TreeMap<String, String> result = new TreeMap<String, String>();
        Set<String> iterator = jsonObject.keySet();
        String value = null;
        for (String key : iterator) {
            value = jsonObject.getString(key);
            result.put(key, value);
        }
        return result;
    }

    /**
     * (解析第一层JSONObject中的key和对应的VALUE ， 并把对应的key和value放到Map中 ， 并返回Map对象)
     * @param jsonObject
     * @return
     */
    public static Map<String, Object> JsonToObMap(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<String, Object>();
        Set<String> iterator = jsonObject.keySet();
        Object value = null;
        for (String key : iterator) {
            value = jsonObject.get(key);
            result.put(key, value);
        }
        return result;
    }

    public static void main(String[] args) {
        String jsonStr = null;
        if (StringUtils.isBlank(jsonStr)) {
            System.out.println("yes");
        }

    }
}
