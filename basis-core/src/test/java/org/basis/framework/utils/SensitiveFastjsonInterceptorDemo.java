package org.basis.framework.utils;

import com.alibaba.fastjson.annotation.JSONType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.basis.framework.encryption.desensitization.annotation.SensitiveField;
import org.basis.framework.encryption.desensitization.constant.SensitiveTypeEnum;
import org.basis.framework.encryption.desensitization.interceptor.SensitiveFastjsonInterceptor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode
@ApiModel(description = "订单明细毛利率报表查询返回参数类")
@JSONType(serializer = SensitiveFastjsonInterceptor.class)
public class SensitiveFastjsonInterceptorDemo {
    @ApiModelProperty(value = "订单ID")
    private Integer id;

    @ApiModelProperty( value = "投保人名称")
    private String holderName;

    @ApiModelProperty( value = "投保人证件号码")
    @SensitiveField(type = SensitiveTypeEnum.ID_CARD_NUM)
    private String holderIdentifyNumber;

    @ApiModelProperty( value = "投保人手机号")
    @SensitiveField(type = SensitiveTypeEnum.PHONE_NUM)
    private String holderPhone;

    @ApiModelProperty( value = "车主")
    private String carOwner;

    @ApiModelProperty(value = "车主类型 1： 个人客户   2.企业。团体客户")
    private Integer carOwnerType;

    @ApiModelProperty(value = "车主证件号码")
    @SensitiveField(type = SensitiveTypeEnum.ID_CARD_NUM)
    private String carOwnerIdentifyNumber;

    @ApiModelProperty(value = "车主手机号")
    @SensitiveField(type = SensitiveTypeEnum.PHONE_NUM)
    private String carOwnerPhone;

    @ApiModelProperty( value = "被保人")
    private String insuredName;

    @ApiModelProperty(value = "被保人证件号码")
    @SensitiveField(type = SensitiveTypeEnum.ID_CARD_NUM)
    private String insuredIdentifyNumber;

    @ApiModelProperty(value = "被保人手机号")
    @SensitiveField(type = SensitiveTypeEnum.PHONE_NUM)
    private String insuredPhone;
}