package org.basis.framework.jgit;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.basis.framework.constant.GitConsts;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Description git工厂类
 * @Author ChenWenJie
 * @Data 2020/11/20 2:55 下午
 **/
public class GitFactory {

    /**
     * 功能：获取Git对象
     *
     * @return 返回git对象
     */
    public static Git getInstance() {
        return getInstance((String) null);
    }

    /**
     * 功能：根据指定路径获取git对象
     *
     * @param path 相对路径
     * @return Git
     */
    @SneakyThrows
    public static Git getInstance(String path) {
        if (StringUtils.isNotBlank(path)) {
            return Git.open(new File(GitConsts.REPOSITORY_NAME + File.separator + path));
        }
        return null;
    }

    /**
     * 功能：初始化版本库
     *
     * @param path 仓库路径
     * @return Git
     */
    @SneakyThrows
    public static Git init(String path) {
        Path path1 = Paths.get(GitConsts.REPOSITORY_NAME + File.separator + path);
        Files.createDirectories(path1);
        return Git.init().setDirectory(new File(GitConsts.REPOSITORY_NAME + File.separator + path)).call();

    }
}
