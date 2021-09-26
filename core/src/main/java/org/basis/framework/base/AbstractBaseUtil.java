package org.basis.framework.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import org.basis.framework.security.LoginUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/8/6 6:04 下午
 **/
public abstract class AbstractBaseUtil {
    private static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    private static final String USER_ADMIN_TOKEN = "USER_ADMIN_TOKEN";

    private static final String USER_MOBILE_TOKEN = "USER_MOBILE_TOKEN";

    /**
     * 获取当前是否是windows系统
     * @return true代表为真
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 签名数据
     *
     * @param key  key
     * @param salt 盐
     * @return 加密后的字符串
     */
    public static String sign(String key, String salt) {
        return SecureUtil.md5(key.concat("erp").concat(salt));
    }

    /**
     * 验证签名是否正确
     *
     * @param key  key
     * @param salt 盐
     * @param sign 签名
     * @return 是否正确 true为正确
     */
    public static boolean verify(String key, String salt, String sign) {
        return sign.equals(sign(key, salt));
    }

    /**
     * 获取当前年月的字符串
     *
     * @return yyyyMMdd
     */
    public static String getDate() {
        return DateUtil.format(new Date(), "yyyyMMdd");
    }

    //FIXME 如果获取的地址不正确,直接返回一个固定地址也可以
    public static String getIpAddress() {
        return "https://crm.food-chain.com/";

    }

    public static LoginUser getUser() {
        return threadLocal.get();
    }

    protected static void setUser(LoginUser loginUser){ threadLocal.set(loginUser); }

    public static Long getUserId(){
        return getUser() ==null ? null: getUser().getUid();
    }

    public static void removeThreadLocal(){
        threadLocal.remove();
    }

    public static String getToken(HttpServletRequest request){
        return request.getHeader("Admin-Token") != null ? request.getHeader("Admin-Token") : "";
    }

    public static boolean isAdmin(){ return getUserId().equals(null); }

    public static String getIp() {
        return getUser().get("ip");
    }
}
