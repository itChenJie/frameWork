package org.basis.framework.Identify;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * pdf识别
 */
@Slf4j
public class PdfIdentifyUtil {

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
