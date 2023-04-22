package ApplicationLayer.Channel;

import ApplicationLayer.AppComponents.AppComponent;
import ApplicationLayer.LocalServices.Service;
import ApplicationLayer.Utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Canbus0 extends Channel {
    private int[] data = new int[8]; // Memory efficient buffer

    private AppComponent sevcon_izq;
    private AppComponent sevcon_der;
    private AppComponent lcd;
    private final int lenSevcon = 16; // Hardcoded, specific, actual values updated in this implementation for this Component
    private boolean dev = false;
    /**
     * Each channel has predefined AppComponents
     *
     * @param myComponentList List of AppComponent that this Channel update values to
     * @param myServices Services to inform to whenever an AppComponents get updated
     */
    public Canbus0(List<AppComponent> myComponentList, List<Service> myServices, boolean dev) {
        super(myComponentList, myServices);
        this.dev = dev;
        this.id = "Canbus0";
        // Check that a BMS AppComponent was supplied
        // With the exact amount of double[] values as the implementation here
        for(AppComponent ac : myComponentList) {
            if(ac.getID().equals("sevcon_izq")) {
                sevcon_izq = ac;
            }
            else if(ac.getID().equals("sevcon_der")) {
                sevcon_der = ac;
            }
            else if(ac.getID().equals("lcd")) {
                lcd = ac;
            }
        }
        // try{
        //     this.sevcon = this.myComponentsMap.get("sevcon"); // Must match name in .xlsx file
        //     if(sevcon != null){
        //         int len = sevcon.len;
        //         if(len != this.lenSevcon){
        //             throw new Exception("Cantidad de valores del SEVCON en AppComponent != Cantidad de valores de lectura implementados");
        //         }
        //     }else{
        //         throw new Exception("A Sevcon AppComponent was not supplied in Canbus1 channel");
        //     }
        // }catch(Exception e){
        //     e.printStackTrace();
        // }
        //this.lcd = this.myComponentsMap.get("lcd");
    }

    @Override
    public void readingLoop() {}
    /**
     * Main reading and parsing loop
     */
    @Override
    public void singleRead() {
        Thread req = new Thread(new KellyRequest("can0", "0C8"));
        //
        long maxDelay = 1500;
        String[] command = {"candump", "can0,0cd:7FF", "-T 900"};
        // Init sphere.py
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
                    );
            req.start();
            String line;
            int msg = 0;
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
                if(line != null) {
                    System.out.println("read");
                    System.out.println(line);
                    parseMessage(line, msg);
                    msg++;
                    msg = msg % 4;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Commands executed once
     */
    @Override
    public void setUp() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);
        // NOTA: primero hay que iniciar el can com en comando 'stty -F /dev/serial0 raw 9600 cs8 clocal -cstopb'
        // (9600 es el baud rate)
        //stringBuilder.append("cd ./src/main/java/ApplicationLayer/SensorReading/CANReaders/linux-can-utils;");
        //stringBuilder.append("gcc candump.c lib.c -o candump;"); // Comment this on second execution, no need to recompile
        processBuilder.command("sudo", "/sbin/ip", "link" , "set", "can0", "up", "type", "can", "bitrate", "1000000");
        try {
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parsing function. Transforms CANBUS message from console to double,
     * into AppComponent bms's double[] valoresRealesActuales, directly.
     * @param message
     */
    public void parseMessage(String message, int msgNumber) {
        //String[] msg = Utils.split(message, " "); // Better performance split than String.split()
        String[] msg = message.split("\\s+"); // etter performance split than String.split()
        if(msg[0].length() < 1) return;

        switch (msgNumber){
            case 0:
                System.out.println("Mensaje 0 kelly");
                break;
            case 1:
                System.out.println("Mensaje 1 kelly");
                break;
            case 2:
                System.out.println("Mensaje 2 kelly");
                break;
            case 3:
                System.out.println("Mensaje 3 kelly");
                break;
            default:
                System.out.println("Trama "+msg[0]+" no procesada");
        } // switch
    } // parseMessage()
}
