package org.basis.framework.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 *  分页
 * @Author CWJ
 * @Data 2021/3/18 下午4:32
 **/
@Data
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1l;
    /**
     * 总数
     */
    private int totalCount;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数
     */
    private int currPage;

    private List<?> list;

    /**
     * 分页
     * @param list
     * @param totalCount
     * @param pageSize
     * @param currPage
     */
    public PageUtils(List< ? > list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
    }

    public PageUtils(IPage<?> iPage) {
        this.list = iPage.getRecords();
        this.totalCount = (int) iPage.getTotal();
        this.pageSize = (int) iPage.getSize();
        this.currPage = (int) iPage.getCurrent();
        this.totalPage = (int) iPage.getPages();
    }
}
