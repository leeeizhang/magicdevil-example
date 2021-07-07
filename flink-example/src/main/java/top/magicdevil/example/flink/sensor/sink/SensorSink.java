package top.magicdevil.example.flink.sensor.sink;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import top.magicdevil.example.flink.sensor.bean.SensorReading;

public class SensorSink implements SinkFunction<Tuple2<SensorReading, String>> {
    @Override
    public void invoke(Tuple2<SensorReading, String> value, Context context) throws Exception {
        System.out.printf("%s - %s : %.2f\n", value.f0.getSensor(), value.f1, value.f0.getTemporary());
    }
}
