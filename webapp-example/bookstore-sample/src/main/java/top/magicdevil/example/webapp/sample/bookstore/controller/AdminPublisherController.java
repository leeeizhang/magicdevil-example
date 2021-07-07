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

import top.magicdevil.example.webapp.sample.bookstore.entity.Publisher;
import top.magicdevil.example.webapp.sample.bookstore.service.PublisherService;

@Controller
@RequestMapping("/admin/publisher")
public class AdminPublisherController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PublisherService publisherService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/admin/publisher/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("publisherList", publisherService.getAllBySpilt(currentPage, pageSize));
            mav.addObject("allCount", publisherService.getAllCount());
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/publisher/publisher_list.html");
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
            mav.setViewName("admin/publisher/publisher_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ModelAndView insert(
            @RequestParam(value = "pname", required = true) String pname,
            HttpSession session, ModelAndView mav) {
        boolean insertFinish = false;
        try {
            Publisher publisher = new Publisher();
            publisher.setPname(pname);
            insertFinish = publisherService.insert(publisher);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("insertState", !insertFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/publisher/insert");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(
            @RequestParam(value = "pid", required = true) Long pid,
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("updateState", updateState);
            mav.addObject("publisher", publisherService.getByID(pid));
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/publisher/publisher_update.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "pid", required = true) Long pid,
            @RequestParam(value = "pname", required = true) String pname,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;
        try {
            Publisher publisher = new Publisher();
            publisher.setPid(pid);
            publisher.setPname(pname);
            updateFinish = publisherService.update(publisher);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("pid", pid);
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/publisher/update");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ModelAndView remove(
            @RequestParam(value = "pid", required = true) Long[] pid,
            @RequestParam(value = "deleteState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<Publisher> publisherList = new ArrayList<>();
            for (Long item : pid) {
                Publisher publisher = publisherService.getByID(item);
                if (publisher != null) {
                    publisherList.add(publisher);
                }
            }
            mav.addObject("publisherList", publisherList);
            mav.addObject("deleteState", deleteState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/publisher/publisher_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "pid", required = true) Long[] pid,
            HttpSession session, ModelAndView mav) {

        boolean deleteFinish = false;
        try {
            Set<Publisher> deletePublisher = new HashSet<>();
            for (Long item : pid) {
                Publisher publisher = publisherService.getByID(item);
                if (publisher != null) {
                    deletePublisher.add(publisher);
                }
            }
            deleteFinish = publisherService.removeBunch(deletePublisher);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("pid", pid);
            mav.addObject("deleteState", !deleteFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/publisher/remove");
        }
        return mav;

    }

}