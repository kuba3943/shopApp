package motorola.akademia.shop.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Product {

    private int id;
    private String name;
    private BigDecimal price;
    private String unit;
    private Category category;
    private String details;


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", category=" + category +
                ", details='" + details + '\'' +
                '}';
    }
}
