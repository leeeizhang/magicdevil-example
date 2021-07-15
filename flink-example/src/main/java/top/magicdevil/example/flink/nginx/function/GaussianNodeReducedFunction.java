package top.magicdevil.example.flink.nginx.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import top.magicdevil.example.flink.nginx.bean.GaussianNode;
import top.magicdevil.example.flink.nginx.utils.RestrictedMap;

public class GaussianNodeReducedFunction
        extends KeyedProcessFunction<Long, Tuple2<GaussianNode, Integer>, GaussianNode> {

    ValueState<RestrictedMap> rmap;
    Integer unhitCount = 0;
    Integer allCount = 0;

    @Override
    public void open(Configuration parameters) throws Exception {
        this.rmap = this.getRuntimeContext().getState(
                new ValueStateDescriptor<>("rmap", RestrictedMap.class)
        );
    }

    @Override
    public void processElement(
            Tuple2<GaussianNode, Integer> value,
            Context ctx,
            Collector<GaussianNode> out) throws Exception {
        if (rmap.value() == null) {
            this.rmap.update(new RestrictedMap<GaussianNode, Integer>((a, b) -> a - b, 100));
        }
        if (!rmap.value().containsKey(value.f0)) {
            out.collect(value.f0);
            unhitCount++;
        }
        allCount++;
        rmap.value().put(value.f0, value.f1);
        System.out.printf("命中率: %.4f (%d/%d)\n",
                (allCount - unhitCount) * 1.0 / allCount, unhitCount, allCount);
    }

}
