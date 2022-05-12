package org.basis.framework.utils;


import org.junit.Test;

public class SnowFlakeUtilsTest {

    @Test
    public void autoCreateUserCouponCode() {
//        // 1.雪花算法
//        Long snowFlakeId = SnowFlakeUtils.autoId();
        // 2.编码 GQ + 活动类型 + 券ID + 雪花算法
        //System.out.println("code："+StringUtils.join("GQ", 0, 123456, snowFlakeId));
    }

    @Test
    public void autoCreateUserCouponForCode() {
//       // SnowFlakeUtils snowFlake = new SnowFlakeUtils(SnowFlakeUtils.getWorkId(), SnowFlakeUtils.getDataCenterId());
//        // 1.雪花算法
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(snowFlake.nextId());
//        }
    }
}