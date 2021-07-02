package spring.service.aop;

import spring.stereotype.Component;
import spring.util.MessageTracker;

@Component(value = "logService")
public class LogService implements LogInterface {
    @Override
    public void log() {
        System.out.println("place order");
        MessageTracker.addMsg("place order");
    }
}
