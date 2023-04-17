package ApplicationLayer.Channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/*
 * Recibe una lista de channels, un delay en ms y ejecuta secuencialmente los channels separados
 * por el delay.
 */
public class ChannelRunner {

    public List<Channel> channels;
    public int delay;

    public ChannelRunner(List<Channel> channels, int delay) {
        this.channels = channels;
        this.delay = delay;
    }

    public static void main(String[] command) {
        // public void read() {
        // Init sphere.py
        ProcessBuilder processBuilder = new ProcessBuilder("/usr/bin/candump", "any", "-T 1000");
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            long initialTime = System.currentTimeMillis();
            // while ((line = reader.readLine()) != null) {
            while (true) {
                if(System.currentTimeMillis() - initialTime >= 1500) {
                    System.out.println("Time passed");
                    process.destroy();
                    break;
                }
                line = reader.readLine();
                // Hacer algo
                System.out.println(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void run() {
    //     while(true) {
    //         for(Channel channel : channels) {
    //             channel.run();
    //         }
    //     }
    // }
}
