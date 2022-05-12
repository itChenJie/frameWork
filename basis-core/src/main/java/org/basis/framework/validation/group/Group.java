package org.basis.framework.validation.group;

import javax.validation.GroupSequence;

/**
 * @Description 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 * @Author CWJ
 * @Data 2021/3/19 下午12:24
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
