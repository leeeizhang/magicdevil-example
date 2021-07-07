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

import top.magicdevil.example.webapp.sample.bookstore.entity.Degree;
import top.magicdevil.example.webapp.sample.bookstore.service.DegreeService;

@Controller
@RequestMapping("/admin/degree")
public class AdminDegreeController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private DegreeService degreeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/admin/degree/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("degreeList", degreeService.getAllBySpilt(currentPage, pageSize));
            mav.addObject("allCount", degreeService.getAllCount());
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/degree/degree_list.html");
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
            mav.setViewName("admin/degree/degree_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ModelAndView insert(
            @RequestParam(value = "dname", required = true) String dname,
            HttpSession session, ModelAndView mav) {
        boolean insertFinish = false;
        try {
            Degree degree = new Degree();
            degree.setDname(dname);
            insertFinish = degreeService.insert(degree);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("insertState", !insertFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/degree/insert");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(
            @RequestParam(value = "did", required = true) Long did,
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("updateState", updateState);
            mav.addObject("degree", degreeService.getByID(did));
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/degree/degree_update.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "did", required = true) Long did,
            @RequestParam(value = "dname", required = true) String dname,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;
        try {
            Degree degree = new Degree();
            degree.setDid(did);
            degree.setDname(dname);
            updateFinish = degreeService.update(degree);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("did", did);
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/degree/update");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ModelAndView remove(
            @RequestParam(value = "did", required = true) Long[] did,
            @RequestParam(value = "deleteState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<Degree> degreeList = new ArrayList<>();
            for (Long item : did) {
                Degree degree = degreeService.getByID(item);
                if (degree != null) {
                    degreeList.add(degreeService.getByID(item));
                }
            }
            mav.addObject("degreeList", degreeList);
            mav.addObject("deleteState", deleteState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/degree/degree_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "did", required = true) Long[] did,
            HttpSession session, ModelAndView mav) {

        boolean deleteFinish = false;
        try {
            Set<Degree> deleteDegree = new HashSet<>();
            for (Long item : did) {
                Degree degree = degreeService.getByID(item);
                if (degree != null) {
                    deleteDegree.add(degree);
                }
            }
            deleteFinish = degreeService.removeBunch(deleteDegree);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("did", did);
            mav.addObject("deleteState", !deleteFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/degree/remove");
        }
        return mav;

    }

}