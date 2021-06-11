package com.baomidou.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 考勤人员打卡地点表
 * </p>
 *
 * @author ChenWenJie
 * @since 2021-06-09
 */
@TableName("72crm_admin_attendance_address")
@ApiModel(value="AdminAttendanceAddress对象", description="考勤人员打卡地点表")
public class AdminAttendanceAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer AddressId;

    @ApiModelProperty(value = "打卡地名称")
    private String AddressName;

    @ApiModelProperty(value = "打卡地信息")
    private String AddressInfo;

    @ApiModelProperty(value = "范围（米）")
    private Integer Scope;

    @ApiModelProperty(value = "经度")
    private String Lng;

    @ApiModelProperty(value = "纬度")
    private String Lat;

    @ApiModelProperty(value = "人员ID")
    private Long UserId;

    public Integer getAddressId() {
        return AddressId;
    }

    public void setAddressId(Integer AddressId) {
        this.AddressId = AddressId;
    }
    public String getAddressName() {
        return AddressName;
    }

    public void setAddressName(String AddressName) {
        this.AddressName = AddressName;
    }
    public String getAddressInfo() {
        return AddressInfo;
    }

    public void setAddressInfo(String AddressInfo) {
        this.AddressInfo = AddressInfo;
    }
    public Integer getScope() {
        return Scope;
    }

    public void setScope(Integer Scope) {
        this.Scope = Scope;
    }
    public String getLng() {
        return Lng;
    }

    public void setLng(String Lng) {
        this.Lng = Lng;
    }
    public String getLat() {
        return Lat;
    }

    public void setLat(String Lat) {
        this.Lat = Lat;
    }
    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long UserId) {
        this.UserId = UserId;
    }

    @Override
    public String toString() {
        return "AdminAttendanceAddress{" +
            "AddressId=" + AddressId +
            ", AddressName=" + AddressName +
            ", AddressInfo=" + AddressInfo +
            ", Scope=" + Scope +
            ", Lng=" + Lng +
            ", Lat=" + Lat +
            ", UserId=" + UserId +
        "}";
    }
}
