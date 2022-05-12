package org.basis.framework.excel;

import cn.hutool.core.date.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/10/22 10:34 上午
 **/
public class Test {
    public static void main(String[] args) throws IOException {
        Map<String, String> map= new LinkedHashMap<>();
        map.put("会员名称","");
        map.put("会员等级","钻石会员,铂金会员,黄金会员,白银会员,青铜会员");
        map.put("订单号","");
        map.put("订单时间","");
        map.put("数量","");
        List<List<Object>> lists = new ArrayList<>();
        ArrayList<Object> list = new ArrayList<>();
        list.add("1111");
        list.add("钻石会员");
        list.add("2021102201");
        list.add(DateUtil.date());
        list.add("2");
        lists.add(list);
        list = new ArrayList<>();
        list.add("222");
        list.add("铂金会员");
        list.add("2021102202");
        list.add(DateUtil.date());
        list.add("22");
        lists.add(list);
        HSSFWorkbook workbook = ExcelUtils.createExcel("商城订单数据表", map, lists);
        OutputStream outputStream = new FileOutputStream("商城订单数据表.xls");
        workbook.write(outputStream);

        FileInputStream fileInputStream = new FileInputStream("商城订单数据表.xls");
        List<List<Object>> analysisExcelList = ExcelUtils.analysisExcelList(fileInputStream);
        for (List<Object> objects : analysisExcelList) {
            for (Object object : objects) {
                System.out.print(object+" ");
            }
            System.out.println();
        }

    }
}
