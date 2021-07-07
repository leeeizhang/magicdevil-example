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
import top.magicdevil.example.webapp.sample.bookstore.service.BookTypeService;

@Controller
@RequestMapping("/admin/booktype")
public class AdminBooktypeController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private BookTypeService booktypeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/admin/booktype/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("booktypeList", booktypeService.getAllBySpilt(currentPage, pageSize));
            mav.addObject("allCount", booktypeService.getAllCount());
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/booktype/booktype_list.html");
        }

        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ModelAndView insert(
            @RequestParam(value = "insertState", required = false, defaultValue = DEFAULT_STATE) Integer insertState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("insertState", insertState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/booktype/booktype_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ModelAndView insert(
            @RequestParam(value = "tname", required = true) String tname,
            HttpSession session, ModelAndView mav) {
        boolean insertFinish = false;
        try {
            BookType booktype = new BookType();
            booktype.setTname(tname);
            insertFinish = booktypeService.insert(booktype);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("insertState", !insertFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/booktype/insert");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(
            @RequestParam(value = "tid", required = true) Long tid,
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("updateState", updateState);
            mav.addObject("booktype", booktypeService.getByID(tid));
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/booktype/booktype_update.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "tid", required = true) Long tid,
            @RequestParam(value = "tname", required = true) String tname,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;
        try {
            BookType booktype = new BookType();
            booktype.setTid(tid);
            booktype.setTname(tname);
            updateFinish = booktypeService.update(booktype);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("tid", tid);
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/booktype/update");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ModelAndView remove(
            @RequestParam(value = "tid", required = true) Long[] tid,
            @RequestParam(value = "deleteState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<BookType> booktypeList = new ArrayList<>();
            for (Long item : tid) {
                BookType bookType = booktypeService.getByID(item);
                if (bookType != null) {
                    booktypeList.add(bookType);
                }
            }
            mav.addObject("booktypeList", booktypeList);
            mav.addObject("deleteState", deleteState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/booktype/booktype_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "tid", required = true) Long[] tid,
            HttpSession session, ModelAndView mav) {

        boolean deleteFinish = false;
        try {
            Set<BookType> deleteBooktype = new HashSet<>();
            for (Long item : tid) {
                BookType bookType = booktypeService.getByID(item);
                if (bookType != null) {
                    deleteBooktype.add(bookType);
                }
            }
            deleteFinish = booktypeService.removeBunch(deleteBooktype);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("tid", tid);
            mav.addObject("deleteState", !deleteFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/booktype/remove");
        }
        return mav;

    }

}