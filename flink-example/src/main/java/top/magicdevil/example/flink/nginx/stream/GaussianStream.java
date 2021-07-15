package top.magicdevil.example.flink.nginx.stream;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import top.magicdevil.example.flink.nginx.bean.GaussianNode;
import top.magicdevil.example.flink.nginx.function.GaussianNodeReducedFunction;
import top.magicdevil.example.flink.nginx.source.GaussianSourceFunction;

public class GaussianStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        DataStream<GaussianNode> inputStream = env.addSource(new GaussianSourceFunction());

        DataStream<GaussianNode> reducedStream = inputStream
                .map(node -> new Tuple2<GaussianNode, Integer>(node, 1))
                .returns(Types.TUPLE(Types.POJO(GaussianNode.class), Types.INT))
                .keyBy(source -> source.f0.getGaussianValue())
                .sum(1)
                .keyBy(source -> source.f0.getGaussianValue())
                .process(new GaussianNodeReducedFunction());

        env.execute();
    }
}
