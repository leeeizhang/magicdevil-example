package top.magicdevil.example.webapp.sample.bookstore.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import top.magicdevil.example.webapp.sample.bookstore.entity.User;
import top.magicdevil.example.webapp.sample.bookstore.service.UserBookVisitService;
import top.magicdevil.example.webapp.sample.bookstore.entity.Book;
import top.magicdevil.example.webapp.sample.bookstore.entity.BookType;
import top.magicdevil.example.webapp.sample.bookstore.entity.Publisher;
import top.magicdevil.example.webapp.sample.bookstore.entity.UserBookVisit;
import top.magicdevil.example.webapp.sample.bookstore.service.BookService;
import top.magicdevil.example.webapp.sample.bookstore.service.BookTypeService;
import top.magicdevil.example.webapp.sample.bookstore.service.PublisherService;
import top.magicdevil.example.webapp.sample.bookstore.service.RecommendationService;
import top.magicdevil.example.webapp.sample.bookstore.service.UserService;

@Controller
@RequestMapping("/user/shop")
public class UserBookVisitController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private BookService bookService;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private BookTypeService booktypeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendationService recService;

    @Autowired
    private UserBookVisitService userBookVisitService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/user/shop/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = { RequestMethod.GET })
    public ModelAndView list(
            @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
            @RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "12") Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {

            List<Book> bookList = new ArrayList<>();
            Long allCount = 0L;

            if (!searchType.isEmpty() && !searchValue.isEmpty()) {
                switch (searchType) {
                case "bid":
                    bookList.add(bookService.getByID(Long.valueOf(searchValue)));
                    allCount = 1L;
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
                case "keyword":
                    bookList.addAll(bookService.getLikeKeywordAndSplit(searchValue,
                            currentPage, pageSize));
                    allCount = bookService.getCountLikeKeyword(searchValue);
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
                    bookList.addAll(bookService.getAllBySpilt(currentPage, pageSize));
                    allCount = bookService.getAllCount();
                }
            } else {
                bookList.addAll(bookService.getAllBySpilt(currentPage, pageSize));
                allCount = bookService.getAllCount();
            }

            if (currentPage == 1 && searchType.isEmpty()) {
                mav.addObject("recBookList",
                        recService.getRecsByAlsFunc((User) session.getAttribute("user")));
            }
            mav.addObject("bookList", bookList);
            mav.addObject("publisherList", publisherService.getAll());
            mav.addObject("booktypeList", booktypeService.getAll());
            mav.addObject("searchType", searchType);
            mav.addObject("searchValue", searchValue);
            mav.addObject("allCount", allCount);
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/shop/book_list.html");
        }
        return mav;

    }

    @RequestMapping(value = "/{bid}", method = { RequestMethod.GET })
    public ModelAndView visit(
            @PathVariable("bid") Long bid,
            HttpSession session, ModelAndView mav) {
        try {
            Book book = bookService.getByID(bid);
            User user = userService.getByID(((User) session.getAttribute("user")).getUid());
            UserBookVisit dao = new UserBookVisit();
            {
                dao.setBook(book);
                dao.setUser(user);
                dao.setVtime(new Date());
            }
            userBookVisitService.insert(dao);
            mav.addObject("book", book);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/shop/book_detail.html");
        }
        return mav;
    }

}
