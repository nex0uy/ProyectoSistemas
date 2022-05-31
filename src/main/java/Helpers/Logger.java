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

/**
 *
 * @author germanpujadas
 */
public class Logger {

    private static final Logger logger = new Logger();
    private final String logPath = "src/main/java/Files/Log.txt";
    private final Queue<String> logLines = new LinkedList<String>();

    public Logger() {

    }

    public void addLine(String input) {
        synchronized (logLines) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            logLines.add(dateFormat.format(date) + " | " + input + " | Reloj: " + Watch.getWatch().getWatchCounter() + "\n");
            this.save();
        }
    }

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
