
package org.basis.framework.query;

import org.basis.framework.page.PageImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
/**
 *@Description
 * mybatis基础服务
 *@Author ChenWenJie
 *@Data 2021/12/7 5:14 下午
 **/
public interface IBaseService<T> {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    T get(Serializable id);

    /**
     * 根据条件查询第一个 不抛出异常
     * @param query
     * @return
     */
    default T getOne(Query<T> query) {
        return this.getOne(query, false);
    }

    /**
     * 根据条件查询第一个 是否抛出异常
     * @param query
     * @param throwEx
     * @return
     */
    T getOne(Query<T> query, boolean throwEx);

    /**
     * 根据id集合查询
     * @param ids
     * @return
     */
    Collection<T> getListByIds(Collection<? extends Serializable> ids);
    /**
     * 根据条件查询第一个
     * @param query
     * @return
     */
    T getFirst(Query<T> query);

    /**
     * 根据条件查询
     * @param query
     * @return
     */
    List<T> getList(Query<T> query);

    /**
     * 分页 根据条件查询
     * @param query
     * @param limit
     * @return
     */
    PageImpl<T> getPageList(Query<T> query, int limit);

    default PageImpl<T> getPageList(Query<T> query) {
        return this.getPageList(query, (Integer)Optional.ofNullable(PageConfig.maxPageSize).orElse(200));
    }

    List<T> getSliceList(Query<T> query);

    /**
     * 合计
     * @param query
     * @return
     */
    int count(Query<T> query);

    /**
     * 新增
     * @param entity
     * @return
     */
    T create(T entity);

    /**
     * 批量创建
     * @param entityList
     */
    void create(Collection<T> entityList);

    /**
     * 根据id删除
     * @param id
     */
    void delete(Serializable id);

    /**
     * 根据条件删除
     * @param query
     * @return
     */
    boolean delete(Query<T> query);

    /**
     * 根据id集合删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 更新
     * @param entity
     */
    void update(T entity);

    /**
     * 批量更新
     * @param entityList
     * @param batchSize
     */
    void updateAll(Collection<T> entityList, int batchSize);

    /**
     * 批量更新一次最多1000
     * @param domain
     */
    default void updateAll(Collection<T> domain) {
        this.updateAll(domain, 1000);
    }
}
