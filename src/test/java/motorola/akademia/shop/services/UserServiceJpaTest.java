package motorola.akademia.shop.services;

import motorola.akademia.shop.ShopApplication;
import motorola.akademia.shop.entities.User;
import motorola.akademia.shop.entities.UserRole;
import motorola.akademia.shop.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceJpaTest {

    private static final User USER = new User(1L, "noName", "noSurname","user", "user",  UserRole.USER);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Test
    void shouldAddNewUserAndFindUserByUsernameAndPassword(){

        userService.addNewUser(USER);

        User newUser = userService.findUserByUsernameAndPassword(USER.getUsername(), USER.getPassword());

        assertTrue(USER.getUsername().equals(newUser.getUsername())
                && USER.getPassword().equals(newUser.getPassword())
                && USER.getName().equals(newUser.getName())
        );

    }



}