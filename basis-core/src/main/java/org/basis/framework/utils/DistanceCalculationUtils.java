package org.basis.framework.utils;

/**
 * @Description 距离计算工具
 * @Author ChenWenJie
 * @Data 2021/6/11 2:52 下午
 **/
public class DistanceCalculationUtils {
    /**
     * 补充：计算两点之间真实距离
     * @return 米
     */
    public static Long getDistance(String longitude1, String latitude1, String longitude2, String latitude2) {
        // 维度
        double lat1 = (Math.PI / 180) * Double.valueOf(latitude1);
        double lat2 = (Math.PI / 180) * Double.valueOf(latitude2);
        // 经度
        double lon1 = (Math.PI / 180) * Double.valueOf(longitude1);
        double lon2 = (Math.PI / 180) * Double.valueOf(longitude2);
        // 地球半径
        double R = 6371;
        // 两点间距离 km
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return new Double(d*1000).longValue();
    }
}
