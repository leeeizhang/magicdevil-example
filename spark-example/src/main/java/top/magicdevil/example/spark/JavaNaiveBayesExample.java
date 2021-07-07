package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JavaNaiveBayesExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaNaiveBayesExample")
                .getOrCreate();
        Logger.getRootLogger().setLevel(Level.ERROR);

        // $example on$
        // Load training data
        Dataset<Row> dataFrame =
                spark.read().format("libsvm").load("data/sample_libsvm_data.txt");

        Dataset<Row> train = dataFrame;
        Dataset<Row> test = dataFrame;

        // create the trainer and set its parameters
        NaiveBayes nb = new NaiveBayes();

        // train the model
        NaiveBayesModel model = nb.fit(train);

        // Select example rows to display.
        Dataset<Row> predictions = model.transform(test);
        predictions.show();

        // compute accuracy on the test set
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("com.exceeddata.intern.deploy.Test set accuracy = " + accuracy);

        System.out.println(Arrays.toString(model.oldLabels()));
        System.out.println(model.pi());
        System.out.println(Arrays.toString(model.theta().toArray()));
        // $example off$

        double[] label = model.oldLabels();
        double[] pi = model.pi().toArray();
        double[] theta = model.theta().toArray();

        System.out.println(model.toString());

        double[] testvalue = {194, 50};
        for (int i = 0; i < pi.length; i++) {
            System.out.println(label[i] + "  :  \t" + ((testvalue[0] * theta[i * 2] + testvalue[1] * theta[i * 2 + 1]) * pi[i]));
        }

        model.save("data/model/classifier/bayes");

        spark.stop();
    }
}