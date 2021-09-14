package motorola.akademia.shop.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class User {

    private String name;
    private String surname;
    private String username;
    private String password;
    private UserRole userRole;

}
