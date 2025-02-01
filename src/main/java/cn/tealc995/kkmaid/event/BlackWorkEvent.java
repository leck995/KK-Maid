package cn.tealc995.kkmaid.event;

import cn.tealc995.kikoreu.model.Work;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-08 10:13
 */
public class BlackWorkEvent {
    private Work work;

    public BlackWorkEvent(Work work) {
        this.work = work;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}