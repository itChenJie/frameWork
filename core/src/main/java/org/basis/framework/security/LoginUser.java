package org.basis.framework.security;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/8/6 6:10 下午
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements Principal {
    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 账号
     */
    private String account;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 称号
     */
    private String title;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 签名
     */
    private String signature;
    /**
     * 组名
     */
    private String group;
    /**
     * 电话
     */
    private String phone;

    /**
     * 权限
     */
    private List<String> authoritys;
    /**
     *
     */
    private Map<String, Object> data;

    @JsonAnySetter
    public void set(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
    }

    public <T> T get(String key) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        return (T) this.data.get(key);
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }
}
