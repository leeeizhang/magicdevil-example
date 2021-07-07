package top.magicdevil.example.webapp.sample.bookstore.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import top.magicdevil.example.webapp.sample.bookstore.entity.User;

public class AdminLoginInterceptor implements IInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        if (((User) request.getSession().getAttribute("user")).getAdmin_tag() != 1) {
            response.sendError(403);
            return false;
        }
        return true;
    }

}