package Helpers;

import Common.Watch;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class Logger {
    private final String logPath = "src/main/java/Files/Log.txt"; //Ruta destino del archivo
    private final Queue<String> logLines = new LinkedList<String>();
    
    //Genera una nueva línea y la guarda en el archivo de log
    public void addLine(String input) {
        synchronized (logLines) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            logLines.add(dateFormat.format(date) + " | " + input + " | Momento: " + Watch.getWatch().getCounter() + "\n");
            this.save();
        }
    }

    //Guarda línea
    public void save() {
        synchronized (logLines) {
            try ( BufferedWriter writer = new BufferedWriter(new FileWriter(logPath, true));) {
                while (!logLines.isEmpty()) {
                    writer.append(logLines.poll());
                }
            } catch (IOException ex) {
                System.out.println("Error saving log file");
            }
        }
    }

}
