package motorola.akademia.shop.services;

import motorola.akademia.shop.repository.Cart;
import motorola.akademia.shop.repository.Order;
import motorola.akademia.shop.repository.User;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderListService {

    List<Order> orders = new ArrayList<>();
    private int id = 1;

    public Order addOrder(Cart cart, User user){
        Order order = new Order(id,user,cart);
        id++;
        orders.add(order);
        return order;
    }

    public List<Order> all() {
        return orders;
    }

    public List<Order> getOrdersByUser (User user){
        List<Order> ordersByUser = new ArrayList<>();
        for (Order o : orders) {
            if (o.getUser().getUsername().equals(user.getUsername())){
                ordersByUser.add(o);
            }

        }
        return ordersByUser;
    }

    public Order getOrdersById (int id){
        Order order = null;
        for (Order o : orders) {
            if (o.getId()==id){
                order=o;
            }

        }
        return order;
    }

    public BigDecimal getTotalPriceFromOrder(Order order){
            return order.getCart().getItemMap().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
