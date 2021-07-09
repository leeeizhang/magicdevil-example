package top.magicdevil.example.flink.nginx.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import top.magicdevil.example.flink.nginx.bean.GaussianNode;
import top.magicdevil.example.flink.nginx.source.GaussianSourceFunction;
import top.magicdevil.example.flink.nginx.utils.RestrictedMap;

import java.util.Map;

public class GaussianStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        DataStream<GaussianNode> inputStream = env.addSource(new GaussianSourceFunction());

        DataStream<GaussianNode> reducedStream = inputStream
                .map(new MapFunction<GaussianNode, Tuple2<GaussianNode, Integer>>() {
                    @Override
                    public Tuple2<GaussianNode, Integer> map(GaussianNode value) throws Exception {
                        return new Tuple2<>(value, 1);
                    }
                })
                .keyBy(source -> source.f0.getGaussianValue())
                .sum(1)
                .keyBy(source -> source.f0.getGaussianValue())
                .process(new KeyedProcessFunction<Long, Tuple2<GaussianNode, Integer>, GaussianNode>() {
                    Map<GaussianNode, Integer> rmap;
                    Integer unhitCount = 0;
                    Integer allCount = 0;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        rmap = new RestrictedMap<>((a, b) -> a - b, 150);
                    }

                    @Override
                    public void close() throws Exception {
                        this.rmap.clear();
                    }

                    @Override
                    public void processElement(
                            Tuple2<GaussianNode, Integer> value,
                            Context ctx,
                            Collector<GaussianNode> out) {
                        if (!rmap.containsKey(value.f0)) {
                            out.collect(value.f0);
                            unhitCount++;
                        }
                        allCount++;
                        rmap.put(value.f0, value.f1);
                        System.out.printf("命中率: %.2f (%d/%d)\n",
                                (allCount - unhitCount) * 1.0 / allCount, unhitCount, allCount);
                    }
                });

        env.execute();
    }
}
