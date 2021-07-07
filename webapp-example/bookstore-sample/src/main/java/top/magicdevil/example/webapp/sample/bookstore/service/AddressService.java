package top.magicdevil.example.webapp.sample.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.IAddressDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.Address;
import top.magicdevil.example.webapp.sample.bookstore.entity.User;

@Service
public class AddressService extends SpringAbstractService<Long, Address, IAddressDAO> {

    private static final long serialVersionUID = 1L;

    public Long getCountByUser(User user) throws Exception {
        return this.getDao().getCountByUserID(user.getUid());
    }

    public List<Address> getByUser(User user) throws Exception {
        return this.getDao().findByUserID(user.getUid());
    }

    public List<Address> getByUserAndSplit(
            User user,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByUserIDAndSplit(user.getUid(), (currentPage - 1) * pageSize,
                pageSize);
    }
}
