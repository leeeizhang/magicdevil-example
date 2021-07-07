package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.GBTClassificationModel;
import org.apache.spark.ml.classification.GBTClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JavaGradientBoostedTreeClassifierExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaGradientBoostedTreeClassifierExample")
                .getOrCreate();
        Logger.getRootLogger().setLevel(Level.ERROR);

        Dataset<Row> data = spark
                .read()
                .format("libsvm")
                .load("data/sample_libsvm_data.txt");

        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(data);

        VectorIndexerModel featureIndexer = new VectorIndexer()
                .setInputCol("features")
                .setOutputCol("indexedFeatures")
                .setMaxCategories(4)
                .fit(data);

        Dataset<Row>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testData = splits[1];

        GBTClassifier gbt = new GBTClassifier()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("indexedFeatures")
                .setMaxIter(10);

        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(labelIndexer.labels());

        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{labelIndexer, featureIndexer, gbt, labelConverter});

        PipelineModel model = pipeline.fit(trainingData);

        Dataset<Row> predictions = model.transform(testData);

        predictions.select("predictedLabel", "label", "features").show(5);

        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("indexedLabel")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("com.exceeddata.intern.deploy.Test Error = " + (1.0 - accuracy));

        GBTClassificationModel gbtModel = (GBTClassificationModel) (model.stages()[2]);
        System.out.println("Learned classification GBT model:\n" + gbtModel.toDebugString());

        gbtModel.save("data/model/classifier/gbt");
        spark.stop();
    }
}