package motorola.akademia.shop.apiControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import motorola.akademia.shop.entities.Category;
import motorola.akademia.shop.entities.Product;
import motorola.akademia.shop.services.ProductListService;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductApiControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductListService productListService;

    @Autowired
    private MappingJackson2HttpMessageConverter mapper;

    @AfterEach
    void afterEach(TestInfo testInfo) {
        if(testInfo.getTags().contains("CleanUp")) {
            productListService.deleteProductById(21L);
        }
    }


    @Test
    void shouldGetAllProducts() throws Exception {

        mvc.perform(get("/api/shop/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(20)));
    }

    @Test
    void shouldGetProductById() throws Exception {

            mvc.perform(get("/api/shop/productById?id=1" )
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("name").value("Apple"))
                    .andExpect(jsonPath("id").value(1));

    }

    @Test
    @Tag("CleanUp")
    void shouldAddNewProduct() throws Exception {
        Product product = new Product(25L, "noName", BigDecimal.valueOf(1), "kg", Category.VEGETABLES, "noDetails");

        mvc.perform(post("/api/shop/products")
                .content(mapper.getObjectMapper().writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("noName"))
                .andExpect(jsonPath("details").value("noDetails"));
    }


}
