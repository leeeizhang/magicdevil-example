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

import top.magicdevil.example.webapp.sample.bookstore.entity.Address;
import top.magicdevil.example.webapp.sample.bookstore.entity.User;
import top.magicdevil.example.webapp.sample.bookstore.service.AddressService;

@Controller
@RequestMapping("/user/address")
public class UserAddressController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/user/address/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {
            User user = (User) session.getAttribute("user");
            mav.addObject("addressList",
                    addressService.getByUserAndSplit(user, currentPage, pageSize));
            mav.addObject("allCount", addressService.getAllCount());
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/address/address_list.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ModelAndView insert(
            HttpSession session, ModelAndView mav) {

        try {
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/address/address_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ModelAndView insert(
            @RequestParam(value = "location", required = true) String location,
            HttpSession session, ModelAndView mav) {
        boolean insertFinish = false;
        try {
            Address address = new Address();
            address.setUser((User) session.getAttribute("user"));
            address.setLocation(location);
            insertFinish = addressService.insert(address);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("insertState", !insertFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("user/address/address_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(
            @RequestParam(value = "aid", required = true) Long aid,
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("updateState", updateState);
            mav.addObject("address", addressService.getByID(aid));
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/address/address_update.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "aid", required = true) Long aid,
            @RequestParam(value = "location", required = true) String location,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;
        try {
            Address address = new Address();
            address.setAid(aid);
            address.setUser((User) session.getAttribute("user"));
            address.setLocation(location);
            updateFinish = addressService.update(address);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("aid", aid);
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/user/address/update");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ModelAndView remove(
            @RequestParam(value = "aid", required = true) Long[] aid,
            @RequestParam(value = "deleteState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<Address> addressList = new ArrayList<>();
            for (Long item : aid) {
                Address address = addressService.getByID(item);
                if (address != null) {
                    addressList.add(address);
                }
            }
            mav.addObject("addressList", addressList);
            mav.addObject("deleteState", deleteState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/address/address_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "aid", required = true) Long[] aid,
            HttpSession session, ModelAndView mav) {

        boolean deleteFinish = false;
        try {
            Set<Address> deleteAddress = new HashSet<>();
            for (Long item : aid) {
                Address address = addressService.getByID(item);
                if (address != null) {
                    deleteAddress.add(address);
                }
            }
            deleteFinish = addressService.removeBunch(deleteAddress);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("aid", aid);
            mav.addObject("deleteState", !deleteFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/user/address/remove");
        }
        return mav;

    }

}