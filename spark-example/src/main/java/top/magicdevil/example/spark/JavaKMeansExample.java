package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.evaluation.ClusteringEvaluator;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JavaKMeansExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) throws IOException {
        // Create a SparkSession.
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaKMeansExample")
                .getOrCreate();
        Logger.getRootLogger().setLevel(Level.ERROR);

        // $example on$
        // Loads data.
        Dataset<Row> dataset = spark.read().format("libsvm").load("data/sample_kmeans_data.txt");

        // Trains a k-means model.
        KMeans kmeans = new KMeans().setK(2).setSeed(1L);
        KMeansModel model = kmeans.fit(dataset);

        // Make predictions
        Dataset<Row> predictions = model.transform(dataset);

        // Evaluate clustering by computing Silhouette score
        ClusteringEvaluator evaluator = new ClusteringEvaluator();

        double silhouette = evaluator.evaluate(predictions);
        System.out.println("Silhouette with squared euclidean distance = " + silhouette);

        // Shows the result.
        Vector[] centers = model.clusterCenters();
        System.out.println("Cluster Centers: ");
        for (Vector center : centers) {
            System.out.println(center);
        }
        System.out.println("k：" + model.getK());
        System.out.println(model.predict(Vectors.dense(new double[]{10, 10, 10})));
        // $example off$

        model.save("data/model/cluster/kmeans");

        spark.stop();
    }
}