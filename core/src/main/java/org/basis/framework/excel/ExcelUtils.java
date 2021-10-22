package org.basis.framework.excel;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.basis.framework.error.IgnoreException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Description Excel工具类
 * @Author ChenWenJie
 * @Data 2020/9/24 9:39 上午
 **/
public class ExcelUtils {

    /**
     * 生成Excel
     * @param head 报表头
     * @param title 列名 Map.put("列名称"，"默认值{数组用,分割}")
     * @param datas 数据
     * @return
     */
    public static HSSFWorkbook createExcel(String head,Map<String,String> title,List<List<Object>> datas){
        HSSFWorkbook wb = new HSSFWorkbook();
        return createExcel(head, title, datas, wb, headCellStyle(wb),  titleCellStyle(wb),  dataCellStyle(wb));
    }
    /**
     * 生成Excel
     * @param head 报表头
     * @param title 列名 Map.put("列名称"，"默认值{数组用,分割}")
     * @param datas 数据
     * @param wb
     * @param headCellStyle 头样式
     * @param titleCellStyle 标题样式
     * @param dataCellStyle  数据列表样式
     * @return
     */
    public static HSSFWorkbook createExcel(String head,Map<String,String> title,List<List<Object>> datas
            ,HSSFWorkbook wb,HSSFCellStyle headCellStyle,HSSFCellStyle titleCellStyle
            ,HSSFCellStyle dataCellStyle) {

        HSSFSheet sheet = wb.createSheet();
        int rownum =0;
        // 创建报表头部
        if (StringUtils.isNotBlank(head)){
            createNormalHead(head, rownum,title.size()-1, sheet,headCellStyle);
            ++rownum;
        }
        // 创建报表列
        Map enumMap = new HashMap();
        HSSFRow row = sheet.createRow(rownum);
        row.setHeight((short) 600);
        int i=0;
        for (Map.Entry<String, String> entry : title.entrySet()) {
            sheet.setColumnWidth(i,entry.getKey().getBytes(StandardCharsets.UTF_8).length * 2 * 256);
            if (StringUtils.isNotBlank(entry.getValue()))
                enumMap.put(i, entry.getValue());
            createRow(row, titleCellStyle, i, entry.getKey());
            i++;
        }
        for (Iterator<Map.Entry<Object, String>> it = enumMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Object, String> next = it.next();
            setHSSFValidation(sheet, next.getValue().split(","), 0,
                    Integer.MAX_VALUE, (Integer) next.getKey(), (Integer) next.getKey());
        }
        if (CollectionUtil.isNotEmpty(datas)){
            for (List<Object> columns : datas) {
                ++rownum;
                HSSFRow hssfRow = sheet.createRow(rownum);
                hssfRow.setHeight((short) 400);
                for (int i1 = 0; i1 < columns.size(); i1++) {
                    createRow(hssfRow, dataCellStyle, i1, String.valueOf(columns.get(i1)));
                }
            }
        }
        return wb;
    }
    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return
     */
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    /**
     * 创建通用EXCEL头部
     *
     * @param headString 头部显示的字符
     * @param rownum 行
     * @param colSum     该报表的列数
     * @param sheet
     * @param cellStyle
     */
    public static void createNormalHead(String headString,int rownum, int colSum, HSSFSheet sheet,HSSFCellStyle cellStyle) {
        HSSFRow row = sheet.createRow(rownum);
        // 设置第一行
        HSSFCell cell = row.createCell(0);
        row.setHeight((short) 800);
        // 定义单元格为字符串类型 中文处理
        cell.setCellType(CellType.STRING);
        cell.setCellValue(new HSSFRichTextString(headString));
        // 指定合并区域
        sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, (short) 0, (short) colSum));
        cell.setCellStyle(cellStyle);
    }

    /**
     * 创建列
     *
     * @param row       当前行
     * @param cellStyle 样式
     * @param index     列下标
     * @param content   内容
     */
    public static void createRow(HSSFRow row, HSSFCellStyle cellStyle, int index, String content) {
        HSSFCell cell = row.createCell(index);
        cell.setCellValue(new HSSFRichTextString(content));
        cell.setCellStyle(cellStyle);
    }

    /**
     * 设置列头样式
     *
     * @return
     */
    public static HSSFCellStyle headCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        // 指定单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 指定当单元格内容显示不下时换行
        cellStyle.setWrapText(true);
        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 20);
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 设置标题单元格样式
     *
     * @param wb
     * @return
     */
    public static HSSFCellStyle titleCellStyle(HSSFWorkbook wb) {
        // 定义单元格格式，添加单元格表样式，并添加到工作簿
        HSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格水平对齐类型
        // 指定单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 指定单元格自动换行
        cellStyle.setWrapText(true);
        cellStyle.setLocked(true);
        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setFontName("微软雅黑");
        //设置字体大小
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 设置数据列样式
     * @param wb
     * @return
     */
    public static HSSFCellStyle dataCellStyle(HSSFWorkbook wb) {
        // 定义单元格格式，添加单元格表样式，并添加到工作簿
        HSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格水平对齐类型
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 指定单元格自动换行
        cellStyle.setWrapText(true);
        cellStyle.setLocked(true);
        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setFontName("微软雅黑");
        //设置字体大小
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * Excel解析
     * @param inputStream
     * @return value-》 值
     */
    public static List<List<Object>> analysisExcelList(InputStream inputStream){
        List<LinkedHashMap<String, Object>>  linkedHashMaps= analysisExcel(inputStream);
        List<List<Object>> result = new ArrayList<>();
        if (CollectionUtil.isEmpty(linkedHashMaps))
            return result;

        for (LinkedHashMap<String, Object> linkedHashMap : linkedHashMaps) {
            ArrayList<Object> list = new ArrayList<>();
            for (Map.Entry<String, Object> objectEntry : linkedHashMap.entrySet()) {
                list.add(objectEntry.getValue());
            }
            result.add(list);
        }
        return result;
    }
    /**
     * Excel解析
     *
     * @param inputStream 文件流
     * @return key -》列下标 value-》 值
     */
    public static List<LinkedHashMap<String, Object>> analysisExcel(InputStream inputStream){
        Workbook wb = null;
        List<LinkedHashMap<String, Object>> result = null;
        try {
            wb = WorkbookFactory.create(inputStream);
            int numberOfSheets = wb.getNumberOfSheets();
            result = new ArrayList<>(numberOfSheets);
            for (int i = 0; i <= wb.getActiveSheetIndex(); i++) {
                Sheet sheetAt = wb.getSheetAt(i);
                if (null == sheetAt) {
                    return null;
                }
                // 遍历行Row
                int lastRowNum = sheetAt.getLastRowNum();
                List title = null;
                for (int rowNum = 2; rowNum <= lastRowNum; rowNum++) {
                    Row row = sheetAt.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    // 遍历列Cell
                    short lastCellNum = row.getLastCellNum();
                    if (lastCellNum == -1) {
                        continue;
                    }
                    //如果是第一排则存到标题List里
                    if (rowNum == 0) {
                        title = new ArrayList<>(lastCellNum);
                        for (int cellNum = 0; cellNum <= lastCellNum; cellNum++) {
                            Cell hssfCell = row.getCell(cellNum);
                            if (null == hssfCell) {
                                continue;
                            }
                            title.add(getValueCell(hssfCell));
                        }
                    }
                    LinkedHashMap<String, Object> rowMap = new LinkedHashMap<>(lastCellNum);
                    for (int cellNum = 0; cellNum <= lastCellNum; cellNum++) {
                        Cell hssfCell = row.getCell(cellNum);
                        if (null == hssfCell) {
                            continue;
                        }
                        rowMap.put(CollectionUtils.isNotEmpty(title) && cellNum < title.size() && null !=
                                title.get(cellNum) ? title.get(cellNum).toString() : String.valueOf(cellNum), getValueCell(hssfCell));
                    }
                    result.add(rowMap);
                }
            }
        } catch (IOException e) {
            throw new IgnoreException("解析Excel失败："+e.getMessage());
        }finally {
            if (wb != null){
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 提取单元格中的值
     *
     * @param cell 每一行对象
     */
    private static Object getValueCell(Cell cell) {
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                return dataFormatter.formatCellValue(cell);
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                cell.setCellType(CellType.STRING);
                return cell.getStringCellValue();
        }
    }

}
