package motorola.akademia.shop.services;

import motorola.akademia.shop.entities.Category;
import motorola.akademia.shop.entities.Product;
import motorola.akademia.shop.entities.User;
import motorola.akademia.shop.entities.UserRole;
import motorola.akademia.shop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final User USER = new User(1L, "noName", "noSurname","user", "user",  UserRole.USER);


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void shouldLogUser(){

        when(userRepository.findByUsernameAndPassword(any(),any())).thenReturn(java.util.Optional.of(USER));

        userService.login(USER.getUsername(), USER.getPassword());

        assertEquals(USER, userService.getLoggedUser());
    }

    @Test
    void shouldLogOutUser(){
        when(userRepository.findByUsernameAndPassword(any(),any())).thenReturn(java.util.Optional.of(USER));

        userService.login(USER.getUsername(), USER.getPassword());
        assertEquals(USER, userService.getLoggedUser());

        userService.logout();
        assertNull(userService.getLoggedUser());
    }



}