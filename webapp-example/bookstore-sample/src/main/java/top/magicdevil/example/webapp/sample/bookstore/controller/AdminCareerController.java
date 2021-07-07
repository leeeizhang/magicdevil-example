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

import top.magicdevil.example.webapp.sample.bookstore.service.CareerService;
import top.magicdevil.example.webapp.sample.bookstore.entity.Career;

@Controller
@RequestMapping("/admin/career")
public class AdminCareerController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CareerService careerService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/admin/career/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("careerList", careerService.getAllBySpilt(currentPage, pageSize));
            mav.addObject("allCount", careerService.getAllCount());
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/career/career_list.html");
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
            mav.setViewName("admin/career/career_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ModelAndView insert(
            @RequestParam(value = "cname", required = true) String cname,
            HttpSession session, ModelAndView mav) {
        boolean insertFinish = false;
        try {
            Career career = new Career();
            career.setCname(cname);
            insertFinish = careerService.insert(career);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("insertState", !insertFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/career/insert");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(
            @RequestParam(value = "cid", required = true) Long cid,
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("updateState", updateState);
            mav.addObject("career", careerService.getByID(cid));
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/career/career_update.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "cid", required = true) Long cid,
            @RequestParam(value = "cname", required = true) String cname,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;
        try {
            Career career = new Career();
            career.setCid(cid);
            career.setCname(cname);
            updateFinish = careerService.update(career);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("cid", cid);
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/career/update");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ModelAndView remove(
            @RequestParam(value = "cid", required = true) Long[] cid,
            @RequestParam(value = "deleteState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<Career> careerList = new ArrayList<>();
            for (Long item : cid) {
                Career career = careerService.getByID(item);
                if (career != null) {
                    careerList.add(career);
                }
            }
            mav.addObject("careerList", careerList);
            mav.addObject("deleteState", deleteState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/career/career_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "cid", required = true) Long[] cid,
            HttpSession session, ModelAndView mav) {

        boolean deleteFinish = false;
        try {
            Set<Career> deleteCareer = new HashSet<>();
            for (Long item : cid) {
                Career career = careerService.getByID(item);
                if (career != null) {
                    deleteCareer.add(career);
                }
            }
            deleteFinish = careerService.removeBunch(deleteCareer);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("cid", cid);
            mav.addObject("deleteState", !deleteFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/career/remove");
        }
        return mav;

    }

}