package ApplicationLayer.Channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import ApplicationLayer.AppComponents.AppComponent;

public class SphereChannel {

    public AppComponent sphere;

    public SphereChannel(AppComponent sphere) {
        this.sphere = sphere;
    }

    /*
     * Read a single frame
     */
    public static void main(String[] argv) {
        // public void read() {
        long maxDelay = 1500;
        String command = "/home/jayki/Desktop/eolian/ea-back-end/sphere.py";
        // Init sphere.py
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
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
                System.out.println(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        // Read sphere.py

        // Close sphere.py
    }
}
