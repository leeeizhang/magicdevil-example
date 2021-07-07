package top.magicdevil.example.webapp.sample.bookstore.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface IService<K, V> extends Serializable {

    public abstract boolean insert(V vo) throws Exception;

    public abstract long getAllCount() throws Exception;

    public abstract V getByID(K id) throws Exception;

    public abstract List<V> getAll() throws Exception;

    public abstract List<V> getAllBySpilt(int pageSize, int currentPage) throws Exception;

    public abstract boolean update(V vo) throws Exception;

    public abstract boolean removeByID(K id) throws Exception;

    public abstract boolean removeBunch(Set<V> ids) throws Exception;

}
