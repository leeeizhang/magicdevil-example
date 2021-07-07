package top.magicdevil.example.webapp.sample.bookstore.service;

import java.util.List;
import java.util.Set;

import top.magicdevil.example.webapp.sample.bookstore.entity.IEntity;
import top.magicdevil.example.webapp.sample.bookstore.dao.IDAO;

public abstract class AbstractService<K, V extends IEntity, D extends IDAO<K, V>>
        implements IService<K, V> {

    private static final long serialVersionUID = 1L;

    private D dao;

    public D getDao() {
        return dao;
    }

    public void setDao(D dao) {
        this.dao = dao;
    }

    @Override
    public boolean insert(V vo) throws Exception {
        return dao.doCreate(vo);
    }

    @Override
    public long getAllCount() throws Exception {
        return dao.getAllCount();
    }

    @Override
    public V getByID(K id) throws Exception {
        return dao.findByID(id);
    }

    @Override
    public List<V> getAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public List<V> getAllBySpilt(int currentPage, int pageSize) throws Exception {
        return dao.findBunchBySplit((pageSize - 1) * currentPage, currentPage);
    }

    @Override
    public boolean update(V vo) throws Exception {
        return dao.doUpdate(vo);
    }

    @Override
    public boolean removeByID(K id) throws Exception {
        return dao.doDeleteByID(id);
    }

    @Override
    public boolean removeBunch(Set<V> ids) throws Exception {
        return dao.doDeleteBunch(ids);
    }

}
