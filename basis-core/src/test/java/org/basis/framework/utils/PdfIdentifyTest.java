package org.basis.framework.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.basis.framework.Identify.pdf.IdentifyPDFProperties;
import org.basis.framework.Identify.pdf.IdentifyRegexConfig;
import org.basis.framework.Identify.pdf.PdfIdentifyConfigUtil;
import org.basis.framework.Identify.pdf.PdfIdentifyUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @Description
 **/
public class PdfIdentifyTest {

    /**
     * 按坐标提取
     */
    @Test
    public void  extractByCoordinates() throws IOException {
        PDDocument document = PDDocument.load(new File(""));
        PdfIdentifyUtil.extractByCoordinates(document,"发动机号;发动机号码", "车架号;VIN码/车架号", "使用性质");
    }

    @Test
    public void identifyProperties(){
        IdentifyPDFProperties pdfProperties = PdfIdentifyConfigUtil.loadProperties("D:\\Idea-project\\frameWork\\basis-core\\src\\test\\java\\org\\basis\\framework\\utils\\pdfIdentify.json");
        System.out.println(pdfProperties.toString());
    }

    public void regexProcess(){
        IdentifyPDFProperties pdfProperties = new IdentifyPDFProperties();
        for (IdentifyRegexConfig regexConfig : pdfProperties.getRegex()) {
            String val = PdfIdentifyUtil.regexProcess("", regexConfig.getFile(), regexConfig.getConfigs(),null);
        }
    }
}
