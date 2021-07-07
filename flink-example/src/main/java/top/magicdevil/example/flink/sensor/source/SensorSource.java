package top.magicdevil.example.flink.sensor.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import top.magicdevil.example.flink.sensor.bean.SensorReading;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SensorSource implements SourceFunction<SensorReading> {

    private boolean isRun = true;

    private static List<String> sensorTemplate = new ArrayList<>() {{
        this.add("sensor-1");
        this.add("sensor-2");
        this.add("sensor-3");
        this.add("sensor-4");
        this.add("sensor-5");
    }};

    @Override
    public void run(SourceContext ctx) throws Exception {
        while (isRun) {
            ctx.collect(new SensorReading(
                    sensorTemplate.get(Math.abs((new Random()).nextInt()) % sensorTemplate.size()),
                    (new Random()).nextGaussian() * 10,
                    new Date()
            ));
            Thread.sleep(100);
        }
    }

    @Override
    public void cancel() {
        isRun = false;
    }

}
