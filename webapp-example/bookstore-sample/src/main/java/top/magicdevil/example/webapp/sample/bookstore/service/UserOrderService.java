package top.magicdevil.example.webapp.sample.bookstore.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.entity.User;
import top.magicdevil.example.webapp.sample.bookstore.dao.IUserOrderDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.Address;
import top.magicdevil.example.webapp.sample.bookstore.entity.UserOrder;

@Service
public class UserOrderService extends SpringAbstractService<Long, UserOrder, IUserOrderDAO> {

    private static final long serialVersionUID = 1L;

    public Long getCountByUser(User user) throws Exception {
        return this.getDao().getCountByUserID(user.getUid());
    }

    public Long getCountByAddress(Address address) throws Exception {
        return this.getDao().getCountByAddressID(address.getAid());
    }

    public List<UserOrder> getByUserAndSplit(
            User user,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByUserIDAndSplit(user.getUid(), (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<UserOrder> getByAddressAndSplit(
            Address address,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByAddressIDAndSplit(address.getAid(), (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<UserOrder> getAllWithUnpaid() throws Exception {
        return this.getDao().findByPaid(false);
    }

    public boolean updateBunchWithPaid(Set<UserOrder> userorder) throws Exception {
        return this.getDao().doUpdatePaidBunch(1, userorder);
    }

}
