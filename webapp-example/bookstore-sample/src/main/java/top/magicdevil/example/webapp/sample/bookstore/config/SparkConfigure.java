package top.magicdevil.example.webapp.sample.bookstore.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import top.magicdevil.example.webapp.sample.bookstore.spark.config.ALSConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.SourceConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.SparkConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.function.ALSRecommendationFunction;

@Configuration
public class SparkConfigure implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_TABLE = "bookstore.UserBookStatistic";

    @Value("${db.url}")
    private String dbURL;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${spark.master}")
    private String master;

    @Value("${spark.appname}")
    private String appname;

    @Value("${spark.panel-url}")
    private String panelURL;

    @Value("${spark.als.maxIter}")
    private Integer maxIter;

    @Value("${spark.als.rank}")
    private Integer rank;

    @Value("${spark.als.numBlocks}")
    private Integer numBlocks;

    @Value("${spark.als.alpha}")
    private Double alpha;

    @Value("${spark.als.alpha}")
    private Double lambda;

    @Value("${spark.als.regParm}")
    private Double regParm;

    @Value("${spark.als.recNum}")
    private Integer recNum;

    @Bean("SparkConfig")
    public SparkConfig sparkSession() throws Exception {
        SparkConfig config = new SparkConfig();
        {
            config.setMaster(master);
            config.setAppName(appname);
            config.setPanelURL(panelURL);
        }
        return config;
    }

    @Bean("SourceConfig")
    public SourceConfig sourceConfig() throws Exception {
        SourceConfig config = new SourceConfig();
        {
            config.setDBDriver(DB_DRIVER);
            config.setDBURL(dbURL);
            config.setUsername(username);
            config.setPassword(password);
            config.setTable(DB_TABLE);
            config.setUserCol("uid");
            config.setItemCol("bid");
            config.setRateCol("rate");
        }
        return config;
    }

    @Bean("ALSConfig")
    public ALSConfig alsConfig() throws Exception {
        ALSConfig alsConfig = new ALSConfig();
        {
            alsConfig.setMaxIter(maxIter);
            alsConfig.setRank(rank);
            alsConfig.setNumBlocks(numBlocks);
            alsConfig.setAlpha(alpha);
            alsConfig.setLambda(lambda);
            alsConfig.setRegParam(regParm);
            alsConfig.setRecNum(recNum);
        }
        return alsConfig;
    }

    @Bean("ALSRecommendationFunction")
    public ALSRecommendationFunction alsRecommendationFunction(
            @Autowired SparkConfig sparkConfig,
            @Autowired SourceConfig sourceConfig,
            @Autowired ALSConfig alsConfig) throws Exception {
        return ALSRecommendationFunction.Builder(sparkConfig, sourceConfig, alsConfig);
    }

}
