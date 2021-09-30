package motorola.akademia.shop.apiControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import motorola.akademia.shop.entities.Category;
import motorola.akademia.shop.entities.Product;
import motorola.akademia.shop.services.ProductListService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductListService productListService;

    @Autowired
    private MappingJackson2HttpMessageConverter mapper;

    @Test
    void shouldGetAllProducts() throws Exception {

        Product product = new Product(21L, "noName", BigDecimal.valueOf(1), "kg", Category.VEGETABLES, "noDetails");
        Product product2 = new Product(22L, "noName2", BigDecimal.valueOf(1), "kg", Category.FRUITS, "noDetails");
        List<Product> products = Arrays.asList(product, product2);

        Mockito.when(productListService.all()).thenReturn(products);
        //when - then

        mvc.perform(get("/api/shop/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.getObjectMapper().writeValueAsString(products)));

    }

    @Test
    void shouldAddNewProduct() throws Exception {
        Product product = new Product(25L, "noName", BigDecimal.valueOf(1), "kg", Category.VEGETABLES, "noDetails");

        Mockito.when(productListService.addProduct(any())).thenReturn(product);

        mvc.perform(post("/api/shop/products")
                .content(mapper.getObjectMapper().writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        .andExpect(content().json(mapper.getObjectMapper().writeValueAsString(product)));
    }


    @Test
    void shouldGetProductByName() throws Exception {
        Product product = new Product(25L, "noName", BigDecimal.valueOf(1), "kg", Category.VEGETABLES, "noDetails");

        Mockito.when(productListService.byName(any())).thenReturn(product);

        mvc.perform(get("/api/shop/productByName?name="+product.getName()))
                .andExpect(status().isOk())
        .andExpect(jsonPath("details").value("noDetails"))
        .andExpect(jsonPath("id").value(25L));
    }

    @Test
    void shouldDeleteProductById() throws Exception {
        Product product = new Product(25L, "noName", BigDecimal.valueOf(1), "kg", Category.VEGETABLES, "noDetails");

        Mockito.when(productListService.deleteProductById(product.getId())).thenReturn(product);

        mvc.perform(delete("/api/shop/products?id=" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("details").value("noDetails"))
                .andExpect(jsonPath("id").value(25L));
    }



}