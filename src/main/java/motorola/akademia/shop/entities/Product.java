package motorola.akademia.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private String unit;

    @Enumerated(value = EnumType.STRING)
    private Category category;
    private String details;


}
