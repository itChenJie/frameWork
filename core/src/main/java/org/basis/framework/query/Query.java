package org.basis.framework.query;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.basis.framework.page.PageImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/12/6 6:07 下午
 **/
public abstract class Query<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private LambdaQueryWrapper<T> wrapper;
    private String asc;
    private String desc;
    private long pageSize;
    private long pageIndex;
    private boolean callGetWrapper;

    public Query() {
        this.wrapper = Wrappers.lambdaQuery();
        this.asc = "";
        this.desc = "";
        this.pageSize = 20L;
        this.pageIndex = 1L;
        this.callGetWrapper = false;
    }

    public Query(LambdaQueryWrapper<T> wrapper) {
        this.wrapper = Wrappers.lambdaQuery();
        this.asc = "";
        this.desc = "";
        this.pageSize = 20L;
        this.pageIndex = 1L;
        this.callGetWrapper = false;
        this.wrapper = wrapper;
        this.callGetWrapper();
    }

    public Query(IPage<T> page) {
        this.wrapper = Wrappers.lambdaQuery();
        this.asc = "";
        this.desc = "";
        this.pageSize = 20L;
        this.pageIndex = 1L;
        this.callGetWrapper = false;
        this.pageSize = page.getSize();
        this.pageIndex = page.getCurrent();
    }

    public Query(LambdaQueryWrapper<T> wrapper, IPage<T> page) {
        this(page);
        this.wrapper = wrapper;
        this.callGetWrapper();
    }

    private void callGetWrapper() {
        this.callGetWrapper = true;
    }

    protected boolean valid(Integer value) {
        return value != null;
    }

    protected boolean valid(Long value) {
        return value != null;
    }

    protected boolean valid(Double value) {
        return value != null;
    }

    protected boolean valid(String value) {
        return StringUtils.isNotBlank(value);
    }

    protected boolean valid(Date value) {
        return value != null;
    }

    protected boolean valid(List<?> list) {
        return list != null && list.size() > 0;
    }

    protected boolean valid(Boolean value) {
        return value != null;
    }

    protected boolean valid(Enum value) {
        return value != null;
    }

    public abstract Wrapper<T> toWrapper();

    public PageImpl<T> toPage() {
        PageImpl<T> page = new PageImpl(this.pageIndex, this.pageSize);
        page.setAsc(this.asc);
        page.setDesc(this.desc);
        return page;
    }

    public LambdaQueryWrapper<T> getWrapper() {
        return this.wrapper;
    }

    public String getAsc() {
        return this.asc;
    }

    public void setAsc(String asc) {
        this.asc = asc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageSize() {
        return this.pageSize;
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getPageIndex() {
        return this.pageIndex;
    }

    public boolean isCallGetWrapper() {
        return this.callGetWrapper;
    }
}
