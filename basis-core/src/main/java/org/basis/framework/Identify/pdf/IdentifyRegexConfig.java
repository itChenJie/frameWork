package org.basis.framework.Identify.pdf;

import java.util.List;

/**
 * @Description pdf识别 规则
 * @Author ChenJie
 **/
public class IdentifyRegexConfig {
    /**
     * 字段
     */
    private String file;
    /**
     * 规则
     */
    private List<RegexConfig> configs;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<RegexConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<RegexConfig> configs) {
        this.configs = configs;
    }
}
