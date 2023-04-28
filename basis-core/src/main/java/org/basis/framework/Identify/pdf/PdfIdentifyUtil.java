package org.basis.framework.Identify.pdf;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.basis.framework.error.BizCodeEnume;
import org.basis.framework.error.ServiceException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description  pdf识别 工具类
 * @Author ChenWenJie
 * @Data 2023/04/28 2:55 下午
 **/
@Slf4j
public class PdfIdentifyUtil {
    /** 中文*/
    public static final String chineseRegex ="[\u4e00-\u9fa5]";
    /**
     *  Matcher.quoteReplacement("\r\n") 符合替换 处理 linux中转义符格式不同问题
     * */
    /**
     * 根据路径获取pdf 内容
     * @param pdfPath
     * @return
     * @throws ServiceException
     */
    public static String readText(String pdfPath) throws ServiceException {
        String allText="";
        InputStream stream = download(pdfPath);
        try(PDDocument doc = PDDocument.load(stream)){
            PDFTextStripper textStripper = new PDFTextStripper();
            // 设置按位置排序
            textStripper.setSortByPosition(true);
            allText = textStripper.getText(doc);
        }catch (Exception e){
            throw new ServiceException("获取 导入pdf文件失败！",BizCodeEnume.DEFAULT.getCode(),e);
        }finally {
            try {
                if (stream!=null) {
                    stream.close();
                }
            } catch (IOException e) {
                throw new ServiceException("关闭 导入pdf文件失败！",BizCodeEnume.DEFAULT.getCode(),e);
            }
        }
        return allText;
    }

    private static InputStream download(String filePath) throws ServiceException {
        InputStream inputStream = null;
        try {
            // 建立链接
            URL httpUrl = new URL(filePath);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            inputStream = conn.getInputStream();
        }catch (Exception e) {
            throw new ServiceException("下载文件失败!",BizCodeEnume.DEFAULT.getCode(),e);
        }
        return inputStream;
    }

    /**
     * 正则表达式执行
     * @param str 文本
     * @param regex 规则
     * @return
     */
    public static String regexProcess(String str,String regex){
        return regexProcess(str, null, regex);
    }

    /**
     * 正则表达式执行
     * @param str 文本
     * @param param 字段
     * @param regexConfigs 规则集合
     * @return
     */
    public static String regexProcess(String str, String param, List<RegexConfig> regexConfigs){
        return regexProcess(str, param, regexConfigs, null);
    }

    /**
     * 正则表达式执行
     * @param str 文本
     * @param param
     * @param regexConfigs 规则集合
     * @param city 城市
     * @return
     */
    public static String regexProcess(String str, String param, List<RegexConfig> regexConfigs,String city){
        String val="";
        for (RegexConfig regexConfig : regexConfigs) {
            if (StringUtils.isBlank(city)||StringUtils.isBlank(regexConfig.getCity())||regexConfig.getCity().equals(city)) {
                val = regexProcess(str, param, regexConfig.getRegex());
                if (!StringUtils.isBlank(val) && regexConfig.getChildRegex() != null) {
                    for (String childRegex : regexConfig.getChildRegex()) {
                        val = regexProcess(val, param, childRegex);
                    }
                }
                if (!StringUtils.isBlank(val)) {
                    return val;
                }
            }
        }
        return val;
    }

    /**
     * 规则处理
     * @param str 文本
     * @param param 参数
     * @param regex 规则
     * @return
     */
    public static String regexProcess(String str,String param,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            if (param==null)
                return matcher.group();

            if (!StringUtils.isBlank(matcher.group(param))) {
                return matcher.group(param);
            }else {
                return " ";
            }
        }
        return "";
    }

    /**
     * 日期格式化
     * @param dateStr
     * @return
     */
    public static String dateFormat(String dateStr){
        try {
            if (!dateStr.contains("年")){
                String startDateStr=PdfIdentifyUtil.regexProcess(dateStr,null,"\\d{4}-\\d+-\\d+\\d+:\\d{2}");
                return  DateUtil.format(DateUtil.parse(startDateStr, "yyyy-MM-ddHH:mm"),"yyyy-MM-dd HH:mm:ss");
            }else {
                if (dateStr.contains("时")&&dateStr.contains("分")){
                    String startDateStr=PdfIdentifyUtil.regexProcess(dateStr,null,"\\d{4}年\\d+月\\d+日\\d+时\\d+分");
                    return  DateUtil.format(DateUtil.parse(startDateStr, "yyyy年MM月dd日HH时mm分"),"yyyy-MM-dd HH:mm:ss");
                }else if(dateStr.contains("时")){
                    if (dateStr.contains(":")) {
                        String startDateStr= PdfIdentifyUtil.regexProcess(dateStr,null,"\\d{4}年\\d+月\\d+日\\d+:\\d+时");
                        return  DateUtil.format(DateUtil.parse(startDateStr, "yyyy年MM月dd日HH:mm时"),"yyyy-MM-dd HH:mm:ss");
                    }
                    String startDateStr=PdfIdentifyUtil.regexProcess(dateStr,null,"\\d{4}年\\d+月\\d+日\\d+时");
                    return  DateUtil.format(DateUtil.parse(startDateStr, "yyyy年MM月dd日HH时"),"yyyy-MM-dd HH:mm:ss");
                } else {
                    String startDateStr = PdfIdentifyUtil.regexProcess(dateStr,null,"\\d{4}年\\d+月\\d+日");
                    return DateUtil.format(DateUtil.parse(startDateStr, "yyyy年MM月dd日"),"yyyy-MM-dd HH:mm:ss");
                }
            }
        }catch (Exception e){
            log.error("日期格式化异常！",e.fillInStackTrace());
        }
        return "";
    }

    /**
     * 根据标题对应坐标进行截取并提取内容
     * @param document
     * @param xStr
     * @param yStr pageWidth 页面宽度
     * @param hStr 高度
     * @return
     */
    public static String extractByCoordinates(PDDocument document,String xStr,String yStr,String hStr){
        return extractByCoordinates(document,xStr,yStr,hStr,true);
    }

    /**
     * 根据标题对应坐标进行截取并提取内容
     * @param document
     * @param xStr
     * @param yStr pageWidth 页面宽度
     * @param hStr
     * @param row false 单行 true 双行
     * @return
     */
    public static String extractByCoordinates(PDDocument document, String xStr, String yStr, String hStr, boolean row) {
        try {
            PDPageTree allPages = document.getDocumentCatalog().getPages();
            PDPage firstPage = (PDPage) allPages.get(0);
            int pageWidth = Math.round(firstPage.getCropBox().getWidth());
            List<float[]> xlist =getCoordinate(xStr,document);
            if (xlist==null || xlist.size()<=0)
                return "";

            List<float[]> ylist = new ArrayList<>();
            if (!"pageWidth".equals(yStr)){
                ylist =getCoordinate(yStr,document);
            }
            List<float[]> hlist = getCoordinate(hStr,document);
            if (hlist==null || hlist.size()<=0)
                return "";

            int fdjx = Math.round(xlist.get(0)[1]);
            int fdjy,fdjw;
            if (yStr.equals("pageWidth")){
                fdjy = Math.round(xlist.get(0)[0])-(Math.round(hlist.get(0)[0])-Math.round(xlist.get(0)[0]))+5;
                fdjw =  pageWidth-(Math.round(xlist.get(0)[1]));
            }else {
                fdjy = Math.round(ylist.get(0)[0])-(Math.round(hlist.get(0)[0])-Math.round(ylist.get(0)[0]))+5;
                fdjw = Math.round(ylist.get(0)[1])-Math.round(xlist.get(0)[1]);
            }
            int fdjh = Math.round(hlist.get(0)[0])-Math.round(xlist.get(0)[0])+1;
            Rectangle rectangle = new Rectangle(fdjx, fdjy, fdjw, fdjh);
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            stripper.addRegion("val", rectangle);
            stripper.extractRegions(firstPage);
            String val = stripper.getTextForRegion("val");
            if (row) {
                if (StringUtils.isNotBlank(val) && val.split(Matcher.quoteReplacement("\n")).length == 3) {
                    String[] split = val.split(Matcher.quoteReplacement("\n"));
                    return (split[0] + split[2]).replaceAll(" ", "").replaceAll("\\u00A0","");
                } else {
                    return "";
                }
            }else {
                return val.replaceAll(" ","").replaceAll("\\u00A0","");
            }

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取文本所在pdf中的坐标
     * @param str
     * @param document
     * @return 坐标：y:" + xlist[0] + ", x:" + xlist[1]
     * @throws IOException
     */
    public static List<float[]> getCoordinate(String str, PDDocument document) throws IOException {
        List<float[]> list = new ArrayList<>();
        for (String y : str.split(";")) {
            PdfBoxKeyWordPosition kwp = new PdfBoxKeyWordPosition(y);
            kwp.setDocument(document);
            list = kwp.getCoordinate();
            if (list!=null&&list.size()>0)
                break;
        }
        return list;
    }

}
