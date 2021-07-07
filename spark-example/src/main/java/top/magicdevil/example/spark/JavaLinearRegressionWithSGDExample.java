package top.magicdevil.example.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import scala.Tuple2;

import java.io.File;

public class JavaLinearRegressionWithSGDExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("JavaLinearRegressionWithSGDExample").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // $example on$
        // Load and parse the data
        String path = "data/lpsa.data";
        JavaRDD<String> data = sc.textFile(path);
        JavaRDD<LabeledPoint> parsedData = data.map(line -> {
            String[] parts = line.split(",");
            String[] features = parts[1].split(" ");
            double[] v = new double[features.length];
            for (int i = 0; i < features.length - 1; i++) {
                v[i] = Double.parseDouble(features[i]);
            }
            return new LabeledPoint(Double.parseDouble(parts[0]), Vectors.dense(v));
        });
        parsedData.cache();

        // Building the model
        int numIterations = 100;
        double stepSize = 0.00000001;
        LinearRegressionModel model = LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations, stepSize);

        // Evaluate model on training examples and compute training error
        JavaPairRDD<Double, Double> valuesAndPreds = parsedData.mapToPair(point ->
                new Tuple2<>(model.predict(point.features()), point.label()));

        double MSE = valuesAndPreds.mapToDouble(pair -> {
            double diff = pair._1() - pair._2();
            return diff * diff;
        }).mean();
        System.out.println("training Mean Squared Error = " + MSE);
        System.out.println(model);

        model.save(sc.sc(), "data/model/regression/linear");
        // $example off$

        sc.stop();
    }
}
