package org.basis.framework.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 字符串工具类
 * @Author ChenWenJie
 * @Data 2020/11/21 3:23 下午
 **/
public class StringUtil {

    private static final Log log = LogFactory.getLog(StringUtil.class);

    private static final String[] EMPTY_ARRAY = new String[0];

    private static final String NONCE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final char[] LOWERCASES = {'\000', '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', ''};

    private static final char[] CHAR_MAP = new char[64];

    public static final String EMPTY = "";

    private StringUtil(){
    }

    static {
        for (int i = 0; i < 10; i++) {
            CHAR_MAP[i] = (char) ('0' + i);
        }
        for (int i = 10; i < 36; i++) {
            CHAR_MAP[i] = (char) ('a' + i - 10);
        }
        for (int i = 36; i < 62; i++) {
            CHAR_MAP[i] = (char) ('A' + i - 36);
        }
        CHAR_MAP[62] = '_';
        CHAR_MAP[63] = '-';
    }

    /**
     * 判断字符串是否包含中文字符串
     * @param str
     * @return
     */
    public static boolean isChinese(String str){
        if (isEmpty(str)){
            throw new NullPointerException("str is null");
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 字符串为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return (str == null) || str.length() == 0;
    }

    /**
     * 字符串不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     *  判断字符串长度,非单字节字符串算两个长度
     * @param str
     * @return
     */
    public static int length(String str){return str.replaceAll("[^\\x00-\\xff]", "rr").length();}

    public static boolean isNotBlank(String str){return !isBlank(str);}

    public static boolean isBlank(String str) {
        return (str == null) || (nullValue(str).trim().length() == 0);
    }
    /**
     * 判断对象是否为空，如果为空返回空字符，如果不为空，返回该字符串的toString字符串
     *
     * @param s 要判断的对象
     * @return {String}
     */
    public static String nullValue(Object s) {
        return s == null ? "" : s.toString();
    }
    /**
     * 剔除字符串中的空字符
     * @param str
     * @return
     */
    public static String trim(String str){
        return str == null ? "" : str.trim();
    }

    /**
     * 截取指定位置的字符串
     * @param str
     * @param start
     * @return
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        if (start < 0){
            start = str.length() + start;
        }

        if (start > str.length()) {
            return EMPTY;
        }

        return str.substring(start);
    }

    /**
     * 截取从某个下标到某个下标的字符串
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String substring(String str, int start, int end){
        if (str == null){
            return null;
        }

        if (end < 0) {
            end = str.length() + end;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    /**
     * 以{delim}格式截取字符串返回list,不返回为空的字符串
     * @param str
     * @param delim
     * @return
     */
    public static List<String> split(String str, String delim){
        List<String> list = new ArrayList<>();
        str = trim(str);
        String[] split = str.split(delim);
        for (String s : split) {
            if (s.trim().length()>0){
                list.add(s);
            }
        }
        return list;
    }

    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 字符串匹配
     * @param str
     * @param regex
     * @return
     */
    public static boolean matching(String str,String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    public static String urlEncode(String url){
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

