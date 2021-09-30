package motorola.akademia.shop.services;

import motorola.akademia.shop.entities.Cart;
import motorola.akademia.shop.entities.Category;
import motorola.akademia.shop.entities.Product;
import motorola.akademia.shop.repositories.CartRepository;
import motorola.akademia.shop.repositories.ItemRepository;
import motorola.akademia.shop.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private static final Product PRODUCT = new Product(1L, "noName", BigDecimal.TEN, "kg", Category.BREAD, "noDetails");
    private static final double DELTA = 0.00000000000001;

    @Mock
    private ProductListService productListService;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartService cartService;

    private Cart cart;

    @BeforeEach
    void setNewCart() {
        cart = new Cart(new ArrayList<>());
    }

    @Test
    void shouldAddItemToCart() {
        when(productListService.productById(any())).thenReturn(PRODUCT);

        assertTrue(cart.getItem().isEmpty());

        cart = cartService.addProductToCart(PRODUCT, 10);

        assertEquals(cart.getItem().size(), 1);
    }

    @Test
    void shouldAddSecondItemToCart() {
        when(productListService.productById(any())).thenReturn(PRODUCT);

        cart = cartService.addProductToCart(PRODUCT, 10);
        cart = cartService.addProductToCart(new Product(2L, "noName2", BigDecimal.valueOf(2), "kg", Category.FRUITS, "noDetails"), 20);

        assertEquals(2, cart.getItem().size());
    }

    @Test
    void shouldUpdateQuantityOfItem() {
        when(productListService.productById(any())).thenReturn(PRODUCT);

        cart = cartService.addProductToCart(PRODUCT, 10);
        cart = cartService.addProductToCart(PRODUCT, 10);

        assertEquals(20, cart.getItem().get(0).getQuantity());
    }

    @Test
    void shouldDeleteItemFromCart() {
        when(productListService.productById(any())).thenReturn(PRODUCT);

        cart = cartService.addProductToCart(PRODUCT, 10);
        cart = cartService.addProductToCart(new Product(2L, "noName2", BigDecimal.valueOf(2), "kg", Category.FRUITS, "noDetails"), 20);

        cart = cartService.deleteProductFromCart(2L);

        assertEquals(1, cart.getItem().size());
    }

    @Test
    void shouldChangeQuantityOfItem() {
        when(productListService.productById(any())).thenReturn(PRODUCT);

        cart = cartService.addProductToCart(PRODUCT, 10);

        cart = cartService.changeQuantityOfProduct(1L, 5);

        assertEquals(5, cart.getItem().get(0).getQuantity());
    }

    @Test
    void shouldCleanCart(){
        when(productListService.productById(any())).thenReturn(PRODUCT);

        cart = cartService.addProductToCart(PRODUCT, 10);
        assertEquals(cart.getItem().size(), 1);

        cart = cartService.clearCart();

        assertTrue(cart.getItem().isEmpty());
    }

    @Test
    void shouldGetTotalPriceOfCart(){
        when(productListService.productById(any())).thenReturn(PRODUCT);

        cart = cartService.addProductToCart(PRODUCT, 10);
        cart = cartService.addProductToCart(new Product(2L, "noName2", BigDecimal.TEN, "kg", Category.FRUITS, "noDetails"), 20);

        assertEquals(cartService.getTotalPriceOfCart(), BigDecimal.valueOf(300));
    }

    @Test
    void shouldGetTotalPriceOfCartWithReduction(){
        when(productListService.productById(any())).thenReturn(PRODUCT);

        cart = cartService.addProductToCart(PRODUCT, 10);
        cart = cartService.addProductToCart(new Product(2L, "noName2", BigDecimal.TEN, "kg", Category.FRUITS, "noDetails"), 20);

        assertEquals(cartService.specialOffer20(0.8).setScale(0, RoundingMode.FLOOR), BigDecimal.valueOf(240).setScale(0, RoundingMode.FLOOR));
    }




}