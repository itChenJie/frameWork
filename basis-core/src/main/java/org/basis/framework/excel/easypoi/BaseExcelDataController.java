package org.basis.framework.excel.easypoi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2022/5/31 15:53
 **/
@Slf4j
public class BaseExcelDataController {

    /**
     * 最终设置的response header是这样:
     * Content-Disposition: attachment;
     * filename="encoded_text";
     * filename*=utf-8''encoded_text
     * 其中encoded_text是经过RFC 3986的"百分号URL编码"规则处理过的文件名
     * @param filename
     * @param list
     * @param clz
     * @param dataHandler
     * @return
     */
    public ResponseEntity<byte[]> download(String filename,  List<?> list,  Class<?> clz, ConverterDataHandler dataHandler) {

        filename += "-" + this.getCurTimeStr() + ".xls";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String headerValue = " filename=\"" + this.encodeURIComponent(filename) + "\";";
        headerValue += " filename*=utf-8''" + this.encodeURIComponent(filename);
        headers.setContentDispositionFormData("attachment", headerValue);

        ExportParams params = new ExportParams();

        params.setDataHandler(dataHandler);
         Workbook workbook = ExcelExportUtil.exportExcel(params, clz, list);
        //下载方法
        try {
            @Cleanup  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
        } catch ( IOException e) {
            log.error("导出失败", e);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 符合 RFC 3986 标准的"百分号URL编码"
     * 在这个方法里，空格会被编码成%20，而不是+
     * 和浏览器的encodeURIComponent行为一致
     */
    public String encodeURIComponent( String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
        } catch ( UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getCurTimeStr() {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(LocalDate.now());
    }
}
