package motorola.akademia.shop.services;

import motorola.akademia.shop.entities.Category;
import motorola.akademia.shop.entities.Product;
import motorola.akademia.shop.repositories.ProductRepository;
import motorola.akademia.shop.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductListServiceTest {

    private static final Product PRODUCT = new Product(21L, "noName", BigDecimal.valueOf(1),"kg", Category.VEGETABLES, "noDetails");


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductListService productListService;

    @Test
    void shouldGetAllProducts() {

        List<Product> allProducts = productListService.all();
        assertEquals(20, allProducts.size());

    }

    @Test
    void shouldGetProductbyName() {

        Product product = productListService.byName("Lemon");
        assertEquals("Lemon", product.getName());
    }

    @Test
    void shouldThrowExceptionWhenThereAreNotProductByName() {

        Assertions.assertThrows(RuntimeException.class, ()-> productListService.byName("Orange"));
    }

    @Test
    void shouldGetProductsByCategory() {
        List<Product> productsByCategory = productListService.getProductsByCategory("FRUITS");

        assertEquals(4, productsByCategory.size());
        assertTrue(productsByCategory.stream().allMatch(p -> p.getCategory()== Category.FRUITS));
    }

    @Test
    void addProduct() {

        productListService.addProduct(PRODUCT);
        assertEquals(21, productListService.all().size());
        assertTrue(productListService.productById(21L).getName().equals("noName"));
    }

}