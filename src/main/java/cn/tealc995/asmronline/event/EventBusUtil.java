package cn.tealc995.asmronline.event;

import org.greenrobot.eventbus.EventBus;

/**
 * @program: Asmr-Online
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