package Models;

public class Customer {

    public int id; //Id de cliente
    public Boolean membership; //Indica si el cliente tiene membresía

    public Customer(
            int id, Boolean membership) {
        this.id = id;
        this.membership = membership;
    }
}
