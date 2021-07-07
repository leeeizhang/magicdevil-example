package top.magicdevil.example.webapp.sample.bookstore.spark.config;

import java.io.Serializable;

import org.apache.spark.SparkConf;

public class SparkConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String master;
    private String appName;
    private String panelURL;

    public SparkConf generateSparkConf() {
        return (new SparkConf()).setAppName(appName).setMaster(master);
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPanelURL() {
        return panelURL;
    }

    public void setPanelURL(String panelURL) {
        this.panelURL = panelURL;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appName == null) ? 0 : appName.hashCode());
        result = prime * result + ((master == null) ? 0 : master.hashCode());
        result = prime * result + ((panelURL == null) ? 0 : panelURL.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SparkConfig other = (SparkConfig) obj;
        if (appName == null) {
            if (other.appName != null)
                return false;
        } else if (!appName.equals(other.appName))
            return false;
        if (master == null) {
            if (other.master != null)
                return false;
        } else if (!master.equals(other.master))
            return false;
        if (panelURL == null) {
            if (other.panelURL != null)
                return false;
        } else if (!panelURL.equals(other.panelURL))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SparkConfig [master=" + master + ", appName=" + appName + ", panelURL=" + panelURL
                + "]";
    }

}
