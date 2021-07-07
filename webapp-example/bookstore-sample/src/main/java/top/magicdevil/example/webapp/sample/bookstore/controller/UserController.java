package top.magicdevil.example.webapp.sample.bookstore.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import top.magicdevil.example.webapp.sample.bookstore.entity.User;
import top.magicdevil.example.webapp.sample.bookstore.service.CareerService;
import top.magicdevil.example.webapp.sample.bookstore.service.DegreeService;
import top.magicdevil.example.webapp.sample.bookstore.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CareerService careerService;

    @Autowired
    private DegreeService degreeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/user/shop/list");
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "loginFail", required = false, defaultValue = "false") Boolean loginFail,
            HttpSession session, ModelAndView mav) throws Exception {

        mav.addObject("loginFail", loginFail);
        mav.setViewName("user/login.html");
        return mav;

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpSession session, ModelAndView mav) throws Exception {

        User user = userService.login(username, password);
        if (user != null && user.getPassword().matches(password)) {
            session.setAttribute("user", user);
            mav.setViewName("redirect:/user/shop/list");
        } else {
            session.removeAttribute("user");
            mav.addObject("loginFail", Boolean.TRUE);
            mav.setViewName("redirect:/user/login");
        }
        return mav;

    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(
            @RequestParam(value = "registerFail", required = false, defaultValue = "false") Boolean registerFail,
            HttpSession session, ModelAndView mav) throws Exception {

        mav.addObject("registerFail", registerFail);
        mav.addObject("careerList", careerService.getAll());
        mav.addObject("degreeList", degreeService.getAll());
        mav.setViewName("user/register.html");
        return mav;

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(
            @RequestParam(value = "uname", required = true) String uname,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "sex", required = false, defaultValue = "0") Integer sex,
            @RequestParam(value = "birth", required = false, defaultValue = "2000-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birth,
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "career_id", required = true) Long career_id,
            @RequestParam(value = "degree_id", required = true) Long degree_id,
            @RequestParam(value = "admin_tag", required = false, defaultValue = "0") Integer admin_tag,
            HttpSession session, ModelAndView mav) throws Exception {

        User user = userService.register(uname, password, sex, birth, phone, email,
                careerService.getByID(career_id), degreeService.getByID(degree_id), admin_tag);
        if (user != null) {
            session.setAttribute("user", user);
            mav.setViewName("redirect:/user/shop/list");
        } else {
            session.removeAttribute("user");
            mav.addObject("registerFail", Boolean.TRUE);
            mav.setViewName("redirect:/user/register");
        }

        return mav;
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView logout(HttpSession session, ModelAndView mav) throws Exception {

        session.removeAttribute("user");
        mav.setViewName("redirect:/user/login");

        return mav;
    }

}