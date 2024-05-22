package org.basis.framework.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Csv 文件工具类
 * @author ChenWenJie
 * @date 2019/7/8 14:26
 * @since 1.0.0
 */
@Service
public class CsvFileUtils {

    public static final String GBK = "GBK";
    public static final String UTF8 = "UTF8";

    /**
     * 读取csv文件
     * @return
     */
    public static List<String>  readCsvFile(MultipartFile file,Integer startLine,String coding){
        BufferedReader br = null;
        if (file.isEmpty()) {
            return null;
        }
        try {
            InputStream inputStream = file.getInputStream();
            InputStreamReader is = new InputStreamReader(inputStream, Charset.forName(coding));
            br = new BufferedReader(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        ArrayList<String> allString = new ArrayList<>();
        try {
            // 读取到的内容给line变量
            Integer lineNum=1;
            while ((line = br.readLine()) != null)
            {
                if(lineNum<startLine){
                    lineNum++;
                    continue;
                }
                everyLine = line;
                allString.add(everyLine);
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;
    }

    /**
     * 读取csv文件
     * @return
     */
    public static List<String>  readCsvFileByInput(InputStream inputStream,Integer startLine,String coding){
        BufferedReader br = null;
        if (inputStream == null) {
            return null;
        }
        try {
            InputStreamReader is = new InputStreamReader(inputStream, Charset.forName(coding));
            br = new BufferedReader(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        ArrayList<String> allString = new ArrayList<>();
        try {
            // 读取到的内容给line变量
            Integer lineNum=1;
            while ((line = br.readLine()) != null)
            {
                if(lineNum<startLine){
                    lineNum++;
                    continue;
                }
                everyLine = line;
                allString.add(everyLine);
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;
    }

}
