/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

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
    private Semaphore semDelivery = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);
    private LinkedList<Order> listaOrdenes = new LinkedList<Order>();

    public void agregarOrden(Order order) {
        try {
            
                semRest.acquire();
                mutex.acquire();
                listaOrdenes.add(order);
                System.out.println("Pedido nro. " + order.orderId + " ha quedado pronto para ser despachado");
                mutex.release();

                Thread.sleep(500);
           
        } catch (InterruptedException ex) {
            Logger.getLogger(Deposit.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semDelivery.release();
        }

    }

    public void despacharOrden(Delivery delivery) {
        System.out.println( "El delivery " + delivery.deliveryId + " intenta llevar una orden" );
        try {
            
                semDelivery.acquire();
                mutex.acquire();
                Order orden = this.listaOrdenes.removeFirst();
                System.out.println(orden.orderId + "Ha sido retirara por el delivery " + delivery.deliveryId);
                mutex.release();
                Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Deposit.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semRest.release();
        }
    }
}
