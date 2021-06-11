package org.basis.framework.jgit.bean;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Response {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommitResponse extends Response {
        private String commitId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogResponse extends Response {
        /**
         * 提交ID
         */
        private String commitId;
        /**
         * 提交说明
         */
        private String message;
        /**
         * 提交人
         */
        private String committer;
        /**
         * 标签名称
         */
        private String tagName;
        /**
         * 提交日期
         */
        private Date date;
        /**
         * 提交数据
         */
        private List<LogData> logDataList;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogData {
        /**
         * 数据id
         */
        private String id;
        /**
         * 标签代码
         */
        private String code;
        /**
         * 数据内容
         */
        private String data;
    }

}
