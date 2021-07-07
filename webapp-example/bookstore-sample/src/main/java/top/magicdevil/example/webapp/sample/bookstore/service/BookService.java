package top.magicdevil.example.webapp.sample.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.IBookDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.BookType;
import top.magicdevil.example.webapp.sample.bookstore.entity.Book;
import top.magicdevil.example.webapp.sample.bookstore.entity.Publisher;

@Service
public class BookService extends SpringAbstractService<Long, Book, IBookDAO> {

    private static final long serialVersionUID = 1L;

    public Long getCountByPublisher(Publisher publisher) throws Exception {
        return this.getDao().getCountByPublisherID(publisher.getPid());
    }

    public Long getCountByBookType(BookType booktype) throws Exception {
        return this.getDao().getCountByBookTypeID(booktype.getTid());
    }

    public Long getCountLikeBname(String bname) throws Exception {
        return this.getDao().getCountLikeBname(bname);
    }

    public Long getCountLikeISBN(String isbn) throws Exception {
        return this.getDao().getCountLikeISBN(isbn);
    }

    public Long getCountLikeKeyword(String keyword) throws Exception {
        return this.getDao().getCountLikeKeyword(keyword);
    }

    public List<Book> getByPublisherAndSplit(
            Publisher publisher,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByPublisherIDAndSplit(publisher.getPid(),
                (currentPage - 1) * pageSize, pageSize);
    }

    public List<Book> getByBookTypeAndSplit(
            BookType booktype,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByBookTypeIDAndSplit(booktype.getTid(),
                (currentPage - 1) * pageSize, pageSize);
    }

    public List<Book> getLikeBnameAndSplit(
            String bname,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findLikeBnameAndSplit(bname, (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<Book> getLikeISBNAndSplit(
            String isbn,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findLikeISBNAndSplit(isbn, (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<Book> getLikeKeywordAndSplit(
            String keyword,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findLikeKeywordAndSplit(keyword, (currentPage - 1) * pageSize,
                pageSize);
    }
}
