package org.basis.framework.excel;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.basis.framework.error.IgnoreException;
import org.basis.framework.excel.annotation.ExcelIgnore;
import org.basis.framework.excel.annotation.ExcelProperty;
import org.basis.framework.excel.annotation.ExcelSelected;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Description Excel工具类
 * @Author ChenWenJie
 * @Data 2020/9/24 9:39 上午
 **/
public class ExcelUtils {
    private final static String excel2003L = ".xls"; // 2003- 版本的excel
    private final static String excel2007U = ".xlsx"; // 2007+ 版本的excel
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

    /**
     * 根据实体生成对应 excel 模板
     * @param head
     * @param tips
     * @param sheetName
     * @param map
     * @return
     */
    public static XSSFWorkbook entityTemplate(Class<?> head, String tips, String sheetName, Map<String, List<String>> map){
        XSSFWorkbook workbook =new XSSFWorkbook();
        Font titleFont = workbook.createFont();
        titleFont.setFontName("simsun");
        titleFont.setBold(true);
        titleFont.setColor(IndexedColors.BLACK.index);
        titleFont.setFontHeightInPoints((short) 14);
        XSSFCellStyle titleStyle = (XSSFCellStyle) workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        titleStyle.setWrapText(true);
        titleStyle.setFillForegroundColor(new XSSFColor((CTColor) java.awt.Color.WHITE));
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor((CTColor) java.awt.Color.BLACK));
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Field[] fields = head.getDeclaredFields();
        XSSFRow xssfRow = sheet.createRow(0);
        xssfRow.setHeightInPoints(120);
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setWrapText(true);
        xssfRow.setRowStyle(style);
        xssfRow.createCell(0).setCellValue(tips);
        XSSFRow row = sheet.createRow(1);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelSelected selected = field.getAnnotation(ExcelSelected.class);
            ExcelProperty property = field.getAnnotation(ExcelProperty.class);
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);
            if (excelIgnore!=null){
                continue;
            }
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(property.value()[0]);
            cell.setCellStyle(titleStyle);
            if (selected!=null){
                String[] source = selected.source();
                if (source!=null&& source.length>0){
                    setDropDownBox(workbook,sheetName,source,i);
                }else if (StringUtils.isNotBlank(selected.key())){
                    List<String> vals = map.get(selected.key());
                    if (CollectionUtils.isNotEmpty(vals)){
                        setDropDownBox(workbook,sheetName,vals.toArray(new String[]{}),i);
                    }
                }
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fields.length));
        for (int i = 0; i < fields.length; i++) {
            //针对SXSSFWorkbook需要加上这句代码才能进行宽度自适应
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i,sheet.getColumnWidth(i)*17/10);
        }
        return workbook;
    }

    /**
     * 设置下拉框数据
     * @param wb       表格对象
     * @param typeName 要渲染的sheet名称
     * @param values   下拉框的值
     * @param col      下拉列的下标
     * @author Hower Wong
     * @date 2022年5月27日
     */
    public static void setDropDownBox(XSSFWorkbook wb, String typeName, String[] values, Integer col) {
        //获取所有sheet页个数
        int sheetTotal = wb.getNumberOfSheets();
        //处理下拉数据
        if (values != null && values.length != 0) {
            //新建一个sheet页
            String hiddenSheetName = "hiddenSheet";
            XSSFSheet hiddenSheet = wb.getSheet(hiddenSheetName);
            if (hiddenSheet == null) {
                hiddenSheet = wb.createSheet(hiddenSheetName);
                sheetTotal++;
            }
            // 获取数据起始行
            int startRowNum = hiddenSheet.getLastRowNum() + 1;
            int endRowNum = startRowNum;
            //写入下拉数据到新的sheet页中
            for (int i = 0; i < values.length; i++)
                hiddenSheet.createRow(endRowNum++).createCell(0).setCellValue(values[i]);
            //将新建的sheet页隐藏掉
            wb.setSheetHidden(sheetTotal - 1, true);
            //获取新sheet页内容
            String strFormula = hiddenSheetName + "!$A$" + ++startRowNum + ":$A$" + endRowNum;
            // 设置下拉
            XSSFSheet mainSheet = wb.getSheet(typeName);
            mainSheet.addValidationData(setDataValidation(wb, strFormula, 1, 65535, col, col));
        }
    }

    /**
     * 单元格数据格式校验
     * @param wb         表格对象
     * @param strFormula formula
     * @param firstRow   起始行
     * @param endRow     终止行
     * @param firstCol   起始列
     * @param endCol     终止列
     * @return 返回类型 DataValidation
     */
    public static DataValidation setDataValidation(Workbook wb, String strFormula, int firstRow, int endRow, int firstCol, int endCol) {
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) wb.getSheet("typelist"));
        DataValidationConstraint formulaListConstraint = dvHelper.createFormulaListConstraint(strFormula);
        DataValidation validation = dvHelper.createValidation(formulaListConstraint, regions);
        // 阻止输入非下拉选项的值
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("提示", "请输入下拉选项中的内容");
        return validation;
    }
    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setTopBorderColor(color);
        style.setLeftBorderColor(color);
        style.setRightBorderColor(color);
        style.setBottomBorderColor(color);
    }
    private static FormulaEvaluator evaluator;

    /**
     * 导入excel,提取表单内容
     *
     * @param in,fileName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    public static List<?> importExcel(InputStream in, String fileName,Class<?> object) throws Exception {
        List<Object> list = null;
        // 创建Excel工作薄
        Workbook work = null;
        try {
            work = getWorkbook(in, fileName);
            evaluator = work.getCreationHelper().createFormulaEvaluator();
            if (null == work) {
                throw new Exception("创建Excel工作薄为空！");
            }
            Row row = null;
            Cell cell = null;
            list = new ArrayList<Object>();
            // 读取Excel中第一个sheet
            if (work.getNumberOfSheets() > 0) {
                Sheet sheet = work.getSheetAt(0);
                if (sheet != null) {
                    Row head = sheet.getRow(0);
                    int m = head.getLastCellNum();
                    // 遍历当前sheet中的所有行
                    for (int j = 2; j <= sheet.getLastRowNum(); j++) {
                        row = sheet.getRow(j);
                        if (row == null) {
                            continue;
                        }
                        // 遍历所有的列
                        List<String> li = new ArrayList<String>();
                        Object instance = object.newInstance();
                        Field[] fields = object.getDeclaredFields();
                        for (int y = 0; y < fields.length; y++) {
                            // 空值策略 转换null为""
                            if (!fields[y].getName().equals("isError")){
                                cell = row.getCell(y, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                fields[y].setAccessible(true);
                                fields[y].set(instance,getCellValue(cell));
                            }
                        }
                        list.add(instance);
                    }
                }
            }
        } finally {
            try {
                if(work != null){
                    work.close();
                }
                if(in!= null){
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003L.equals(fileType)) {
            wb = new HSSFWorkbook(inStr); // 2003-
        } else if (excel2007U.equals(fileType)) {
            wb = new XSSFWorkbook(inStr); // 2007+
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 对表格中数值进行格式化
     * @param cell
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getCellValue(Cell cell) {
        String value = null;
        System.out.println(CellType.STRING.getCode());
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    value = org.basis.framework.utils.DateUtil.formatTime(org.basis.framework.utils.DateUtil.UDateToLocalDateTime(date));
                } else {
                    DecimalFormat df = new DecimalFormat("#.#########");// 格式化number
                    // String字符
                    value = df.format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
                value = "";
                break;
            case FORMULA:
                //处理经excel公式算出的值 // 返回经公式计算后的
                CellValue tempCellValue = evaluator.evaluate(cell);
                double iCellValue = tempCellValue.getNumberValue();
                value=String.format("%.5f", iCellValue);
                break;
            default:
                break;
        }
        return value == null ? null : value.trim();
    }
}
