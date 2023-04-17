package ApplicationLayer.Channel;

import java.io.IOException;
import java.io.OutputStream;

public class CanSend {
    
    public static void main(String[] args) {
        try {
            ProcessBuilder sender = new ProcessBuilder("cansend", "vcan0", "101#ab");
            Process msg = sender.start();
            System.out.println(msg.isAlive());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
