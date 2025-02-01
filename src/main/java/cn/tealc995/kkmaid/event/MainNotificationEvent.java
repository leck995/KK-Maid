package cn.tealc995.kkmaid.event;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-19 05:54
 */
public class MainNotificationEvent {
    private String message;

    public MainNotificationEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}