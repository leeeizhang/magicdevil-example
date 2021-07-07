package top.magicdevil.example.spark;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.regression.AFTSurvivalRegression;
import org.apache.spark.ml.regression.AFTSurvivalRegressionModel;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;


public class JavaAFTSurvivalRegressionExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaAFTSurvivalRegressionExample")
                .getOrCreate();
        Logger.getRootLogger().setLevel(Level.ERROR);

        List<Row> data = Arrays.asList(
                RowFactory.create(1.218, 1.0, Vectors.dense(1.560, -0.605)),
                RowFactory.create(2.949, 0.0, Vectors.dense(0.346, 2.158)),
                RowFactory.create(3.627, 0.0, Vectors.dense(1.380, 0.231)),
                RowFactory.create(0.273, 1.0, Vectors.dense(0.520, 1.151)),
                RowFactory.create(4.199, 0.0, Vectors.dense(0.795, -0.226))
        );
        StructType schema = new StructType(new StructField[]{
                new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("censor", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("features", new VectorUDT(), false, Metadata.empty())
        });
        Dataset<Row> training = spark.createDataFrame(data, schema);
        double[] quantileProbabilities = new double[]{0.3, 0.6};
        AFTSurvivalRegression aft = new AFTSurvivalRegression()
                .setQuantileProbabilities(quantileProbabilities)
                .setQuantilesCol("quantiles");

        AFTSurvivalRegressionModel model = aft.fit(training);

        // Print the coefficients, intercept and scale parameter for AFT survival regression
        System.out.println("Coefficients: " + model.coefficients());
        System.out.println("Intercept: " + model.intercept());
        System.out.println("Scale: " + model.scale());
        
        model.save("data/model/regression/aft");

        spark.stop();
    }
}