package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.regression.GeneralizedLinearRegression;
import org.apache.spark.ml.regression.GeneralizedLinearRegressionModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class JavaGeneralizedLinearRegressionExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaGeneralizedLinearRegressionExample")
                .getOrCreate();
        Logger.getRootLogger().setLevel(Level.ERROR);

        Dataset<Row> dataset = spark.read().format("libsvm")
                .load("data/sample_linear_regression_data.txt");

        GeneralizedLinearRegression glr = new GeneralizedLinearRegression()
                .setFamily("gaussian")
                .setLink("identity")
                .setMaxIter(10)
                .setRegParam(0.3);

        GeneralizedLinearRegressionModel model = glr.fit(dataset);

        model.save("data/model/regression/generallinear");

        spark.stop();
    }
}