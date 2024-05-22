package org.basis.framework.Identify.pdf;

import java.util.List;


public class RegexConfig {
    /**
     * 个性城市
     */
    private String city;
    /**
     * 正则
     */
    private String regex;
    /**
     * 子级 正则
     */
    private List<String> childRegex;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public List<String> getChildRegex() {
        return childRegex;
    }

    public void setChildRegex(List<String> childRegex) {
        this.childRegex = childRegex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
