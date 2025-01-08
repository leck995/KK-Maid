package cn.tealc995.asmronline.event;

import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.api.model.playList.PlayListRemoveWork;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:39
 */
public class MainPlayListRemoveWorkEvent {
    private Work work;

    public MainPlayListRemoveWorkEvent(Work work) {
        this.work = work;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}