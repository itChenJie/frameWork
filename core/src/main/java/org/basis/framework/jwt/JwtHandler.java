package org.basis.framework.jwt;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Description
 * jwt登录token认证
 * @Author ChenWenJie
 * @Data 2021/8/27 5:56 下午
 **/
public abstract class JwtHandler {
    public static final String AUTHORIZATION = "Authorization";
    /**
     * @param request
     * @return null 代表没有
     */
    public String getCookieJWT(HttpServletRequest request) {
        Cookie[] cs = request.getCookies();
        if (null != cs && cs.length > 0) {
            Optional<Cookie> v = Stream.of(cs).filter((c) -> c.getName().equals(AUTHORIZATION)).findFirst();
            if (v.isPresent()) {
                Cookie cookie = v.get();
                return cookie.getValue();
            }
        }
        if (StringUtils.isNotBlank(request.getHeader(AUTHORIZATION))) {
            return request.getHeader(AUTHORIZATION);
        }
        return null;
    }

    /**
     * 设置CookieJWT
     * @param object
     * @param response
     */
    public abstract void setCookieJWT(Object object, HttpServletResponse response);

    /**
     *  String jwt = JwtUtil.getJWTString(userInfoVo);
     *         Cookie cookie = new Cookie(AUTHORIZATION, jwt);
     *         cookie.setPath("/");
     *         //过期时间为24小时
     *         cookie.setMaxAge(7 * 24 * 3600);
     *         response.setHeader(AUTHORIZATION, jwt);
     *         response.addCookie(cookie);
     */
}
