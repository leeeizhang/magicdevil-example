package top.magicdevil.example.kafka.quickstart;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;
import java.util.concurrent.Future;

public class MyProducer {

    private static final Properties prop = new Properties() {{
        // 设置集群地址
        this.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.37.4.156:9092");
        // ACK机制
        this.put(ProducerConfig.ACKS_CONFIG, "all");
        // 重试次数
        this.put(ProducerConfig.RETRIES_CONFIG, "0");
        // 批次大小:消息大小为16KB才发送消息
        this.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        // 等待时间:如果消息大小迟迟不为batch.size大小，则等待linger.ms时间后直接发送
        this.put(ProducerConfig.LINGER_MS_CONFIG, "10");
        // ReadAccumulator缓冲区大小
        this.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        // 序列化
        this.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    }};

    static class ProducerThread implements Runnable {

        private final Producer<String, String> producer;
        private final Gson gson = new Gson();

        public ProducerThread(final Properties properties) {
            this.producer = new KafkaProducer<String, String>(properties);
        }

        public synchronized void run() {
            while (true) {
                try {
                    Map<String, String> data = new HashMap<>();
                    {
                        data.put("name", UUID.randomUUID().toString());
                        data.put("cnt", String.valueOf(Math.round(Math.random() * 100)));
                        data.put("time", (new Date()).toString());
                        data.put("price", String.valueOf(Math.random() * 100));
                    }
                    ProducerRecord<String, String> record = new ProducerRecord<>(
                            "demo-topic",
                            UUID.randomUUID().toString(),
                            gson.toJson(data)
                    );

                    Future<RecordMetadata> future = this.producer.send(record);
                    RecordMetadata metadata = future.get(); //等待发送结果返回，实现消息异步转同步

                    System.out.println(String.format("producer-%d : send %s",
                            Thread.currentThread().getId(), metadata.offset()));

                    this.wait(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        ProducerThread thread1 = new ProducerThread(prop);
        ProducerThread thread2 = new ProducerThread(prop);
        ProducerThread thread3 = new ProducerThread(prop);
        new Thread(thread1).start();
        new Thread(thread2).start();
        new Thread(thread3).start();
    }

}
