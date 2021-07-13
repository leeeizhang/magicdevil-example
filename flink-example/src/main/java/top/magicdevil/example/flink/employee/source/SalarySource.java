package top.magicdevil.example.flink.employee.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import top.magicdevil.example.flink.employee.bean.Salary;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SalarySource implements SourceFunction<Salary> {
    private boolean isRunning = true;
    private Set<Integer> eidTag = new HashSet<>(128);

    @Override
    public void run(SourceContext<Salary> ctx) throws Exception {
        while (this.isRunning && this.eidTag.size() != 128) {
            Random random = new Random();
            Integer eid = Math.abs(random.nextInt(128));
            Integer months = Math.abs(random.nextInt(36));
            if (!eidTag.contains(eid)) {
                eidTag.add(eid);
                for (int i = 1; i <= months; i++) {
                    ctx.collect(new Salary(
                            eid,
                            1000L * 60 * 60 * 24 * 30 * i,
                            Math.abs(random.nextDouble() * 10000)
                    ));
                }
            }
            Thread.sleep(10);
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }
}
