package Common;

import Helpers.Logger;
import Models.Customer;
import Models.Deposit;
import Models.Order;
import Models.Restaurant;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;

public class InitialLoad {

    private final Logger logger = new Logger();
    LinkedList<Restaurant> restarurants = new LinkedList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     *
     * @param path
     * @return array with file lines
     */
    String[] loadFile(String path) {
        this.logger.addLine(String.format("Executing InitialLoad.loadFile in path: %s", path));

        ArrayList<String> listaLineasArchivo = new ArrayList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String lineaActual = br.readLine();
            lineaActual = br.readLine();
            while (lineaActual != null) {
                listaLineasArchivo.add(lineaActual);
                lineaActual = br.readLine();
            }
            br.close();
            fr.close();
            this.logger.addLine(String.format("File in path: %s , loaded successful", path));
        } catch (FileNotFoundException e) {
            this.logger.addLine(String.format("File in path %s not found", path));
            e.printStackTrace();
        } catch (IOException e) {
            this.logger.addLine(String.format("Error loading file in path: %s", path));
            e.printStackTrace();
        }
        return listaLineasArchivo.toArray(new String[0]);
    }

    /**
     * @param restaurantPath
     * @param deposit
     * @return List of restaurants
     */
    public LinkedList<Restaurant> LoadRestaurant(String restaurantPath, Deposit deposit) {
        this.logger.addLine(String.format("Executing InitialLoad.LoadRestaurant"));
        try {
            String[] restaurantLines = loadFile(restaurantPath);
            for (var restaurant : restaurantLines) {
                var _restaurantLine = restaurant.split(",");
                var _restaurant = new Restaurant(
                        Integer.parseInt(
                                _restaurantLine[0]),
                        deposit);
                restarurants.add(_restaurant);
            }
        } catch (Exception e) {
            this.logger.addLine("Error loading restaurants");
            e.printStackTrace();
        }
        this.logger.addLine(String.format("%s restaurant loaded successful", restarurants.size()));
        return restarurants;
    }

    /**
     * @param ordersPath
     * @param customersPath
     * @return List of orders
     */
    public LinkedList<Order> LoadOrders(String ordersPath, String customersPath) {
        this.logger.addLine(String.format("Executing InitialLoad.LoadOrders"));

        var result = new LinkedList<Order>();
        var customers = this.LoadCustomers(customersPath);
        try {
            String[] orderLines = loadFile(ordersPath);
            for (var order : orderLines) {
                var _orderLine = order.split(",");
                var _customer = customers.stream() //Toma los clientes de la lista "customers" que se correponden al cliente del pedido
                        .filter(customer -> customer.id == Integer.parseInt(_orderLine[1]))
                        .findFirst()
                        .get();
                var _order = new Order(
                        Integer.parseInt(_orderLine[0]),
                        _customer,
                        Integer.parseInt(_orderLine[2]),
                        LocalDateTime.parse(_orderLine[3], formatter),
                        Integer.parseInt(_orderLine[4]));
                result.add(_order);
            }
        } catch (Exception e) {

            var message = "Error loading orders";
            System.out.println(message);
            this.logger.addLine(message);
            e.printStackTrace();
        }
            this.logger.addLine(String.format("%s orders loaded successful", result.size()));
        return result;
    }

    /**
     * @param customersPath
     * @return List of customers
     */
    LinkedList<Customer> LoadCustomers(String path) {
        this.logger.addLine(String.format("Executing InitialLoad.LoadCustomers"));
        var result = new LinkedList<Customer>();
        try {
            String[] customersLines = loadFile(path);
            for (var customerLine : customersLines) {
                var _customerLine = customerLine.split(",");
                var _customer = new Customer(
                        Integer.parseInt(_customerLine[0]),
                        _customerLine[1].equals("1"));
                result.add(_customer);
            }
        } catch (Exception e) {

            var message = "Error loading customers";
            System.out.println(message);
            this.logger.addLine(message);
            e.printStackTrace();
        }
        this.logger.addLine(String.format("%s customers loaded successful", result.size()));
        return result;
    }
}
