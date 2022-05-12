package org.basis.framework.query;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.basis.framework.error.ServiceException;
import org.basis.framework.page.PageImpl;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/12/7 4:53 下午
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T>{

    private Wrapper<T> getQueryWrapper(Query<T> query) {
        return (Wrapper)(query.isCallGetWrapper() ? query.getWrapper() : query.toWrapper());
    }

    @Override
    public T get(Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public T getOne(Query<T> query) {
        return this.getOne(query,true);
    }

    @Override
    public T getOne(Query<T> query, boolean throwEx) {
        return  super.getOne(this.getQueryWrapper(query), throwEx);
    }

    @Override
    public Collection<T> getListByIds(Collection<? extends Serializable> ids) {
        return super.listByIds(ids);
    }

    @Override
    public T getFirst(Query<T> query) {
        return this.getFirst(query);
    }

    @Override
    public List<T> getList(Query<T> query) {
        return this.list(this.getQueryWrapper(query));
    }

    @Override
    public PageImpl<T> getPageList(Query<T> query, int limit) {
        PageImpl<T> page = query.toPage();
        if (page.getSize() >= (long)limit) {
            throw new ServiceException("pageSize不能超过 limit");
        } else {
            return (PageImpl)this.page(page, query.toWrapper());
        }
    }

    @Override
    public List<T> getSliceList(Query<T> query) {
        return this.baseMapper.selectPage(query.toPage(), this.getQueryWrapper(query)).getRecords();
    }

    @Override
    public int count(Query<T> query) { return this.count(query.toWrapper()); }

    @Override
    public T create(T entity) {
        boolean saved = this.save(entity);
        if (!saved) {
            throw new ServiceException("数据新增失败");
        } else {
            return entity;
        }
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void create(Collection<T> entityList) {
        Iterator iterator = entityList.iterator();

        while(iterator.hasNext()) {
            T anEntityList = (T) iterator.next();
            this.baseMapper.insert(anEntityList);
        }
    }

    @Override
    public void delete(Serializable id) {
        boolean deleted = this.removeById(id);
        if (!deleted) {
            throw new ServiceException("数据删除失败");
        }
    }

    @Override
    public boolean delete(Query<T> query) {
        return super.remove(this.getQueryWrapper(query));
    }

    @Override
    public void deleteByIds(Collection<? extends Serializable> ids) {
        if (!ids.isEmpty()) {
            this.removeByIds(ids);
        }
    }

    @Override
    public void update(T entity) {
        boolean updated = super.updateById(entity);
        if (!updated) {
            throw new ServiceException("数据更新失败");
        }
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void updateAll(Collection<T> entityList, int batchSize) {
        super.updateBatchById(entityList, batchSize);
    }
}
