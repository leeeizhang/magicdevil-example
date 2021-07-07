package top.magicdevil.example.webapp.sample.bookstore.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import top.magicdevil.example.webapp.sample.bookstore.spark.config.ALSConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.SparkConfig;

@Controller
@RequestMapping("/admin/als")
public class AdminALSController implements IController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ALSConfig alsConfig;

    @Autowired
    private SparkConfig sparkConfig;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpSession session, ModelAndView mav) {
        mav.setViewName("redirect:/admin/als/panel");
        return mav;
    }

    @RequestMapping(value = "/panel", method = RequestMethod.GET)
    public ModelAndView panel(
            @RequestParam(value = "updateState", required = false, defaultValue = DEFAULT_STATE) Integer updateState,
            HttpSession session, ModelAndView mav) {

        try {
            mav.addObject("alsConfig", alsConfig);
            mav.addObject("panelURL", sparkConfig.getPanelURL());
            mav.addObject("updateState", updateState);
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.setViewName("admin/als/als_panel.html");
        }
        return mav;

    }

    @RequestMapping(value = "/panel", method = RequestMethod.POST)
    public ModelAndView panel(
            @RequestParam(value = "maxIter", required = true) Integer maxIter,
            @RequestParam(value = "rank", required = true) Integer rank,
            @RequestParam(value = "numBlocks", required = true) Integer numBlocks,
            @RequestParam(value = "alpha", required = true) Double alpha,
            @RequestParam(value = "lambda", required = true) Double lambda,
            @RequestParam(value = "regParam", required = true) Double regParam,
            @RequestParam(value = "recNum", required = true) Integer recNum,
            HttpSession session, ModelAndView mav) {

        boolean updateFinish = false;

        try {
            alsConfig.setMaxIter(maxIter);
            alsConfig.setRank(rank);
            alsConfig.setNumBlocks(numBlocks);
            alsConfig.setAlpha(alpha);
            alsConfig.setRegParam(regParam);
            alsConfig.setRecNum(recNum);
            mav.addObject("alsConfig", alsConfig);
            updateFinish = true;
        } catch (Exception e) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            mav.addObject("updateState", !updateFinish ? ERROR_STATE : SUCCESS_STATE);
            mav.setViewName("redirect:/admin/als/panel");
        }
        return mav;

    }

}
