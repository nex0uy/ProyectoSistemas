package Models;

public class Delivery extends Thread {

    public Deposit deposito; //Depósito al cual el delivery irá a buscar
    public int deliveryId; //Id del delivery

    public Delivery(int deliveryId, Deposit deposito) {
        this.deposito = deposito;
        this.deliveryId = deliveryId;
    }

    @Override
    public void run() {
        while (true) {
            deposito.deliverOrder(this); //El delivery va a buscar un pedido
        }
    }
}
