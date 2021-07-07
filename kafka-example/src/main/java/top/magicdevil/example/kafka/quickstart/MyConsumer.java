package top.magicdevil.example.kafka.quickstart;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class MyConsumer {

    private static final Properties prop = new Properties() {{
        // 设置集群地址
        this.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.37.4.156:9092");
        // 从poll回话处理时长
        this.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // 若Topic不存在,则自动创建
        this.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "true");
        // 消费规则，earliest/latest
        this.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 序列化
        this.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        this.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    }};

    static class ConsumerThread implements Runnable {

        private final Consumer<String, String> consumer;
        private final Gson gson = new Gson();

        public ConsumerThread(final Properties properties) {
            properties.put(
                    ConsumerConfig.GROUP_ID_CONFIG,
                    String.valueOf(Thread.currentThread().getId())
            ); // 组名,不同组名可以重复消费

            this.consumer = new KafkaConsumer<>(properties);
            this.consumer.subscribe(Arrays.asList("demo-topic"));
        }

        public synchronized void run() {
            while (true) {
                try {
                    ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofHours(1));
                    Iterator<ConsumerRecord<String, String>> ite = records.iterator();

                    while (ite.hasNext()) {
                        ConsumerRecord<String, String> record = ite.next();
                        System.out.println(String.format("consumer-%d : %s",
                                Thread.currentThread().getId(), gson.fromJson(record.value(), Map.class)));
                    }

                    this.wait(500);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        ConsumerThread thread1 = new ConsumerThread(prop);
        ConsumerThread thread2 = new ConsumerThread(prop);
        ConsumerThread thread3 = new ConsumerThread(prop);
        new Thread(thread1).start();
        new Thread(thread2).start();
        new Thread(thread3).start();
    }

}
