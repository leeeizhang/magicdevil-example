package top.magicdevil.example.webapp.sample.bookstore.spark.function;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import scala.collection.mutable.WrappedArray;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.ALSConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.SourceConfig;
import top.magicdevil.example.webapp.sample.bookstore.spark.config.SparkConfig;

public class ALSRecommendationFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    private volatile SparkConfig sparkConfig;
    private volatile SourceConfig sourceConfig;
    private volatile ALSConfig alsConfig;

    private volatile SparkSession spark;
    private volatile ALSModel model;
    private volatile Dataset<Row> recs;

    private ALSRecommendationFunction() {
    }

    private void initSparkSession() {
        this.spark = SparkSession.builder()
                .config(sparkConfig.generateSparkConf())
                .getOrCreate();
    }

    private DataFrameReader sourceReader() {
        DataFrameReader sourceReader = spark.read().format("jdbc");
        {
            sourceReader.option("driver", sourceConfig.getDBDriver());
            sourceReader.option("url", sourceConfig.getDBURL());
            sourceReader.option("user", sourceConfig.getUsername());
            sourceReader.option("password", sourceConfig.getPassword());
            sourceReader.option("dbtable", sourceConfig.getTable());
        }
        return sourceReader;
    }

    public static ALSRecommendationFunction Builder(
            SparkConfig sparkConfig,
            SourceConfig sourceConfig,
            ALSConfig alsConfig) throws Exception {
        ALSRecommendationFunction alsfunction = new ALSRecommendationFunction();
        {
            alsfunction.sparkConfig = sparkConfig;
            alsfunction.sourceConfig = sourceConfig;
            alsfunction.alsConfig = alsConfig;
            alsfunction.initSparkSession();
            alsfunction.update();
        }
        return alsfunction;
    }

    public synchronized void update() throws Exception {
        Dataset<Row> source = this.sourceReader().load();
        ALS als = new ALS()
                .setUserCol(sourceConfig.getUserCol())
                .setItemCol(sourceConfig.getItemCol())
                .setRatingCol(sourceConfig.getRateCol())
                .setMaxIter(alsConfig.getMaxIter())
                .setRank(alsConfig.getRank())
                .setNumBlocks(alsConfig.getNumBlocks())
                .setAlpha(alsConfig.getAlpha())
                .setRegParam(alsConfig.getRegParam());

        this.model = als.fit(source).setColdStartStrategy("drop");
        this.recs = this.model.recommendForUserSubset(
                source.select(model.getUserCol()),
                alsConfig.getRecNum());

        this.recs.persist();
        this.recs.count();
    }

    public synchronized Map<Long, Double> recommendByUser(Long id) throws Exception {
        Iterator<Row> ite = this.recs
                .where(String.format("uid = %d", id))
                .select("recommendations").persist().collectAsList().iterator();

        Map<Long, Double> results = new LinkedHashMap<Long, Double>();
        while (ite.hasNext()) {
            Row item = ite.next();
            WrappedArray<?> wrappedArray = (WrappedArray<?>) item.get(0);
            for (int i = 0; i < wrappedArray.size(); i++) {
                results.put(
                        Long.valueOf(((Row) wrappedArray.apply(i)).getInt(0)),
                        Double.valueOf(((Row) wrappedArray.apply(i)).getFloat(1)));
            }
        }

        return results;
    }

    @Override
    protected void finalize() throws Throwable {
        if (spark != null) {
            this.spark.stop();
        }
    }

}
