package org.basis.framework.page;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageImpl<T> implements IPage<T> {
    private long pageSize = 20L;
    private long pageIndex = 1L;
    private List<T> list = Collections.emptyList();
    private long totalRecord = 0L;
    private String[] asc;
    private String[] desc;
    private boolean isDesc = true;

    public PageImpl() {
    }

    public PageImpl(long pageIndex, long pageSize) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }

    public PageImpl(List<T> list, long pageIndex, long pageSize, long totalRecord) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.list = list;
        this.totalRecord = totalRecord;
    }

    public void setAsc(String asc) {
        if (!StringUtils.isEmpty(asc)) {
            this.asc = asc.split(",");
        }
    }

    public void setDesc(String desc) {
        if (!StringUtils.isEmpty(desc)) {
            this.desc = desc.split(",");
        }
    }

    public long getTotalPage() {
        return this.getPages();
    }

    /**
     * 获取排序信息，排序的字段和正反序
     *
     * @return 排序信息
     */
    @Override
    public List<OrderItem> orders() {
        List<OrderItem> collect = new ArrayList<>();
        if (Objects.nonNull(asc)){
            collect = Arrays.stream(asc).map(item -> OrderItem.asc(item)).collect(Collectors.toList());
        }
        if (Objects.nonNull(desc)){
            collect =  Arrays.stream(desc).map(item -> OrderItem.desc(item)).collect(Collectors.toList());
        }
        return collect;
    }

    @JsonIgnore
    public List<T> getRecords() {
        return this.list;
    }

    public PageImpl<T> setRecords(List<T> records) {
        this.list = records;
        return this;
    }

    @JsonIgnore
    public long getTotal() {
        return this.totalRecord;
    }

    public PageImpl<T> setTotal(long total) {
        this.totalRecord = total;
        return this;
    }

    @JsonIgnore
    public long getSize() {
        return this.pageSize;
    }

    public PageImpl<T> setSize(long size) {
        this.pageSize = size;
        return this;
    }

    @JsonIgnore
    public long getCurrent() {
        return this.pageIndex;
    }

    public PageImpl<T> setCurrent(long current) {
        this.pageIndex = current;
        return this;
    }

    @JsonIgnore
    public String[] ascs() {
        return this.asc;
    }

    @JsonIgnore
    public String[] descs() {
        return this.desc;
    }

    public <R> PageImpl<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = (List)this.getRecords().stream().map(mapper).collect(Collectors.toList());
        return (PageImpl<R>) this.setRecords((List<T>) collect);
    }

    public long getPageSize() {
        return this.pageSize;
    }

    public long getPageIndex() {
        return this.pageIndex;
    }

    public List<T> getList() {
        return this.list;
    }

    public long getTotalRecord() {
        return this.totalRecord;
    }

    public String[] getAsc() {
        return this.asc;
    }

    public String[] getDesc() {
        return this.desc;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageImpl)) {
            return false;
        } else {
            PageImpl<?> other = (PageImpl)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getPageSize() != other.getPageSize()) {
                return false;
            } else if (this.getPageIndex() != other.getPageIndex()) {
                return false;
            } else {
                label44: {
                    Object this$list = this.getList();
                    Object other$list = other.getList();
                    if (this$list == null) {
                        if (other$list == null) {
                            break label44;
                        }
                    } else if (this$list.equals(other$list)) {
                        break label44;
                    }

                    return false;
                }

                if (this.getTotalRecord() != other.getTotalRecord()) {
                    return false;
                } else if (!Arrays.deepEquals(this.getAsc(), other.getAsc())) {
                    return false;
                } else if (!Arrays.deepEquals(this.getDesc(), other.getDesc())) {
                    return false;
                } else {
                    return Arrays.deepEquals(this.getDesc(), other.getDesc());
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageImpl;
    }

    public int hashCode() {
        int result = 1;
        long $pageSize = this.getPageSize();
        result = result * 59 + (int)($pageSize >>> 32 ^ $pageSize);
        long $pageIndex = this.getPageIndex();
        result = result * 59 + (int)($pageIndex >>> 32 ^ $pageIndex);
        Object $list = this.getList();
        result = result * 59 + ($list == null ? 43 : $list.hashCode());
        long $totalRecord = this.getTotalRecord();
        result = result * 59 + (int)($totalRecord >>> 32 ^ $totalRecord);
        result = result * 59 + Arrays.deepHashCode(this.getAsc());
        result = result * 59 + Arrays.deepHashCode(this.getDesc());
        result = result * 59 + Arrays.deepHashCode(this.getDesc());
        return result;
    }

    public String toString() {
        return "PageImpl(pageSize=" + this.getPageSize() + ", pageIndex=" + this.getPageIndex() + ", list=" + this.getList() + ", totalRecord=" + this.getTotalRecord() + ", asc=" + Arrays.deepToString(this.getAsc()) + ", desc=" + Arrays.deepToString(this.getDesc()) + ", isDesc=" + Arrays.deepToString(this.getDesc()) + ")";
    }
}
