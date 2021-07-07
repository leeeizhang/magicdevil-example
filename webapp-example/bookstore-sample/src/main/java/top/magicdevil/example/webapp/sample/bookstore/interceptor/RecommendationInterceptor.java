package top.magicdevil.example.webapp.sample.bookstore.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import top.magicdevil.example.webapp.sample.bookstore.service.RecommendationService;

public class RecommendationInterceptor implements IInterceptor {

    private static final long serialVersionUID = 1L;

    @Autowired
    private RecommendationService recService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        this.recService.updateAlsFunc();
    }

}
