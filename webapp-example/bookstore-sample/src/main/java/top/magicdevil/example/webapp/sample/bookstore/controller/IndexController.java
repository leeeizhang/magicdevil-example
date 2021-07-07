package top.magicdevil.example.webapp.sample.bookstore.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController implements IController {

    private static final long serialVersionUID = 1L;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/user/shop/list");
        return mav;
    }

}
