package top.magicdevil.example.webapp.sample.bookstore.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import top.magicdevil.example.webapp.sample.bookstore.entity.OrderItem;
import top.magicdevil.example.webapp.sample.bookstore.entity.UserOrder;
import top.magicdevil.example.webapp.sample.bookstore.service.AddressService;
import top.magicdevil.example.webapp.sample.bookstore.service.BookService;
import top.magicdevil.example.webapp.sample.bookstore.service.OrderItemService;
import top.magicdevil.example.webapp.sample.bookstore.service.UserOrderService;

@Controller
@RequestMapping("/user/order")
public class UserOrderController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/user/order/list");
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "currentPage", required = false, defaultValue = CURRENT_PAGE) Integer currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) Integer pageSize,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("orderList", userOrderService
                    .getByUserAndSplit((User) session.getAttribute("user"), currentPage, pageSize));
            mav.addObject("allCount", addressService.getAllCount());
            mav.addObject("currentPage", currentPage);
            mav.addObject("pageSize", pageSize);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/order/order_list.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ModelAndView insert(
            @RequestParam(value = "bid", required = true) Long bid,
            @RequestParam(value = "insertState", required = false, defaultValue = DEFAULT_STATE) Integer insertState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("book", bookService.getByID(bid));
            mav.addObject("addressList",
                    addressService.getByUser((User) session.getAttribute("user")));
            mav.addObject("unpaidOrderList", userOrderService.getAllWithUnpaid());
            mav.addObject("insertState", insertState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/order/order_insert.html");
        }
        return mav;

    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ModelAndView insert(
            @RequestParam(value = "uoid", required = true) Long uoid,
            @RequestParam(value = "aid", required = true) Long aid,
            @RequestParam(value = "bid", required = true) Long bid,
            @RequestParam(value = "num", required = true) Long num,
            HttpSession session, ModelAndView mav) {

        boolean insertState = false;
        try {
            OrderItem orderitem = new OrderItem();
            {
                orderitem.setBook(bookService.getByID(bid));
                orderitem.setNum(num);
                if (uoid != -1) {
                    orderitem.setUserorder(userOrderService.getByID(uoid));
                } else {
                    UserOrder userOrder = new UserOrder();
                    {
                        userOrder.setAddress(addressService.getByID(aid));
                        userOrder.setPaid(0);
                        userOrder.setUser((User) session.getAttribute("user"));
                    }
                    userOrderService.insert(userOrder);
                    orderitem.setUserorder(userOrder);
                }
            }
            insertState = orderItemService.insert(orderitem);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("insertState", !insertState ? ERROR_STATE : SUCCESS_STATE);
            mav.addObject("bid", bid);
            mav.setViewName("redirect:/user/order/insert");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(
            @RequestParam(value = "uoid", required = true) Long uoid,
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("userorder", userOrderService.getByID(uoid));
            mav.addObject("addressList",
                    addressService.getByUser((User) session.getAttribute("user")));
            mav.addObject("updateState", updateState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/order/order_update.html");
        }
        return mav;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "uoid", required = true) Long uoid,
            @RequestParam(value = "aid", required = true) Long aid,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;
        try {
            UserOrder userOrder = new UserOrder();
            {
                userOrder.setUoid(uoid);
                userOrder.setPaid(0);
                userOrder.setUser((User) session.getAttribute("user"));
                userOrder.setAddress(addressService.getByID(aid));
            }
            updateFinish = userOrderService.update(userOrder);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("uoid", uoid);
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/user/order/update");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public ModelAndView remove(
            @RequestParam(value = "uoid", required = true) Long[] uoid,
            @RequestParam(value = "deleteState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<UserOrder> orderList = new ArrayList<>();
            for (Long item : uoid) {
                orderList.add(userOrderService.getByID(item));
            }
            mav.addObject("orderList", orderList);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("deleteState", deleteState);
            mav.setViewName("user/order/order_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ModelAndView update(
            @RequestParam(value = "uoid", required = true) Long[] uoid,
            HttpSession session, ModelAndView mav) {

        boolean deleteFinish = false;
        try {
            Set<UserOrder> deleteOrder = new HashSet<>();
            for (Long item : uoid) {
                deleteOrder.add(userOrderService.getByID(item));
            }
            deleteFinish = userOrderService.removeBunch(deleteOrder);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("deleteState", !deleteFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("user/order/order_remove.html");
        }
        return mav;

    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public ModelAndView pay(
            @RequestParam(value = "uoid", required = true) Long[] uoid,
            @RequestParam(value = "payState", required = false, defaultValue = DEFAULT_STATE) Integer deleteState,
            HttpSession session, ModelAndView mav) {

        try {
            List<UserOrder> orderList = new ArrayList<>();
            Double totalPrice = 0.0D;
            for (Long item : uoid) {
                UserOrder userorder = userOrderService.getByID(item);
                orderList.add(userorder);
                totalPrice += userorder.getPrice();
            }
            mav.addObject("orderList", orderList);
            mav.addObject("totalPrice", totalPrice);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("payState", deleteState);
            mav.setViewName("user/order/order_pay.html");
        }
        return mav;

    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ModelAndView pay(
            @RequestParam(value = "uoid", required = true) Long[] uoid,
            HttpSession session, ModelAndView mav) {

        boolean paidFinish = false;
        try {
            Set<UserOrder> payOrder = new HashSet<>();
            for (Long item : uoid) {
                payOrder.add(userOrderService.getByID(item));
            }
            paidFinish = userOrderService.updateBunchWithPaid(payOrder);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("payState", !paidFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("user/order/order_pay.html");
        }
        return mav;

    }

    @RequestMapping(value = "/{uoid}", method = RequestMethod.GET)
    public ModelAndView detail(
            @PathVariable("uoid") Long uoid,
            HttpSession session, ModelAndView mav) {
        try {
            UserOrder userOrder = userOrderService.getByID(uoid);
            mav.addObject("userorder", userOrder);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("user/order/detail_list.html");
        }
        return mav;
    }

}
