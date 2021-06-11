package org.basis.framework.jgit.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class GitParam {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommitParam {
        /**
         * 版本库
         */
        private String versionLibrary;
        /**
         * 标签名称
         */
        private String tagName;
        /**
         * 提交说明
         */
        private String message;
        /**
         * 提交人
         */
        private String userName;
        /**
         * 提交人对应的邮件
         */
        private String email;
        /**
         * 待提交文件集
         */
        private List<String> filePath;
    }

}
