package org.basis.framework.excel.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2022/5/31 17:07
 **/
@ExcelTarget("DemoEntity")
@Data
public class DemoEntity implements Serializable {
    @Excel(name = "pdpComponents", width = 20, isWrap = false)
    private String pdpComponents;

    @Excel(name = "pdpModels", width = 20, isWrap = false)
    private StringBuilder pdpModels;

    @Excel(name = "template", width = 20, isWrap = false)
    private String template;

    /**
     * 演示用例
     */
    public void demo(){
        BaseExcelDataController dataController = new BaseExcelDataController();
        List<DemoEntity> demoEntities = new ArrayList<>();
        dataController.download("12412",demoEntities,DemoEntity.class,new ConverterDataHandler());
    }
}
