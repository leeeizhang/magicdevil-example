package top.magicdevil.example.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import scala.Tuple2;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JavaDecisionTreeRegressionExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("JavaDecisionTreeRegressionExample").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        String datapath = "data/sample_libsvm_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc.sc(), datapath).toJavaRDD();
        JavaRDD<LabeledPoint> trainingData = data;
        JavaRDD<LabeledPoint> testData = data;

        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        String impurity = "variance";
        int maxDepth = 5;
        int maxBins = 32;

        DecisionTreeModel model = DecisionTree.trainRegressor(trainingData,
                categoricalFeaturesInfo, impurity, maxDepth, maxBins);

        JavaPairRDD<Double, Double> predictionAndLabel =
                testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
        double testMSE = predictionAndLabel.mapToDouble(pl -> {
            double diff = pl._1() - pl._2();
            return diff * diff;
        }).mean();
        predictionAndLabel.foreach(pair -> System.out.println("预测值：" + pair._1 + "\t真实值：" + pair._2));

        System.out.println(model.topNode());
        System.out.println("com.exceeddata.intern.deploy.Test Mean Squared Error: " + testMSE);
        System.out.println("Learned regression tree model:\n" + model.toDebugString());

        model.save(jsc.sc(), "data/model/regression/decisiontree");

    }
}
