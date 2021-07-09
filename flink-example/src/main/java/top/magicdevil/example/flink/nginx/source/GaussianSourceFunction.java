package top.magicdevil.example.flink.nginx.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import top.magicdevil.example.flink.nginx.bean.GaussianNode;

import java.util.Date;
import java.util.Random;

public class GaussianSourceFunction implements SourceFunction<GaussianNode> {
    boolean isRunning = true;

    @Override
    public void run(SourceContext<GaussianNode> ctx) throws Exception {
        while (isRunning) {
            Random random = new Random();
            ctx.collect(new GaussianNode(
                    Math.round(Math.abs(random.nextGaussian() * 100)),
                    new Date().getTime()
            ));
//            Thread.sleep(10);
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }

}
