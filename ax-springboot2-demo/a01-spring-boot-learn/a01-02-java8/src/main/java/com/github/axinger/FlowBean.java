package com.github.axinger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowBean {
    private long upFlow; //上行流量
    private long downFlow;//下行流量
    private long sumFlow; //总流量

    // 合并函数
    public static void combine(FlowBean a, FlowBean b) {
        FlowBean combined = new FlowBean();
        combined.upFlow = a.upFlow + b.upFlow;
        combined.downFlow = a.downFlow + b.downFlow;
        combined.sumFlow = combined.upFlow + combined.downFlow;
    }

    public void setSumFlow() {
        this.sumFlow = this.upFlow + this.downFlow;
    }

    @Override
    public String toString() {
        return upFlow + "\t" + downFlow + "\t" + sumFlow;
    }

    // 累加函数
    public void accumulate(FlowBean other) {
        this.upFlow += other.upFlow;
        this.downFlow += other.downFlow;
        setSumFlow();
    }
}
