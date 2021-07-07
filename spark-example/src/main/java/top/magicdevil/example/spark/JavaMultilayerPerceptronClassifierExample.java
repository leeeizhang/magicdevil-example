package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JavaMultilayerPerceptronClassifierExample {

    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaMultilayerPerceptronClassifierExample")
                .getOrCreate();
        Logger.getRootLogger().setLevel(Level.ERROR);

        // $example on$
        // Load training data
        String path = "data/sample_multiclass_classification_data.txt";
        Dataset<Row> dataFrame = spark.read().format("libsvm").load(path);

        // Split the data into train and test
        Dataset<Row> train = dataFrame;
        Dataset<Row> test = dataFrame;

        // specify layers for the neural network:
        // input layer of size 4 (features), two intermediate of size 5 and 4
        // and output of size 3 (classes)
        int[] layers = new int[]{4, 5, 4, 3};

        // create the trainer and set its parameters
        MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier()
                .setLayers(layers)
                .setBlockSize(128)
                .setSeed(1234L)
                .setMaxIter(100);

        // train the model
        MultilayerPerceptronClassificationModel model = trainer.fit(train);

        // compute accuracy on the test set
        Dataset<Row> result = model.transform(test);
        Dataset<Row> predictionAndLabels = result.select("prediction", "label");
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");

        System.out.println("com.exceeddata.intern.deploy.Test set accuracy = " + evaluator.evaluate(predictionAndLabels));
        // $example off$

        predictionAndLabels.foreach((ForeachFunction<Row>) s -> System.out.println(s));

        System.out.println();

        model.save("data/model/classifier/mlp");

        spark.stop();
    }
}