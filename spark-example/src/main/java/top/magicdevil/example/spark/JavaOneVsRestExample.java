package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.classification.ClassificationModel;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.OneVsRest;
import org.apache.spark.ml.classification.OneVsRestModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
// $example off$
import org.apache.spark.sql.SparkSession;

import java.io.File;

public class JavaOneVsRestExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaOneVsRestExample")
                .getOrCreate();

        Logger.getRootLogger().setLevel(Level.ERROR);
        // $example on$
        // load data file.
        Dataset<Row> inputData = spark.read().format("libsvm")
                .load("data/sample_multiclass_classification_data.txt");

        // generate the train/test split.
        Dataset<Row>[] tmp = inputData.randomSplit(new double[]{1.0, 1.0});
        Dataset<Row> train = tmp[0];
        Dataset<Row> test = tmp[1];

        // configure the base classifier.
        LogisticRegression classifier = new LogisticRegression()
                .setMaxIter(10)
                .setTol(1E-6)
                .setFitIntercept(true);

        // instantiate the One Vs Rest Classifier.
        OneVsRest ovr = new OneVsRest().setClassifier(classifier);

        // train the multiclass model.
        OneVsRestModel ovrModel = ovr.fit(train);

        ClassificationModel[] out = ovrModel.models();

        // score the model on test data.
        Dataset<Row> predictions = ovrModel.transform(test)
                .select("prediction", "label");

        // obtain evaluator.
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");

        // compute the classification error on test data.
        double accuracy = evaluator.evaluate(predictions);

        System.out.println("Error = " + (1 - accuracy));

        spark.stop();
    }

}