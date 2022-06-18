/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Common.Watch;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sombra
 */
public class Deposit {

    private final int MAX_LIMITE = 2;
    private Semaphore semRest = new Semaphore(MAX_LIMITE);
    Semaphore semDelivery = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);
    private LinkedList<Order> listaOrdenes = new LinkedList<Order>();

    public void agregarOrden(Order order) {
        try {
            semRest.acquire();
            mutex.acquire();
            listaOrdenes.add(order);
            System.out.println("Pedido nro. " + order.orderId + "del momento "+ order.moment +" ha quedado pronto para ser despachado");
            mutex.release();

            Thread.sleep(500);

        } catch (InterruptedException ex) {
            Logger.getLogger(Deposit.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semDelivery.release();
        }

    }

    public void despacharOrden(Delivery delivery) {
        System.out.println("El delivery " + delivery.deliveryId + " llega al deposito");
        try {

            semDelivery.acquire();
            mutex.acquire();
            Order orden = this.listaOrdenes.removeFirst();
            System.out.println("La orden nro. " + orden.orderId + " del momento "+ orden.moment + " ha sido retirada por el delivery " + delivery.deliveryId + " en el momento " + Watch.getWatch().getCounter());
            mutex.release();
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Deposit.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semRest.release();
        }
    }
}
