package top.magicdevil.example.flink.quickstart;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class FlinkKafkaStream {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();

        Properties properties = new Properties() {{
            this.setProperty("bootstrap.servers", "172.37.4.156:9092");
            this.setProperty("group.id", "flink-kafka");
            this.setProperty("key.deserializer", StringDeserializer.class.toString());
            this.setProperty("value.deserializer", StringDeserializer.class.toString());
        }};

        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
                "demo-topic",
                new SimpleStringSchema(),
                properties
        );
        kafkaConsumer.setStartFromEarliest();

        DataStream<String> streamSource = env.addSource(kafkaConsumer).shuffle();

        streamSource.addSink(new FlinkKafkaProducer<String>(
                properties.getProperty("bootstrap.servers"),
                "sink-test",
                new SimpleStringSchema()
        ));

        env.execute();
    }

}
