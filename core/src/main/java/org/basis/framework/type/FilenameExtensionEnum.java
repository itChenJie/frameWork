package org.basis.framework.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Description 文件扩展名枚举
 * @Author ChenWenJie
 * @Data 2021/6/11 3:06 下午
 */
@Getter
@AllArgsConstructor
public enum FilenameExtensionEnum {
    BMP(".bmp","image/bmp"),
    GIF(".gif","image/gif"),
    JPEG(".jpeg","image/jpg"),
    JPG(".jpg","image/jpg"),
    PNG(".png","image/jpg"),
    HTML(".html","text/html"),
    TXT(".txt","text/plain"),
    VSD(".vsd","application/vnd.visio"),
    PPTX(".pptx","application/msword"),
    PPT(".ppt","application/msword"),
    XML(".xml","text/xml"),
    APK(".apk","application/octet-stream");

    /**
     * 描述
     */
    private String code;
    /**
     * 编码
     */
    private String description;

    public static FilenameExtensionEnum of(String code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
