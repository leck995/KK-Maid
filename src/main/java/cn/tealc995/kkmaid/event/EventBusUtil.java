package cn.tealc995.kkmaid.event;

import org.greenrobot.eventbus.EventBus;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-13 08:56
 */
public class EventBusUtil {
    private static EventBus eventBus=new EventBus();
    public static EventBus getDefault(){
        return eventBus;
    }
}