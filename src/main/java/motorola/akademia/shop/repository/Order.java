package motorola.akademia.shop.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class Order {

    private int id;
    private User user;
    private Cart cart;
}
