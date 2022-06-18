package Models;

/**
 *
 * @author germanpujadas
 */
public class Customer {

    public int id;
    public Boolean membership;

    public Customer(
            int id, Boolean membership) {
        this.id = id;
        this.membership = membership;
    }
}
