package top.magicdevil.example.webapp.sample.bookstore.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import top.magicdevil.example.webapp.sample.bookstore.entity.BookType;
import top.magicdevil.example.webapp.sample.bookstore.entity.Publisher;
import top.magicdevil.example.webapp.sample.bookstore.service.BookService;
import top.magicdevil.example.webapp.sample.bookstore.service.PublisherService;
import top.magicdevil.example.webapp.sample.bookstore.entity.Book;
import top.magicdevil.example.webapp.sample.bookstore.service.BookTypeService;

@Controller
@RequestMapping("/admin/book")
public class AdminBookController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookTypeService booktypeService;

    @Autowired
    private PublisherService publisherService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/admin/book/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
            @RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue,
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {

            List<Book> bookList = new ArrayList<>();
            Long allCount = 0L;

            if (!searchType.isEmpty() && !searchValue.isEmpty()) {
                switch (searchType) {
                case "bid":
                    Book book = bookService.getByID(Long.valueOf(searchValue));
                    if (book != null) {
                        bookList.add(book);
                        allCount = 1L;
                    }
                    break;
                case "bname":
                    bookList.addAll(bookService.getLikeBnameAndSplit(searchValue,
                            currentPage, pageSize));
                    allCount = bookService.getCountLikeBname(searchValue);
                    break;
                case "isbn":
                    bookList.addAll(bookService.getLikeISBNAndSplit(searchValue,
                            currentPage, pageSize));
                    allCount = bookService.getCountLikeISBN(searchValue);
                    break;
                case "tid":
                    BookType booktype = booktypeService.getByID(Long.valueOf(searchValue));
                    bookList.addAll(bookService.getByBookTypeAndSplit(booktype,
                            currentPage, pageSize));
                    allCount = bookService.getCountByBookType(booktype);
                    break;
                case "pid":
                    Publisher publisher = publisherService.getByID(Long.valueOf(searchValue));
                    bookList.addAll(bookService.getByPublisherAndSplit(publisher,
                            currentPage, pageSize));
                    allCount = bookService.getCountByPublisher(publisher);
                    break;
                default:
                    bookList = bookService.getAllBySpilt(currentPage, pageSize);
                    allCount = bookService.getAllCount();
                }
            } else {
                bookList = bookService.getAllBySpilt(currentPage, pageSize);
                allCount = bookService.getAllCount();
            }

            mav.addObject("searchType", searchType);
            mav.addObject("searchValue", searchValue);
            mav.addObject("bookList", bookList);
            mav.addObject("allCount", allCount);
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/book/book_list.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ModelAndView insert(
            @RequestParam(value = "insertState", required = false, defaultValue = DEFAULT_STATE) Integer insertState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("insertState", insertState);
            mav.addObject("booktypeList", booktypeService.getAll());
            mav.addObject("publisherList", publisherService.getAll());
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/book/book_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ModelAndView insert(
            @RequestParam(value = "bname", required = true) String bname,
            @RequestParam(value = "isbn", required = true) String isbn,
            @RequestParam(value = "price", required = true) Double price,
            @RequestParam(value = "stock", required = true) Integer stock,
            @RequestParam(value = "pic", required = true) String pic,
            @RequestParam(value = "booktype_id", required = true) Long booktype_id,
            @RequestParam(value = "publisher_id", required = true) Long publisher_id,
            HttpSession session, ModelAndView mav) {

        boolean insertState = false;
        try {
            Book book = new Book();
            {
                book.setBname(bname);
                book.setIsbn(isbn);
                book.setPrice(price);
                book.setStock(stock);
                book.setPic(pic);
                book.setBooktype(booktypeService.getByID(booktype_id));
                book.setPublisher(publisherService.getByID(publisher_id));
            }
            insertState = bookService.insert(book);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("insertState", !insertState ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/book/insert");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(
            @RequestParam(value = "bid", required = true) Long bid,
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("updateState", updateState);
            mav.addObject("book", bookService.getByID(bid));
            mav.addObject("booktypeList", booktypeService.getAll());
            mav.addObject("publisherList", publisherService.getAll());
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/book/book_update.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "bid", required = true) Long bid,
            @RequestParam(value = "bname", required = true) String bname,
            @RequestParam(value = "isbn", required = true) String isbn,
            @RequestParam(value = "price", required = true) Double price,
            @RequestParam(value = "stock", required = true) Integer stock,
            @RequestParam(value = "pic", required = true) String pic,
            @RequestParam(value = "booktype_id", required = true) Long booktype_id,
            @RequestParam(value = "publisher_id", required = true) Long publisher_id,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;
        try {
            Book book = new Book();
            {
                book.setBid(bid);
                book.setBname(bname);
                book.setIsbn(isbn);
                book.setPrice(price);
                book.setStock(stock);
                book.setPic(pic);
                book.setBooktype(booktypeService.getByID(booktype_id));
                book.setPublisher(publisherService.getByID(publisher_id));
            }
            updateFinish = bookService.update(book);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("bid", bid);
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/book/update");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ModelAndView remove(
            @RequestParam(value = "bid", required = true) Long[] bid,
            @RequestParam(value = "deleteState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<Book> bookList = new ArrayList<>();
            for (Long item : bid) {
                Book book = bookService.getByID(item);
                if (book != null) {
                    bookList.add(book);
                }
            }
            mav.addObject("bookList", bookList);
            mav.addObject("deleteState", deleteState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/book/book_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "bid", required = true) Long[] bid,
            HttpSession session, ModelAndView mav) {

        boolean deleteFinish = false;
        try {
            Set<Book> deleteBook = new HashSet<>();
            for (Long item : bid) {
                Book book = bookService.getByID(item);
                if (book != null) {
                    deleteBook.add(book);
                }
            }
            deleteFinish = bookService.removeBunch(deleteBook);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("bid", bid);
            mav.addObject("deleteState", !deleteFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/book/remove");
        }
        return mav;

    }

}