
package org.basis.framework.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import lombok.SneakyThrows;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * @Description 汉字转拼音工具
 * @Author ChenWenJie
 * @Data 2020/11/21 3:23 下午
 **/
public class PinyinUtils {

    private PinyinUtils() {
    }

    /**
     * 获取字符串对应拼音的首字母,字符原样输出
     * @param pinyin 需要转换的字符串
     * @return
     * @throws
     */
    public static String getHeadString(String pinyin) throws BadHanyuPinyinOutputFormatCombination {
        StringBuffer pybf = new StringBuffer();
        char[] arr = pinyin.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], setHanyuPinyinOutputFormat());
                if (temp != null) {
                    pybf.append(temp[0].charAt(0));
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 设置汉语拼音输出格式
     * @return
     */
    private static HanyuPinyinOutputFormat setHanyuPinyinOutputFormat() {
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        return defaultFormat;
    }

    /**
     * 将字符串转换成相应格式的拼音
     * @param pinyin 需要转换的字符串
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getAll(String pinyin) throws PinyinException {
        return getAll(pinyin, "");
    }

    /**
     * 将字符串转换成相应格式的拼音
     * @param pinyin 需要转换的字符串
     * @param separator 拼音分隔符
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getAll(String pinyin,String separator) throws PinyinException {
        return com.github.stuxuhai.jpinyin.PinyinHelper.convertToPinyinString(pinyin, separator, PinyinFormat.WITHOUT_TONE);
    }

    /**
     * 获取字符串的拼音并且首字母大写
     * @param name
     * @return
     */
    @SneakyThrows
    public static String getPinYinHeaderChar(String name) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] chars = name.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                String[] pinStrs = PinyinHelper.toHanyuPinyinStringArray(c, outputFormat);
                char[] chars1 = pinStrs[0].toCharArray();
                String str = String.valueOf(chars1[0]).toUpperCase();
                for (int i=1;i<chars1.length;i++){
                    str=str+chars1[i];
                }
                builder.append(str);
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
    }



}
