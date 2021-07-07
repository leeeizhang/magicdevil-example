package top.magicdevil.example.flink.quickstart;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class WordCountStream {
    public static void main(String[] args) throws Exception {

        /* 获取当前执行环境
         * ExecutionEnvironment: Bounded Stream
         * StreamExecutionEnvironment: Unbounded Stream
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();

        /*
         * 在执行环境中添加数据源
         */
        DataStream<String> inputSource = env.addSource(
                new WordCountSource(), "wordCountSource");

        // 命令行执行: nc -lk 7777 开启监听端口，从Socket中流式读取文本
        // ParameterTool pmtool = ParameterTool.fromArgs(args);
        // String host = pmtool.get("host", "0.0.0.0");
        // Integer port = pmtool.getInt("port", 7777);
        // DataStream<String> inputSource = env.socketTextStream(host, port);

        /*
         * 进行MapReduce算子操作
         */
        DataStream<Tuple2<String, Integer>> output = inputSource
                .flatMap(new wordCountFlatMap())
                .keyBy(value -> value.f0)
                .sum(1);

        output.print();

        /*
         * StreamExecutionEnvironment下需要激活执行
         */
        env.execute("wordCountStream");
    }

    public static class WordCountSource implements SourceFunction<String> {

        public boolean flag = false;

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            List<String> sourceTemplate = new ArrayList<>() {{
                this.add("hello world");
                this.add("hello scala");
                this.add("hello flink");
                this.add("happy birthday");
                this.add("how about you");
                this.add("this is an unbounded stream");
            }};

            while (!flag) {
                for (String word : sourceTemplate) {
                    ctx.collect(word);
                }
                Thread.sleep(100);
            }
        }

        @Override
        public void cancel() {
            this.flag = true;
        }

    }

    public static class wordCountFlatMap
            implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(
                String value,
                Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] words = value.split("\\s");
            for (String word : words) {
                out.collect(new Tuple2<>(word, 1));
            }
        }
    }

}