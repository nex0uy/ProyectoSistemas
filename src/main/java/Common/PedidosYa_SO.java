package Common;

/**
 *
 * @author germanpujadas
 */
public class PedidosYa_SO {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        var loader = new InitialLoad();
        String basePath = "src/main/java/Files/";
        loader.LoadRestaurant(basePath+"Restaurant.csv",
                              basePath+"Orders.csv",
                              basePath+"Customers.csv");
        loader.start();
    }
}
