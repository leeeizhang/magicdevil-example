package top.magicdevil.example.webapp.sample.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.IOrderItemDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.OrderItem;
import top.magicdevil.example.webapp.sample.bookstore.entity.Book;
import top.magicdevil.example.webapp.sample.bookstore.entity.UserOrder;

@Service
public class OrderItemService extends SpringAbstractService<Long, OrderItem, IOrderItemDAO> {

    private static final long serialVersionUID = 1L;

    public Long getCountByBook(Book book) throws Exception {
        return this.getDao().getCountByBookID(book.getBid());
    }

    public Long getCountByUserOrder(UserOrder userorder) throws Exception {
        return this.getDao().getCountByUserOrderID(userorder.getUoid());
    }

    public List<OrderItem> getByBookAndSplit(
            Book book,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByBookIDAndSplit(book.getBid(), (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<OrderItem> getByUserOrderAndSplit(
            UserOrder userorder,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByUserOrderIDAndSplit(userorder.getUoid(),
                (currentPage - 1) * pageSize, pageSize);
    }

}
