package top.magicdevil.example.flink.sensor.bean;

import java.util.Date;

public class SensorReading {
    private String sensor;
    private Double temporary;
    private Long timestamp;

    public SensorReading() {
    }

    public SensorReading(String sensor, Double temporary, Long timestamp) {
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
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
