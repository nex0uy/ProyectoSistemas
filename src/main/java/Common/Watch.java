package Common;

import Helpers.Logger;

/**
 *
 * @author germanpujadas
 */
public class Watch extends Thread {
    
    private static final Watch watch = new Watch();
    
    //Contador para medir y consultar los instantes de tiempo
    private Integer counter = 1;
    //Contador utilizado para determinar si todos los demas hilos terminaron su ciclo para pasar a la siguiente unidad
    private Integer checkHilosUnidad = 0;
    private Logger logger = new Logger();
    
    public static Watch getWatch() {
        return watch;
    }
     
    public Integer getWatchCounter(){
        synchronized(counter){
            return counter;
        }
    }
    
    public void incrementWatchCount(){
        synchronized (checkHilosUnidad){
            checkHilosUnidad++;
        }
    }

    @Override
    public void run() {
        
        while (true){            
            synchronized (checkHilosUnidad){
                if (checkHilosUnidad == 10){
                    checkHilosUnidad = 0;                    
                    synchronized (this){
                        counter++;
                        System.out.println("-----------UNIDAD RELOJ: "+counter+"-------------");
                        logger.addLine("------------- UNIDAD DE RELOJ " + counter + "-------------");
                        this.notifyAll();
                    }
                    
                }
            }           
            
        }                  
    }
}
