package org.basis.framework.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.basis.framework.constant.GitConsts;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 文件工具类
 * @Author ChenWenJie
 * @Data 2020/11/20 2:55 下午
 **/
public class FileUtils {
    private static final Log logger = LogFactory.getLog(FileUtils.class);
    /**
     * 功能：根据标识获取版本库
     *
     * @param code 标签代码
     * @return 返回版本库
     */
    public static String getVersionLibrary(String code) {
        return code.split("\\.")[0];
    }

    /**
     * 功能：获取文件相对路径（版本库）
     *
     * @param code 标签代码
     * @param id   唯一标识
     * @return 返回文件相对路径
     */
    public static String getRepositoryFilePath(String code, String id) {
        return getPath(code, id, File.separator);
    }

    /**
     * 功能：获取文件相对路径（当前项目库）
     *
     * @param code 标签代码
     * @param id   唯一标识
     * @return 返回文件相对路径
     */
    public static String getProjectFilePath(String code, String id) {
        StringBuilder builder = new StringBuilder();
        builder.append(GitConsts.REPOSITORY_NAME).append(File.separator);
        Arrays.asList( code.split("\\.")).forEach(str->
            builder.append(str).append(File.separator)
        );
        builder.append(getFileName(code, id));
        return builder.toString();
    }

    /**
     * 功能：获取文件相对路径（Git）
     *
     * @param code 标签代码
     * @param id   唯一标识
     * @return 返回文件相对路径
     */
    public static String getGitFilePath(String code, String id) {
        return getPath(code, id, "/");
    }


    public static String getFileName(String code, String id) {
        StringBuilder builder = new StringBuilder();
        builder.append(code.replaceAll("\\.", "_"));
        builder.append("_").append(id).append(GitConsts.FILE_SUFFIX);
        return builder.toString();
    }

    private static String getPath(String code, String id, String separator) {
        StringBuilder builder = new StringBuilder();
        String[] strs = code.split("\\.");
        if (strs.length > 1) {
            for (int i = 1; i < strs.length; i++) {
                builder.append(strs[i]).append(separator);
            }
        }
        builder.append(code.replaceAll("\\.", "_"));
        builder.append("_").append(id).append(GitConsts.FILE_SUFFIX);
        return builder.toString();
    }

    /**
     * 功能：根据文件路径获取文件名称
     *
     * @param path 文件路径
     * @return 返回文件名
     */
    public static String getFileNameByPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * 功能:根据文件路径获取文件前缀
     *
     * @param path 文件路径
     * @return 返回文件路径
     */
    public static String getCode(String path) {
        String fileName = getFileNameByPath(path);
        return fileName.substring(0, fileName.lastIndexOf("_")).replaceAll("_", "\\.");
    }

    /**
     * 功能：根据文件路径获取标识ID
     *
     * @param path 文件路径
     * @return 返回标识ID
     */
    public static String getId(String path) {
        String fileName = getFileNameByPath(path);
        return fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
    }

    /**
     * 功能：获取文件夹路径（相对于版本库）
     *
     * @param code 标签标识
     * @return 返回文件夹路径
     */
    public static String getFolderPath(String code) {
        String[] strs = code.split("\\.");
        StringBuilder builder = new StringBuilder();
        if (strs.length > 1) {
            for (int i = 1; i < strs.length; i++) {
                builder.append(strs[i]);
                if(i<(strs.length-1)){
                    builder.append("/");
                }
            }
        }
        return builder.toString();
    }

    /**
     * 获取文件内容
     * @param code
     * @param  id
     * @return 文件内容
     */
    public static String getFileContent(String code,String id){
        String path = "./"+getProjectFilePath(code, id);
        String content = "";
        try {
            List<String> list = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            content = String.join("", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return content;
    }
}
