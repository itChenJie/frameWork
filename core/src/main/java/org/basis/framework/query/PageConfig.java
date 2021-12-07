package org.basis.framework.query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PageConfig {
    public static Integer maxPageSize;

    public PageConfig() {
    }

    @Value("${config.max-page-size:200}")
    public void setMaxPageSize(Integer maxPageSize) {
        PageConfig.maxPageSize = maxPageSize;
    }
}
