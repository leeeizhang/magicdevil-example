package top.magicdevil.example.webapp.sample.bookstore.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import top.magicdevil.example.webapp.sample.bookstore.entity.User;
import top.magicdevil.example.webapp.sample.bookstore.entity.Career;
import top.magicdevil.example.webapp.sample.bookstore.entity.Degree;
import top.magicdevil.example.webapp.sample.bookstore.service.CareerService;
import top.magicdevil.example.webapp.sample.bookstore.service.DegreeService;
import top.magicdevil.example.webapp.sample.bookstore.service.UserService;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserService userService;

    @Autowired
    private CareerService careerService;

    @Autowired
    private DegreeService degreeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/admin/user/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView list(
            @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
            @RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue,
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {

            List<User> userList = new ArrayList<>();
            Long allCount = 0L;

            if (!searchType.isEmpty() && !searchValue.isEmpty()) {
                switch (searchType) {
                case "uid":
                    User user = userService.getByID(Long.valueOf(searchValue));
                    if (user != null) {
                        userList.add(user);
                        allCount = 1L;
                    }
                    break;
                case "uname":
                    userList.addAll(userService.getLikeUsernameAndSplit(searchValue,
                            currentPage, pageSize));
                    allCount = userService.getCountLikeUsername(searchValue);
                    break;
                case "phone":
                    userList.addAll(
                            userService.getLikePhoneAndSplit(searchValue, currentPage, pageSize));
                    allCount = userService.getCountLikePhone(searchValue);
                    break;
                case "email":
                    userList.addAll(
                            userService.getLikeEmailAndSplit(searchValue, currentPage, pageSize));
                    allCount = userService.getCountLikeEmail(searchValue);
                    break;
                case "did":
                    Degree degree = degreeService.getByID(Long.valueOf(searchValue));
                    userList.addAll(
                            userService.getByDegreeAndSplit(degree, currentPage, pageSize));
                    allCount = userService.getCountByDegree(degree);
                    break;
                case "cid":
                    Career career = careerService.getByID(Long.valueOf(searchValue));
                    userList.addAll(
                            userService.getByCareerAndSplit(career, currentPage, pageSize));
                    allCount = userService.getCountByCareer(career);
                    break;
                default:
                    userList.addAll(userService.getAllBySpilt(currentPage, pageSize));
                    allCount = userService.getAllCount();
                }
            } else {
                userList.addAll(userService.getAllBySpilt(currentPage, pageSize));
                allCount = userService.getAllCount();
            }
            mav.addObject("searchType", searchType);
            mav.addObject("searchValue", searchValue);
            mav.addObject("userList", userList);
            mav.addObject("allCount", allCount);
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/user/user_list.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ModelAndView insert(
            @RequestParam(value = "insertState", required = false, defaultValue = DEFAULT_STATE) Integer insertState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("insertState", insertState);
            mav.addObject("careerList", careerService.getAll());
            mav.addObject("degreeList", degreeService.getAll());
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/user/user_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ModelAndView insert(
            @RequestParam(value = "uname", required = true) String uname,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "sex", required = false, defaultValue = "0") Integer sex,
            @RequestParam(value = "birth", required = false, defaultValue = "2000-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birth,
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "career_id", required = true) Long career_id,
            @RequestParam(value = "degree_id", required = true) Long degree_id,
            @RequestParam(value = "admin_tag", required = false, defaultValue = "0") Integer admin_tag,
            HttpSession session, ModelAndView mav) {

        User user = null;
        try {
            user = userService.register(uname, password, sex, birth, phone, email,
                    careerService.getByID(career_id), degreeService.getByID(degree_id), admin_tag);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("insertState", user == null ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/user/insert");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(
            @RequestParam(value = "uid", required = true) Long uid,
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("updateState", updateState);
            mav.addObject("user", userService.getByID(uid));
            mav.addObject("careerList", careerService.getAll());
            mav.addObject("degreeList", degreeService.getAll());
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/user/user_update.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "uid", required = true) Long uid,
            @RequestParam(value = "uname", required = true) String uname,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "sex", required = false, defaultValue = "0") Integer sex,
            @RequestParam(value = "birth", required = false, defaultValue = "2000-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birth,
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "career_id", required = true) Long career_id,
            @RequestParam(value = "degree_id", required = true) Long degree_id,
            @RequestParam(value = "admin_tag", required = false, defaultValue = "0") Integer admin_tag,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;
        try {
            User user = new User();
            {
                user.setUid(uid);
                user.setUname(uname);
                user.setPassword(password);
                user.setSex(sex);
                user.setBirth(birth);
                user.setPhone(phone);
                user.setEmail(email);
                user.setCareer(careerService.getByID(career_id));
                user.setDegree(degreeService.getByID(degree_id));
                user.setAdmin_tag(admin_tag);
            }
            updateFinish = userService.update(user);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("uid", uid);
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/user/update");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ModelAndView remove(
            @RequestParam(value = "uid", required = true) Long[] uid,
            @RequestParam(value = "deleteState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<User> userList = new ArrayList<>();
            for (Long item : uid) {
                User user = userService.getByID(item);
                if (user != null) {
                    userList.add(user);
                }
            }
            mav.addObject("userList", userList);
            mav.addObject("deleteState", deleteState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/user/user_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "uid", required = true) Long[] uid,
            HttpSession session, ModelAndView mav) {

        boolean deleteFinish = false;
        try {
            Set<User> deleteUser = new HashSet<>();
            for (Long item : uid) {
                User user = userService.getByID(item);
                if (user != null) {
                    deleteUser.add(user);
                }
            }
            deleteFinish = userService.removeBunch(deleteUser);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("uid", uid);
            mav.addObject("deleteState", !deleteFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("admin/user/user_remove.html");
        }
        return mav;

    }

}