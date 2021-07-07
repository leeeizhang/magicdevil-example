package top.magicdevil.example.spark;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;

class JavaDecisionTreeClassificationExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("JavaDecisionTreeClassificationExample").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        Logger.getRootLogger().setLevel(Level.ERROR);

        String datapath = "data/sample_libsvm_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc.sc(), datapath).toJavaRDD();

        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        int numClasses = 2;
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        String impurity = "gini";
        int maxDepth = 5;
        int maxBins = 32;

        DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses,
                categoricalFeaturesInfo, impurity, maxDepth, maxBins);

        JavaPairRDD<Double, Double> predictionAndLabel =
                testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
        double testErr =
                predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testData.count();

        System.out.println("Learned classification tree model:\n" + model.toDebugString());
        System.out.println(model.topNode().toString());
        System.out.println(model.topNode().rightNode());

         model.save(jsc.sc(), "data/model/classifier/decisiontree");
    }
}