package org.basis.framework.jgit.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 描述
     */
    private String message;
    /**
     * 提交ID
     */
    private String commitId;
    /**
     * 版本库
     */
    private String versionLibrary;

}
