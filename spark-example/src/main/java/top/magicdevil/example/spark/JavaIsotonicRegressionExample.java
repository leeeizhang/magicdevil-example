package top.magicdevil.example.spark;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import scala.Tuple2;
import scala.Tuple3;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.IsotonicRegression;
import org.apache.spark.mllib.regression.IsotonicRegressionModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;

import org.apache.spark.SparkConf;

import java.io.File;
import java.util.Arrays;

public class JavaIsotonicRegressionExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("JavaIsotonicRegressionExample").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        Logger.getRootLogger().setLevel(Level.ERROR);

        // $example on$
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(
                jsc.sc(), "data/sample_isotonic_regression_libsvm_data.txt").toJavaRDD();

        // Create label, feature, weight tuples from input data with weight set to default value 1.0.
        JavaRDD<Tuple3<Double, Double, Double>> parsedData = data.map(point ->
                new Tuple3<>(point.label(), point.features().apply(1), 1.0));

        // Split data into training (60%) and test (40%) sets.
        JavaRDD<Tuple3<Double, Double, Double>>[] splits =
                parsedData.randomSplit(new double[]{0.6, 0.4}, 11L);
        JavaRDD<Tuple3<Double, Double, Double>> training = parsedData;
        JavaRDD<Tuple3<Double, Double, Double>> test = parsedData;

        // Create isotonic regression model from training data.
        // Isotonic parameter defaults to true so it is only shown for demonstration
        IsotonicRegressionModel model = new IsotonicRegression().setIsotonic(true).run(training);

        // Create tuples of predicted and real labels.
        JavaPairRDD<Double, Double> predictionAndLabel = test.mapToPair(point ->
                new Tuple2<>(model.predict(point._2()), point._1()));

        System.out.println("predictions = " + Arrays.toString(model.predictions()));
        System.out.println("boundaries = " + Arrays.toString(model.boundaries()));
        System.out.println("isotonic = " + model.isotonic());
        System.out.println("test = ");
        test.foreach(s -> System.out.println(s._1() + " " + s._2() + " " + s._3()));
        // Calculate mean squared error between predicted and real labels.
        double meanSquaredError = predictionAndLabel.mapToDouble(pl -> {
            double diff = pl._1() - pl._2();
            return diff * diff;
        }).mean();
        System.out.println("Mean Squared Error = " + meanSquaredError);
        predictionAndLabel.foreach(pair -> System.out.println("预测值：" + pair._1 + "\t真实值：" + pair._2));

        model.save(jsc.sc(),"data/model/regression/isotonic");

        jsc.stop();
    }
}