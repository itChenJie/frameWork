package org.basis.framework.excel.alibabawrite;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class SimpleXbChannelOrderImportDataWriteHandler  implements SheetWriteHandler {
    private final Collection<String> cityNames;

    private final Collection<String> companyNames;

    private final Collection<String> insBranchs;

    public SimpleXbChannelOrderImportDataWriteHandler(Collection<String> cityNames, Collection<String> companyNames,Collection<String> insBranchs) {
        this.cityNames = cityNames;
        this.companyNames = companyNames;
        this.insBranchs = insBranchs;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Integer sheetNo = writeSheetHolder.getSheetNo();
        if (!Objects.equals(sheetNo, 0)) {
            //只涉及第一个Sheet
            return;
        }else {
            createHiddenSheet(writeWorkbookHolder);
        }
        Sheet sheet = writeSheetHolder.getSheet();

        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint cityConstraint = range(helper,cityNames);
        DataValidationConstraint companyConstraint = range(helper,companyNames);
        DataValidationConstraint insBranchConstraint = formulaRange(helper, "HiddenLists!$A$1:$A$");
        writeSheetHolder.getExcelWriteHeadProperty().getHeadMap().forEach((col, head) -> {
            String headName = head.getHeadNameList().get(0);
            if (StrUtil.contains(headName, "投保城市")) {
                DataValidation cityValidation = dataValid(2, 5000, col, col, helper, cityConstraint);
                sheet.addValidationData(cityValidation);
            }
            if (StrUtil.contains(headName, "保险公司")) {
                DataValidation companyValidation = dataValid(2, 5000, col, col, helper, companyConstraint);
                sheet.addValidationData(companyValidation);
            }
            if (StrUtil.contains(headName, "保险机构")) {
                DataValidation companyValidation = dataValid(2, 5000, col, col, helper, insBranchConstraint);
                sheet.addValidationData(companyValidation);
            }
        });
    }

    private DataValidationConstraint range(DataValidationHelper helper,Collection<String> val) {
        String[] vals = Optional.ofNullable(val)
                .map(Collection::stream).orElse(Stream.empty())
                .toArray(String[]::new);
        return helper.createExplicitListConstraint(vals);
    }

    /**
     * 创建隐藏的 Sheet，用于存储大量下拉项
     */
    private void createHiddenSheet(WriteWorkbookHolder workbookHolder) {
        Sheet hiddenSheet = workbookHolder.getWorkbook().createSheet("HiddenLists");
        int rowNum = 0;
        for (String branch : insBranchs) {
            hiddenSheet.createRow(rowNum++).createCell(0).setCellValue(branch);
        }
        // 设置为隐藏 Sheet
        workbookHolder.getWorkbook().setSheetHidden(workbookHolder.getWorkbook().getSheetIndex("HiddenLists"), true);
    }

    /**
     * 创建基于公式的约束（适用于大数据量）
     */
    private DataValidationConstraint formulaRange(DataValidationHelper helper, String cellRange) {
        return helper.createFormulaListConstraint(cellRange + insBranchs.size());
    }

    private DataValidation dataValid(int firstRow, int lastRow, int firstCol, int lastCol, DataValidationHelper helper, DataValidationConstraint constraint) {
        CellRangeAddressList rangeList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DataValidation validation = helper.createValidation(constraint, rangeList);

        // 阻止输入非下拉选项的值
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("提示", "请输入下拉选项中的内容");

        return validation;
    }
}
