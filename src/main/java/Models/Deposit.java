package Models;

import Common.Watch;
import Helpers.Logger;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Deposit {

    private static final Logger logger = new Helpers.Logger(); //Instancia de logger
    private final int MAX_LIMITE = 2; //Indica la cantidad máxima de peidos que pueden haber
    private Semaphore semRest = new Semaphore(MAX_LIMITE); //Intancia semáforo para restaurant
    Semaphore semDelivery = new Semaphore(0); //Intancia semáforo para delivery
    private Semaphore mutex = new Semaphore(1); //Instancia de semáforo para exclusión mutua
    private LinkedList<Order> listaOrdenes = new LinkedList<Order>();
    
    //Agregar pedido para ser depachada
    public void addOrder(Order order) {
        try {
            semRest.acquire(); //Tomo el semáforo del restaurant
            mutex.acquire(); //Tomo el semáforo del depósito
            listaOrdenes.add(order);
            var message = "Pedido nro. " + order.orderId + "del momento "+ order.moment +" ha quedado pronto para ser despachado";
            System.out.println(message);
            this.logger.addLine(message);
            mutex.release(); //Libero semáforo del depósito

            Thread.sleep(500);

        } catch (InterruptedException ex) {
            this.logger.addLine(ex.getMessage());
            ex.printStackTrace();
        } finally {
            semDelivery.release(); //Libero semáforo del delivery
        }

    }

    //Entrega pedido a cliente
    public void deliverOrder(Delivery delivery) {
        var message = "El delivery " + delivery.deliveryId + " llega al deposito";
        System.out.println(message);
        this.logger.addLine(message);
        try {

            semDelivery.acquire(); //Tomo semáforo del delivery
            mutex.acquire(); //Tomo el semáforo del depósito
            Order orden = this.listaOrdenes.removeFirst();
            message = "La orden nro. " + orden.orderId + " del momento "+ orden.moment + " ha sido retirada por el delivery " + delivery.deliveryId + " en el momento " + Watch.getWatch().getCounter();
            System.out.println(message);
            this.logger.addLine(message);
            mutex.release(); //Libero semáforo del depósito
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            this.logger.addLine(ex.getMessage());
            ex.printStackTrace();
        } finally {
            semRest.release(); //Libero semáforo del restaurant
        }
    }
}
