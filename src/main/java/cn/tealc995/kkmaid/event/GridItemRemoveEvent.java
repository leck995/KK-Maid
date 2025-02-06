package cn.tealc995.kkmaid.event;

import cn.tealc995.kikoreu.model.Work;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-15 02:42
 */
public class GridItemRemoveEvent {
    private Work work;

    public GridItemRemoveEvent(Work work) {
        this.work = work;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}