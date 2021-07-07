package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.classification.LinearSVC;
import org.apache.spark.ml.classification.LinearSVCModel;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
// $example off$

public class JavaLinearSVCExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaLinearSVCExample")
                .getOrCreate();
        Logger.getRootLogger().setLevel(Level.ERROR);

        // $example on$
        // Load training data
        Dataset<Row> training = spark.read().format("libsvm")
                .load("data/linearSVC_test_lib.txt");

        LinearSVC lsvc = new LinearSVC()
                .setMaxIter(10)
                .setRegParam(0.1);

        // Fit the model
        LinearSVCModel lsvcModel = lsvc.fit(training);

        // Print the coefficients and intercept for LinearSVC
        System.out.println("Coefficients: "
                + lsvcModel.coefficients() + "\n Intercept: " + lsvcModel.intercept());
        // $example off$

        double[] coefficients = lsvcModel.coefficients().toArray();
        double intercept = lsvcModel.intercept();
        double[] test = new double[]{150, (0.012016270614676292 - 150 * -0.009854865101668882) / 0.013149021416450576};
        System.out.println(lsvcModel.predict(Vectors.dense(test)));

        double sum = 0;
        for (int i = 0; i < coefficients.length; i++) {
            sum += test[i] * coefficients[i];
        }
        System.out.print(sum);

        lsvcModel.save("data/model/classifier/linearsvc");

        spark.stop();
    }
}