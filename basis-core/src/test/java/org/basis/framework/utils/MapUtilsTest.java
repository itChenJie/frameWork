package org.basis.framework.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapUtilsTest {

    @Test
    public void findAndThen(){
        Map<String, String> map = new HashMap<>();
        map.put("11","AAA");
        map.put("21","AAA");

        MapUtils.findAndThen(map, "11", result->System.out.println("存在对应key 进行逻辑处理"));
    }
}
