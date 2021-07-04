package org.basis.framework.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

public class PinyinUtilsTest {

    @Test
    public void getShort() throws BadHanyuPinyinOutputFormatCombination {
       // System.out.println(PinyinUtils.getShort("你好啊AA"));
    }

    @Test
    public void getAll() throws PinyinException {
        System.out.println(PinyinUtils.getAll("你你你你","-"));
    }

    @Test
    public void getPinYinHeaderChar() {
        System.out.println(PinyinUtils.getPinYinHeaderChar("你好啊小伙子"));
    }
}