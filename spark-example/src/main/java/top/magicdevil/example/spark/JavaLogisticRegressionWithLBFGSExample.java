package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;

import java.io.File;

public class JavaLogisticRegressionWithLBFGSExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("JavaLogisticRegressionWithLBFGSExample").setMaster("local");

        SparkContext sc = new SparkContext(conf);

        Logger.getRootLogger().setLevel(Level.ERROR);

        // $example on$

        String path = "data/sample_libsvm_data.txt";

        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path).toJavaRDD();


        // Split initial RDD into two... [60% training data, 40% testing data].

        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.6, 0.4}, 11L);

        JavaRDD<LabeledPoint> training = splits[0].cache();

        JavaRDD<LabeledPoint> test = splits[1];


        // Run training algorithm to build the model.


        LogisticRegressionModel model = new LogisticRegressionWithLBFGS().setNumClasses(10).run(training.rdd());


        // Compute raw scores on the test set.

        JavaPairRDD<Object, Object> predictionAndLabels = test.mapToPair(p ->

                new Tuple2<>(model.predict(p.features()), p.label()));


        // Get evaluation metrics.

        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());

        double accuracy = metrics.accuracy();

        System.out.println("Accuracy = " + accuracy);

        System.out.println("Intercept=" + model.intercept());

        System.out.println("features=" + model.numFeatures());

        System.out.println("numClasses=" + model.numClasses());

        System.out.println("weight=" + model.weights());

        predictionAndLabels.foreach(t -> System.out.println(t._1 + " " + t._2));

        // Save and load model

        model.save(sc, "data/model/classifier/logistic");

        //LogisticRegressionModel sameModel = LogisticRegressionModel.load(sc, "target/tmp/javaLogisticRegressionWithLBFGSModel");

        // $example off$


        sc.stop();

    }
}