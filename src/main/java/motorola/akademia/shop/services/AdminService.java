package motorola.akademia.shop.services;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.repository.Cart;
import motorola.akademia.shop.repository.Order;
import motorola.akademia.shop.repository.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@AllArgsConstructor
public class AdminService {

    private final ProductListService productListService;
    private final OrderListService orderListService;

    public Product changeProductPrice(Product product, BigDecimal newPrice) {
        for (Product p : productListService.products) {
            if (p.getId() == product.getId()) {
                product.setPrice(newPrice);
            }
        }
        return product;
    }

    public void deleteProductFromOrder(Order order, Cart cart) {
        for (Order o : orderListService.orders) {
            if (o.getId() == order.getId()) {
                o.getCart().getItemMap().remove(cart);
            }
        }
    }
}
