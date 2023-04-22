package ApplicationLayer.Channel;

import java.util.List;
import ApplicationLayer.LocalServices.Service;

public class ServiceRunner implements Runnable {

    public List<Service> services;
    public int delay;
    
    public ServiceRunner(List<Service> services, int delay) {
        this.services = services;
        this.delay = delay;
    }

    public void runServices() {
        while(true) {
            for(Service s : services) {
                System.out.println("Executing service ");
                System.out.println(s.id);
                s.consumeComponents();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        runServices();
    }
    
}
