package org.basis.framework.excel.alibabawrite;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class ChannelOrderImportCellStyleStrategy extends HorizontalCellStyleStrategy {

    public ChannelOrderImportCellStyleStrategy() {
        super(createHeadCellStyle(), createContentCellStyle());
    }

    private static WriteCellStyle createHeadCellStyle() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("simsun");
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteFont.setBold(true);
        headWriteFont.setColor(IndexedColors.BLACK.index);
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteCellStyle.setWrapped(true);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return headWriteCellStyle;
    }

    private static WriteCellStyle createContentCellStyle() {
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteFont.setBold(false);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return contentWriteCellStyle;
    }

}