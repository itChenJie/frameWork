package org.basis.framework.Identify.pdf;

import lombok.Data;
import lombok.ToString;

import java.util.List;


/**
 * @Description pdf识别 规则配置
 * @Author ChenWenJie
 * @Data 2023/04/28 2:55 下午
 **/
@Data
@ToString
public class IdentifyPDFProperties {
    private List<IdentifyRegexConfig> regex;
}
