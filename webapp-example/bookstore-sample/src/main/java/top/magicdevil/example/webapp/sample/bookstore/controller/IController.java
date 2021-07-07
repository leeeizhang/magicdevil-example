package top.magicdevil.example.webapp.sample.bookstore.controller;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import top.magicdevil.example.webapp.sample.bookstore.entity.User;

public interface IController extends Serializable {

    public static final String PAGE_SIZE = "10";
    public static final String CURRENT_PAGE = "1";

    public static final String DEFAULT_STATE = "0";
    public static final String ERROR_STATE = "1";
    public static final String SUCCESS_STATE = "2";

    default void setCurrentUser(HttpSession session, User user) {
        session.setAttribute("user", user);
    }

    default User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

}