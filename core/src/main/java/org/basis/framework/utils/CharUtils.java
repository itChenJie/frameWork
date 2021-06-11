package org.basis.framework.utils;

/**
 * @Description
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
        String str = String.valueOf(chars[0]).toUpperCase();
        for (int i=1;i<chars.length;i++){
            str=str+chars[i];
        }
        return str;
    }
}
