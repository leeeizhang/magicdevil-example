package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;
import scala.Tuple2;

import java.io.File;
import java.util.*;

public class JavaRandomForestRegressionExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) {
        // $example on$
        SparkConf sparkConf = new SparkConf().setAppName("JavaRandomForestRegressionExample").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        Logger.getRootLogger().setLevel(Level.ERROR);
        // Load and parse the data file.
        String datapath = "data/rfr_libsvm.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc.sc(), datapath).toJavaRDD();
        // Split the data into training and test sets (30% held out for testing)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.0, 1.0});
        JavaRDD<LabeledPoint> trainingData = data;
        JavaRDD<LabeledPoint> testData = splits[1];

        // Set parameters.
        // Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        int numTrees = 3; // Use more in practice.
        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        String impurity = "variance";
        int maxDepth = 4;
        int maxBins = 32;
        int seed = 12345;
        // Train a RandomForest model.
        RandomForestModel model = RandomForest.trainRegressor(trainingData,
                categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins, seed);


        // Evaluate model on test instances and compute test error
        JavaPairRDD<Double, Double> predictionAndLabel =
                testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
        double testMSE = predictionAndLabel.mapToDouble(pl -> {
            double diff = pl._1() - pl._2();
            return diff * diff;
        }).mean();
        System.out.println("com.exceeddata.intern.deploy.Test Mean Squared Error: " + testMSE);
        System.out.println("Learned regression forest model:\n" + model.toDebugString());

        model.save(jsc.sc(), "data/model/regression/randomforest");

        jsc.stop();
    }
}