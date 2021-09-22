package motorola.akademia.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "shopOrder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToOne
    private Cart cart;

    private LocalDate DOO;

    public Order(User user, Cart cart) {
        this.user = user;
        this.cart = cart;
        this.DOO = LocalDate.now();
    }
}
