package top.magicdevil.example.flink.employee.stream;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import top.magicdevil.example.flink.employee.bean.Employee;
import top.magicdevil.example.flink.employee.bean.Salary;
import top.magicdevil.example.flink.employee.source.EmpSource;
import top.magicdevil.example.flink.employee.source.SalarySource;

import java.time.Duration;

// TODO: JoinedStream meet timestamp issue to fix

public class EmployeeStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Employee> empStream = env.addSource(new EmpSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        .<Employee>noWatermarks()
                        .withTimestampAssigner((event, assigner) -> 0L)
                );

        DataStream<Salary> salaryStream = env.addSource(new SalarySource())
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        .<Salary>forBoundedOutOfOrderness(Duration.ofDays(30 * 48))
                        .withTimestampAssigner((event, assigner) -> event.getPayday())
                );

        SingleOutputStreamOperator<Tuple3<Salary, Integer, Double>> reducedStream = salaryStream
                .map(new MapFunction<Salary, Tuple3<Salary, Integer, Double>>() {
                    @Override
                    public Tuple3<Salary, Integer, Double> map(Salary value) throws Exception {
                        return new Tuple3<>(value, 1, value.getSalary());
                    }
                })
                .keyBy(tup -> tup.f0.getEid())
                .window(TumblingEventTimeWindows.of(Time.days(30 * 48)))
                .reduce(new ReduceFunction<Tuple3<Salary, Integer, Double>>() {
                    @Override
                    public Tuple3<Salary, Integer, Double> reduce(
                            Tuple3<Salary, Integer, Double> value1,
                            Tuple3<Salary, Integer, Double> value2) throws Exception {
                        return new Tuple3<>(value1.f0, value1.f1 + 1, value1.f2 + value2.f2);
                    }
                });

        DataStream<Tuple3<Employee, Integer, Double>> joinedStream = reducedStream
                .join(empStream).where(tup -> tup.f0.getEid()).equalTo(emp -> emp.getEid())
                .window(TumblingEventTimeWindows.of(Time.days(30 * 48)))
                .apply(new JoinFunction<Tuple3<Salary, Integer, Double>, Employee, Tuple3<Employee, Integer, Double>>() {
                    @Override
                    public Tuple3<Employee, Integer, Double> join(
                            Tuple3<Salary, Integer, Double> first,
                            Employee second) throws Exception {
                        return new Tuple3<>(second, first.f1, first.f2);
                    }
                });

        joinedStream.print();

        env.execute();
    }
}
