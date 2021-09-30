package motorola.akademia.shop.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import motorola.akademia.shop.repositories.CartRepository;
import motorola.akademia.shop.repositories.ItemRepository;
import motorola.akademia.shop.repositories.ProductRepository;
import motorola.akademia.shop.entities.Cart;
import motorola.akademia.shop.entities.Product;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@SessionScope
public class CartService {

    private final ProductListService productListService;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final Cart cart = new Cart(new ArrayList<>());

    public Cart addProductToCart(Product product, int quantity) {
        BigDecimal totalItemPrice = new BigDecimal(String.valueOf(productListService.productById(product.getId()).getPrice().multiply(BigDecimal.valueOf(quantity))));

        boolean b = false;
        for (Cart.Item item : cart.getItem()) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                BigDecimal newTotalItemPrice = new BigDecimal(String.valueOf(productListService.productById(product.getId()).getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
               item.setPrice(newTotalItemPrice);
                itemRepository.save(item);
                b = true;
            }
        }

        if (!b) {
            Cart.Item item = new Cart.Item(product, quantity, totalItemPrice);
            cart.getItem().add(item);
            itemRepository.save(item);
        }
        return cart;
    }

    public Cart deleteProductFromCart(Long id) {

        for (Cart.Item item : cart.getItem()) {
            if (item.getProduct().getId().equals(id)) {
                cart.getItem().remove(item);
                itemRepository.delete(item);
                break;
            }
        }
        return cart;
    }

    public Cart changeQuantityOfProduct(Long productId, int newQuantity) {
        for (Cart.Item i : cart.getItem()) {
            if (i.getProduct().getId().equals(productId)) {
                i.setQuantity(newQuantity);
            }
        }
        return cart;
    }

    public Cart clearCart() {
        cart.setItem(new ArrayList<>());
        return cart;
    }

    public List<Cart.Item> all() {
        return cart.getItem();
    }

    public BigDecimal getTotalPriceOfCart() {
        return cart.getItem().stream().map(Cart.Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal specialOffer20(double reduction) {

        BigDecimal totalPrice20 = new BigDecimal(BigInteger.valueOf(0));


        for (Cart.Item i : cart.getItem()) {
            if (i.getQuantity() >= 5) {
                totalPrice20 = totalPrice20.add(i.getPrice().multiply(BigDecimal.valueOf(reduction)));
            } else {
                totalPrice20 = totalPrice20.add(i.getPrice());
            }
        }
        return totalPrice20;
    }


    public void updateTotalPriceWhenChangeQuantity(Cart cart) {

        cart.getItem().forEach(s ->
                s.setPrice(productListService.productById(s.getProduct().getId()).getPrice().multiply((BigDecimal.valueOf(s.getQuantity())))));
    }

    public void addCartToDB(Cart cart) {
        cartRepository.save(cart);
    }


}
