package top.magicdevil.example.webapp.sample.bookstore.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import top.magicdevil.example.webapp.sample.bookstore.entity.IEntity;

public interface IDAO<K, V extends IEntity> extends Serializable {

    public abstract Boolean doCreate(V vo) throws Exception;

    public abstract Long getAllCount() throws Exception;

    public abstract Long getCountByColomn(String columnName,List<String> keyword) throws Exception;

    public abstract V findByID(K id) throws Exception;

    public abstract List<V> findAll() throws Exception;

    public abstract List<V> findBunchBySplit(Integer pageSize, Integer currentPage)
            throws Exception;

    public abstract List<V> findBunchByColumn(String columnName, List<String> keyword)
            throws Exception;

    public abstract List<V> findBunchByColumnAndSplit(String columnName, List<String> keyword,
            Integer currentPage,
            Integer pageSize) throws Exception;

    public abstract Boolean doUpdate(V vo) throws Exception;

    public abstract Boolean doDeleteByID(K id) throws Exception;

    public abstract Boolean doDeleteBunch(Set<V> ids) throws Exception;

}
