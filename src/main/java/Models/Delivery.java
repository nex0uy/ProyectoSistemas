/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Sombra
 */
public class Delivery extends Thread {
    
    public Deposit deposito;
    public int deliveryId;
    
    public Delivery(int deliveryId, Deposit deposito){
        this.deposito = deposito;
        this.deliveryId = deliveryId;
    }
        @Override
    public void run() {
        while(true){
            deposito.despacharOrden(this);
        }
    }
}
