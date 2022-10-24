package ApplicationLayer.LocalServices;

import ApplicationLayer.AppComponents.AppComponent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.sql.*;

/**
 * Se encarga de guardar la información de los componentes a la base de datos.
 * Debe haber una tabla para cada AppComponent en la base de datos.
 * Hace insert de los datos actuales con un timestamp.
 */
public class DatabaseService extends Service implements Runnable {

    public Connection data_base;

    public String absolute_path; // path to /data folder
    public String[] components;
    public String date; // path to /data/{date} folder

    public DatabaseService(List<AppComponent> lac, String out_dir) {
        super();
        
        try (Connection data_base = DriverManager.getConnection("jdbc:mariadb://localhost/", "root", null)){}

        // absolute_path = System.getProperty("user.dir") + out_dir + "/data";
        absolute_path = out_dir + "/data";

        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        Date date = new Date();


        date = formatter.format(date);

        // creates a file object with specified path
        //new File(date_dir).mkdirs();

        /*
        iniciar los .csv de todos los componentes o esperar a que llamen initDataLog
        
         */
        for(AppComponent ac : lac) {
            try {
                initDataLog(ac.nombreParametros, ac.getID());
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    

    /**
     * Debe tomar el AppComponent y guardar sus valores en su tabla respectiva en la base de datos
     * Debe guardar el timestamp también
     *
     * @param c AppComponent a guardar en la base de datos
     */
    @Override
    protected void serve(AppComponent c) {
        if(c.getID().equals("lcd")) { //no tiene sentido logear los datos de la pantalla
            return;
        }
        try {
            // INSERT double[] en su tabla, sacar el timestamp del momento en que guarda
            // en este punto ya se debio haber llamado a initDataLog con los argumentos correspondientes
            writeValues(c.getValoresRealesActuales(), c.getID());

        } catch (Exception e) {
            e.printStackTrace(); // Sólo se hace print, el sistema no se puede caer
        }
    }

    /**
     * Inicia un .csv en la carpeta "data/date/" con el nombre {ID}.csv. Le agrega una fila con los valores de values.
     *
     * @param values Valores a agregar, en general deberían ser headers.
     * @param ID     Nombre del archivo de salida
     * @throws IOException
     */
    public void initDataLog(String[] values, String ID) throws IOException { //ver cuando llamar a initDataLog
        Statement stmt = data_base.createStatement();
        String command = "CREATE TABLE " + date + ID + ".csv"; // el filename sera el mismo id?;
        String tags_definition = "(id primary key, TIMESTAMP datetime,";

        for (int i = 0; i < values.length - 1; i++) {
            tags_definition = tags_definition + values[i] + " double,";
        }
        tags_definition = tags_definition + values[values.length - 1] + " double)";
        stmt.execute(command + tags_definition);

    }

    /**
     * Añade una fila a ID.csv con los valores de "values".
     * @param values Valores a agregar.
     * @param ID Nombre del archivo a modificar.
     * @throws IOException
     */
    public void writeValues(double[] values, String ID) throws IOException, InterruptedException {
        String fileName = date_dir+"/"+ID+".csv"; // el filename sera el mismo id?;
        FileWriter fileWriter = new FileWriter(fileName, true); // append = true
        PrintWriter printWriter = new PrintWriter(fileWriter);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS"); //esto hay que revisarlo en concreto con la bd
        Date date = new Date();
        printWriter.print(formatter.format(date)+";");

        for(int i = 0; i < values.length-1; i++) {
            printWriter.printf("%f;", values[i]);
        }
        printWriter.printf("%f\n", values[values.length-1]);

        printWriter.close();
    }
}
