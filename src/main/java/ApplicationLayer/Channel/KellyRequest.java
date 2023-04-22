package ApplicationLayer.Channel;

public class KellyRequest implements Runnable {

    public String channel;
    public String kellyId;

    public KellyRequest(String channel, String kellyId) {
        this.channel = channel;
        this.kellyId = kellyId;
    }

    public void run() {
        // init process builder
        // start 4 commands to ask for data

        // launch command CCP_A2D_BATCH_READ1
        ProcessBuilder CCP_A2D_BATCH_READ1 = new ProcessBuilder("cansend", channel, (kellyId + "#1B"));
        // read output 

        // launch command CCP_A2D_BATCH_READ2
        ProcessBuilder CCP_A2D_BATCH_READ2 = new ProcessBuilder("cansend", channel, (kellyId + "#1A"));
        
        // read output 

        // launch command CCP_MONITOR1
        ProcessBuilder CCP_MONITOR1 = new ProcessBuilder("cansend", channel, (kellyId + "#33"));
        
        // read output 

        // launch command CCP_MONITOR2
        ProcessBuilder CCP_MONITOR2 = new ProcessBuilder("cansend", channel, (kellyId + "#37"));
        
        try {
            CCP_A2D_BATCH_READ1.start();
            CCP_A2D_BATCH_READ2.start();
            CCP_MONITOR1.start();
            CCP_MONITOR2.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
        // read output 
    }
}
