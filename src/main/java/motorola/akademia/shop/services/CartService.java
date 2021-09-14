package motorola.akademia.shop.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import motorola.akademia.shop.repository.Cart;
import motorola.akademia.shop.repository.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CartService {

    private final ProductListService productListService;

    private final Cart cart = new Cart(new HashMap<>());


    public Cart addProductToCart(int productId, int quantity){
        Cart.Item item = new Cart.Item(productId,quantity);
        BigDecimal totalItemPrice = new BigDecimal(String.valueOf(productListService.productById(productId).getPrice().multiply(BigDecimal.valueOf(quantity))));
        cart.getItemMap().put(item,totalItemPrice);
        return cart;
    }

    public Cart deleteProductFromCart(Cart.Item item){
        cart.getItemMap().remove(item);
        return cart;
    }

    public Cart changeQuantityOfProduct (int productId, int newQuantity){

        cart.getItemMap().forEach((k,v) -> {
            if (k.getProductId()==productId){
                k.setQuantity(newQuantity);
            } });
        return cart;
    }

    public Cart clearCart(){
        cart.setItemMap(new HashMap<>());
        return cart;
    }

    public Map<Cart.Item, BigDecimal> all() {
        return cart.getItemMap();
    }

    public BigDecimal getTotalPriceOfCart(){
       return cart.getItemMap().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
