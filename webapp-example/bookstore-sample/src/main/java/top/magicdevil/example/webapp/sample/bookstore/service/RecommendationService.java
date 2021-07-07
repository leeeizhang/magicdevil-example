package top.magicdevil.example.webapp.sample.bookstore.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.entity.Book;
import top.magicdevil.example.webapp.sample.bookstore.entity.User;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.ALSConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.SourceConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.SparkConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.function.ALSRecommendationFunction;

@Service
public class RecommendationService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private BookService bookService;

    private ALSRecommendationFunction alsFunc;

    @Autowired
    private void initAlsFunc(
            BookService bookService,
            SparkConfig sparkConfig,
            SourceConfig sourceConfig,
            ALSConfig alsConfig) throws Exception {
        this.alsFunc = ALSRecommendationFunction.Builder(
                sparkConfig,
                sourceConfig,
                alsConfig);
    }

    public void updateAlsFunc() throws Exception {
        this.alsFunc.update();
    }

    public List<Book> getRecsByAlsFunc(User user) throws Exception {
        List<Book> recs = new ArrayList<>(128);
        Iterator<Entry<Long, Double>> ite = this.alsFunc
                .recommendByUser(user.getUid()).entrySet().iterator();
        while (ite.hasNext()) {
            Entry<Long, Double> item = ite.next();
            recs.add(this.bookService.getByID(item.getKey()));
        }
        return recs;
    }

}
