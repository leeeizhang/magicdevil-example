package top.magicdevil.example.flink.quickstart;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.connectors.kudu.connector.KuduTableInfo;
import org.apache.flink.connectors.kudu.connector.writer.AbstractSingleOperationMapper;
import org.apache.flink.connectors.kudu.connector.writer.KuduWriterConfig;
import org.apache.flink.connectors.kudu.connector.writer.TupleOperationMapper;
import org.apache.flink.connectors.kudu.streaming.KuduSink;
import org.apache.flink.connectors.kudu.table.KuduCatalog;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;

import java.util.*;

//document: https://bahir.apache.org/docs/flink/current/flink-streaming-kudu/
public class FlinkKuduStream {
    public static final String KUDU_MASTERS = "172.24.10.2:7051";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        KuduCatalog catalog = new KuduCatalog(KUDU_MASTERS);

        KuduWriterConfig writerConfig = KuduWriterConfig.Builder.setMasters(KUDU_MASTERS).build();
        KuduTableInfo tableInfo = KuduTableInfo
                .forTable("impala::tmp.flink_kudu_test")
                .createTableIfNotExists(
                        //ColumnSchemasFactory
                        () -> Arrays.asList(
                                new ColumnSchema
                                        .ColumnSchemaBuilder("id", Type.INT32)
                                        .key(true)
                                        .build(),
                                new ColumnSchema
                                        .ColumnSchemaBuilder("name", Type.STRING)
                                        .build(),
                                new ColumnSchema
                                        .ColumnSchemaBuilder("time", Type.INT64)
                                        .build(),
                                new ColumnSchema
                                        .ColumnSchemaBuilder("price", Type.DOUBLE)
                                        .build()
                        ),
                        //CreateTableOptionsFactory
                        () -> new CreateTableOptions()
                                .setNumReplicas(3)
                                .addHashPartitions(Collections.singletonList("id"), 3)
                );
        TupleOperationMapper<Tuple4<Integer, String, Long, Double>> tupleMapper = new TupleOperationMapper<>(
                new String[]{"id", "name", "time", "price"},
                AbstractSingleOperationMapper.KuduOperation.UPSERT);

        KuduSink<Tuple4<Integer, String, Long, Double>> sink = new KuduSink<>(writerConfig, tableInfo, tupleMapper);

        catalog.createTable(tableInfo, true);
        DataStreamSource<Tuple4<Integer, String, Long, Double>> source = env.addSource(new FlinkKuduTestSource());
        source.addSink(sink);

        env.execute();
    }

    public static class FlinkKuduTestSource implements SourceFunction<Tuple4<Integer, String, Long, Double>> {

        boolean flag = true;

        @Override
        public void run(SourceContext<Tuple4<Integer, String, Long, Double>> ctx) throws Exception {
            Random random = new Random();
            while (flag) {
                ctx.collect(new Tuple4<>(
                        Math.abs(random.nextInt()),
                        UUID.randomUUID().toString(),
                        (new Date()).getTime(),
                        Math.abs(random.nextGaussian())
                ));
                Thread.sleep(100);
            }
        }

        @Override
        public void cancel() {
            flag = false;
        }

    }

}
