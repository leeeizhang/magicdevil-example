package top.magicdevil.example.spark;

// $example on$

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.spark_project.dmg.pmml.KNNInput;
import org.spark_project.dmg.pmml.KNNInputs;
import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.GradientBoostedTrees;
import org.apache.spark.mllib.tree.configuration.BoostingStrategy;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.util.MLUtils;

public class JavaGradientBoostingRegressionExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }
    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setMaster("local")
                .setAppName("JavaGradientBoostedTreesRegressionExample");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        Logger.getRootLogger().setLevel(Level.ERROR);
        // Load and parse the data file.
        String datapath = "data/sample_libsvm_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc.sc(), datapath).toJavaRDD();
        // Split the data into training and test sets (30% held out for testing)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{1.0, 0.0});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        // Train a GradientBoostedTrees model.
        // The defaultParams for Regression use SquaredError by default.
        BoostingStrategy boostingStrategy = BoostingStrategy.defaultParams("Regression");
        boostingStrategy.setNumIterations(3); // Note: Use more iterations in practice.
        boostingStrategy.getTreeStrategy().setMaxDepth(5);
        // Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        boostingStrategy.treeStrategy().setCategoricalFeaturesInfo(categoricalFeaturesInfo);

        GradientBoostedTreesModel model = GradientBoostedTrees.train(trainingData, boostingStrategy);

        // Evaluate model on test instances and compute test error
        JavaPairRDD<Double, Double> predictionAndLabel =
                testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
        double testMSE = predictionAndLabel.mapToDouble(pl -> {
            double diff = pl._1() - pl._2();
            return diff * diff;
        }).mean();

        System.out.println("Learned regression GBT model:\n" + model.toDebugString());

        model.save(jsc.sc(), "data/model/regression/gbt");

        jsc.stop();
    }
}