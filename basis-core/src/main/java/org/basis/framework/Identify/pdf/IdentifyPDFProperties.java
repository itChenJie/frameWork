package org.basis.framework.Identify.pdf;

import lombok.Data;
import lombok.ToString;

import java.util.List;


/**
 * @Description pdf识别 规则配置
 * @Author ChenJie
 **/
@Data
@ToString
public class IdentifyPDFProperties {
    private List<IdentifyRegexConfig> regex;
}
