package top.magicdevil.example.flink.sensor.bean;

import java.util.Date;

public class SensorReading {
    private String sensor;
    private Double temporary;
    private Date timestamp;

    public SensorReading() {
    }

    public SensorReading(String sensor, Double temporary, Date timestamp) {
        this.sensor = sensor;
        this.temporary = temporary;
        this.timestamp = timestamp;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public Double getTemporary() {
        return temporary;
    }

    public void setTemporary(Double temporary) {
        this.temporary = temporary;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "sensor='" + sensor + '\'' +
                ", temporary=" + temporary +
                ", timestamp=" + timestamp +
                '}';
    }
}
