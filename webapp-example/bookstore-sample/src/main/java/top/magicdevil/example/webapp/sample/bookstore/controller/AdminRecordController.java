package top.magicdevil.example.webapp.sample.bookstore.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import top.magicdevil.example.webapp.sample.bookstore.service.UserBookVisitService;

@Controller
@RequestMapping("/admin/record")
public class AdminRecordController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserBookVisitService ubvService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/admin/record/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("recordList", this.ubvService.getAllBySpilt(currentPage, pageSize));
            mav.addObject("allCount", this.ubvService.getAllCount());
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/record/record_list.html");
        }
        return mav;

    }

}
