package top.magicdevil.example.flink.sensor.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import top.magicdevil.example.flink.sensor.sink.SensorSink;
import top.magicdevil.example.flink.sensor.source.SensorSource;
import top.magicdevil.example.flink.sensor.bean.SensorReading;

public class SensorStream {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<SensorReading> inputStreamA = env.addSource(new SensorSource());
        DataStream<SensorReading> inputStreamB = env.addSource(new SensorSource());

        DataStream<SensorReading> unionSource = inputStreamA.union(inputStreamB);

        DataStream<Tuple2<SensorReading, String>> outputSource = unionSource
                .filter(source -> source.getTemporary().doubleValue() > 0.0D)
                .map(new MapFunction<SensorReading, Tuple2<SensorReading, String>>() {
                    @Override
                    public Tuple2<SensorReading, String> map(SensorReading source) throws Exception {
                        if (source.getTemporary() < 10.0D) {
                            return new Tuple2<>(source, "low");
                        } else if (source.getTemporary() < 20.0D) {
                            return new Tuple2<>(source, "normal");
                        } else {
                            return new Tuple2<>(source, "high");
                        }
                    }
                })
                .keyBy(source -> source.f1)
                .keyBy(source -> source.f0.getSensor())
                .reduce(new ReduceFunction<Tuple2<SensorReading, String>>() {
                    @Override
                    public Tuple2<SensorReading, String> reduce(
                            Tuple2<SensorReading, String> value1,
                            Tuple2<SensorReading, String> value2) throws Exception {
                        if (value1.f0.getTemporary() < value2.f0.getTemporary()
                                && value1.f0.getTimestamp() < value2.f0.getTimestamp()) {
                            return value2;
                        }
                        return value1;
                    }
                });

        outputSource.addSink(new SensorSink());

        env.execute();
    }

}
