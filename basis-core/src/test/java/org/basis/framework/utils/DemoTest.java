package org.basis.framework.utils;

import org.junit.Test;

import java.util.*;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2022/2/11 10:35 上午
 **/
public class DemoTest {
    
    @Test
    public void hasSet(){


        Map map = new HashMap();
        map.put("1",1);
        map.put("2",2);
        System.out.println(map.size());

        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("1");
    }

    @Test
    public void has(){
//        System.out.println((5 - 1) & 69);

        int n = 34 - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        System.out.println(n+1);
    }

    @Test
    public void arrayList(){
//        ArrayList<String> list = new ArrayList<>(5);
//        list.add("1");
//        list.add("2");
//        list.add("3");
//        list.add("4");
//        list.add("5");
//        list.add("6");
//        list.add("7");
//        list.add("8");
//        list.add("9");
//        list.add("10");
//        list.add("11");
//        Map<Object, Object> map = Collections.synchronizedMap(new HashMap<>());
        HashSet<Integer> set = new HashSet<>();
        set.add(null);

    }
}
