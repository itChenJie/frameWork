/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package org.basis.framework.jwt;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.basis.framework.json.JSONHelper;

import java.util.Date;


/**
 * @Description Jwt工具类
 * @Author ChenWenJie
 * @Data 2021/8/27 4:48 下午
 **/
@Slf4j
public class JwtUtil {
    /**
     * 密钥
     */
    private static String SECRET = "MY SECRET";

    /**
     * token失效时间改为1周，若用户一周未登录app需要重新登录
     */
    private static final Long OUTTIME = 7 * 24 * 60 * 60 * 1000L;


    /**
     * @param info
     * @return
     */
    public static String getJWTString(Class<T> info,String id) {
        if (info == null) {
            throw new NullPointerException("null id is illegal");
        }
        String params = JSONHelper.obj2JSONString(info);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setHeaderParam("alg", "HS256")
                .setIssuer("Jersey-Security-Basic")
                .setAudience("user")
                .setExpiration(new Date(System.currentTimeMillis() + OUTTIME))
                .setClaims(JSONHelper.JsonToObMap(JSONObject.parseObject(params)))
                .setIssuedAt(new Date())
                .setId(id)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    private JwtUtil() {
    }

    /**
     * @param jwt
     * @return id：用户主键 如果为null则token失效  跳转登录页面
     */
    public static <T> T parseJwt2Id(String jwt,Class<T> clazz) {
        try {
            //compactJws为jwt字符串
            if (StringUtils.isBlank(jwt)) {
                return null;
            }
            Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt);
            //得到body后我们可以从body中获取我们需要的信息
            return JSONHelper.jsonToObject(JSONHelper.obj2JSONString(parseClaimsJws.getBody()), clazz);
        } catch (SignatureException | MalformedJwtException e) {
            // jwt 解析错误
            log.warn("Jwt:{} 解析错误");
            return null;
        } catch (ExpiredJwtException e) {
            // jwt 已经过期，在设置jwt的时候如果设置了过期时间，这里会自动判断jwt是否已经过期，如果过期则会抛出这个异常，我们可以抓住这个异常并作相关处理。
            log.warn("Jwt:{} 已过期");
            return null;
        }
        //比如 获取主题,当然，这是我们在生成jwt字符串的时候就已经存进来的
//        String subject = body.getSubject();
    }

}
