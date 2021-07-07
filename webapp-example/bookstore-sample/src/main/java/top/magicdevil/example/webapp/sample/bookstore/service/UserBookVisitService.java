package top.magicdevil.example.webapp.sample.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.IUserBookVisitDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.Book;
import top.magicdevil.example.webapp.sample.bookstore.entity.User;
import top.magicdevil.example.webapp.sample.bookstore.entity.UserBookVisit;

@Service
public class UserBookVisitService
        extends SpringAbstractService<Long, UserBookVisit, IUserBookVisitDAO> {

    private static final long serialVersionUID = 1L;

    public Long getCountByUser(User user) throws Exception {
        return this.getDao().getCountByUserID(user.getUid());
    }

    public Long getCountByBook(Book book) throws Exception {
        return this.getDao().getCountByBookID(book.getBid());
    }

    public List<UserBookVisit> getByUserAndSplit(
            User user,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByUserIDAndSplit(user.getUid(),
                (currentPage - 1) * pageSize, pageSize);
    }

    public List<UserBookVisit> getByBookAndSplit(
            Book book,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByBookIDAndSplit(book.getBid(),
                (currentPage - 1) * pageSize, pageSize);
    }

}
