package Main;

import ApplicationLayer.AppComponents.AppComponent;
import ApplicationLayer.Channel.SphereChannel;

public class MainRefactor {
    
    public static void main(String[] argv) {
        // Iniciar componentes
        AppComponent A = new AppComponent(
            "Sphere",
            new double[] {0.0},
            new double[] {1.0},
            new String[] {"radius"});

        // Asociar componentes a los channels
        SphereChannel sc = new SphereChannel(A);
        sc.main(new String[] {});

        // Asociar componentes a los servicios

        // Iniciar thread para los channels

        // Iniciar thread para los servicios
    }
}
