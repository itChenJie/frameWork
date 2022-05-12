package org.basis.framework.utils;

/**
 * @Description
 * 字符工具类
 * @Author ChenWenJie
 * @Data 2021/6/4 1:58 下午
 **/
public class CharUtils {

    /**
     * 首字母大写
     * @param chars
     * @return
     */
    public static String initialsUpperCase(char[] chars){
        StringBuilder str = new StringBuilder(String.valueOf(chars[0]).toUpperCase());
        for (int i=1;i<chars.length;i++){
            str.append(chars[i]);
        }
        return str.toString();
    }

    /**
     * 首字母小写
     * @param chars
     * @return
     */
    public static String initialsLowerCase(char[] chars){
        StringBuilder str = new StringBuilder(String.valueOf(chars[0]).toLowerCase());
        for (int i=1;i<chars.length;i++){
            str.append(chars[i]);
        }
        return str.toString();
    }
}
