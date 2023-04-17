package ApplicationLayer.Channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class KellyChannel {

    public static void main(String[] args) {
        // Init canbus
        
        // launch request in parallel
        Thread req = new Thread(new KellyRequest());
        //
        long maxDelay = 1500;
        String[] command = {"candump", "vcan0", "-T 900"};
        // Init sphere.py
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
                    );
            req.start();
            String line;
            long initialTime = System.currentTimeMillis();
            // while ((line = reader.readLine()) != null) {
                while (true) {
                    if(System.currentTimeMillis() - initialTime >= maxDelay) {
                        System.out.println("Time passed");
                        process.destroy();
                        break;
                    }
                line = reader.readLine();
                // Hacer algo
                if(line != null) System.out.println(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
