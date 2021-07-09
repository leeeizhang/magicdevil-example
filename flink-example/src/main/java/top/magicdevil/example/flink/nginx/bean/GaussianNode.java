package top.magicdevil.example.flink.nginx.bean;

import java.util.Objects;

public class GaussianNode implements Cloneable {
    private long gaussianValue;
    private long timestamp;

    public GaussianNode() {
    }

    public GaussianNode(long GaussianValue, long timestamp) {
        this.gaussianValue = GaussianValue;
        this.timestamp = timestamp;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new GaussianNode(this.gaussianValue, this.timestamp);
    }

    @Override
    public String toString() {
        return "GaussianNode{" +
                "gaussianValue=" + gaussianValue +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GaussianNode that = (GaussianNode) o;
        return Double.compare(that.gaussianValue, gaussianValue) == 0 && timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gaussianValue, timestamp);
    }

    public long getGaussianValue() {
        return gaussianValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setGaussianValue(long gaussianValue) {
        this.gaussianValue = gaussianValue;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
