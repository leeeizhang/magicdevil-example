package top.magicdevil.example.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.clustering.GaussianMixture;
import org.apache.spark.ml.clustering.GaussianMixtureModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;

public class JavaGaussianMixtureExample {
    static {
        System.setProperty("hadoop.home.dir", "D:" + File.separator + "Program Files" + File.separator + "hadoop-3.2.0");
    }

    public static void main(String[] args) throws IOException {

        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaGaussianMixtureExample")
                .getOrCreate();
        Logger.getRootLogger().setLevel(Level.ERROR);


        Dataset<Row> dataset = spark.read().format("libsvm").load("data/sample_kmeans_data.txt");

        GaussianMixture gmm = new GaussianMixture()
                .setK(2);
        GaussianMixtureModel model = gmm.fit(dataset);

        for (int i = 0; i < model.getK(); i++) {
            System.out.printf("Gaussian %d:\nweight=%f\nmu=%s\nsigma=\n%s\n\n",
                    i, model.weights()[i], model.gaussians()[i].mean(), model.gaussians()[i].cov());
        }

        model.save("data/model/cluster/gaussian");

        spark.stop();
    }


    //求代数余子式并转置来实现求逆矩阵
    public static double[][] getReverseMartrix(double[][] data) {
        double[][] newdata = new double[data.length][data[0].length];
        double A = getMartrixResult(data);
//      System.out.println(A);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if ((i + j) % 2 == 0) {
                    newdata[i][j] = getMartrixResult(getConfactor(data, i + 1, j + 1)) / A;
                } else {
                    newdata[i][j] = -getMartrixResult(getConfactor(data, i + 1, j + 1)) / A;
                }

            }
        }
        newdata = trans(newdata);
/*
        for (int i = 0; i < newdata.length; i++) {
            for (int j = 0; j < newdata[0].length; j++) {
                System.out.print(newdata[i][j] + "   ");
            }
            System.out.println();
        }*/
        return newdata;
    }

    private static double[][] trans(double[][] newdata) {
        // TODO Auto-generated method stub
        double[][] newdata2 = new double[newdata[0].length][newdata.length];
        for (int i = 0; i < newdata.length; i++)
            for (int j = 0; j < newdata[0].length; j++) {
                newdata2[j][i] = newdata[i][j];
            }
        return newdata2;
    }

    //计算行列式的值
    public static double getMartrixResult(double[][] data) {
        /*
         * 二维矩阵计算
         */
        if (data.length == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        /*
         * 二维以上的矩阵计算
         */
        double result = 0;
        int num = data.length;
        double[] nums = new double[num];
        for (int i = 0; i < data.length; i++) {
            if (i % 2 == 0) {
                nums[i] = data[0][i] * getMartrixResult(getConfactor(data, 1, i + 1));
            } else {
                nums[i] = -data[0][i] * getMartrixResult(getConfactor(data, 1, i + 1));
            }
        }
        for (int i = 0; i < data.length; i++) {
            result += nums[i];
        }

//      System.out.println(result);
        return result;
    }

    //余子式
    public static double[][] getConfactor(double[][] data, int h, int v) {
        int H = data.length;
        int V = data[0].length;
        double[][] newdata = new double[H - 1][V - 1];
        for (int i = 0; i < newdata.length; i++) {
            if (i < h - 1) {
                for (int j = 0; j < newdata[i].length; j++) {
                    if (j < v - 1) {
                        newdata[i][j] = data[i][j];
                    } else {
                        newdata[i][j] = data[i][j + 1];
                    }
                }
            } else {
                for (int j = 0; j < newdata[i].length; j++) {
                    if (j < v - 1) {
                        newdata[i][j] = data[i + 1][j];
                    } else {
                        newdata[i][j] = data[i + 1][j + 1];
                    }
                }
            }
        }
        return newdata;
    }

}