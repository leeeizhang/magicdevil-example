package top.magicdevil.example.flink.employee.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import top.magicdevil.example.flink.employee.bean.Employee;

import java.util.*;

public class EmpSource implements SourceFunction<Employee> {
    private boolean isRunning = true;
    private Set<Integer> eidTag = new HashSet<>(128);

    @Override
    public void run(SourceContext<Employee> ctx) throws Exception {
        while (this.isRunning && this.eidTag.size() != 128) {
            Random random = new Random();
            Integer eid = Math.abs(random.nextInt(128));
            if (!eidTag.contains(eid)) {
                eidTag.add(eid);
                ctx.collect(new Employee(
                        Math.abs(random.nextInt(128)),
                        UUID.randomUUID().toString(),
                        Short.valueOf(String.valueOf(Math.abs(random.nextInt(2)))),
                        (new Date()).getTime()
                ));
            }
            Thread.sleep(10);
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }
}
