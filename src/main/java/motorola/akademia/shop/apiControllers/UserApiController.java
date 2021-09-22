package motorola.akademia.shop.apiControllers;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.entities.User;
import motorola.akademia.shop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/shop")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Boolean> loginToShop(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user.getUsername(), user.getPassword()));
    }

    @GetMapping("/loggedUser")
    public ResponseEntity<User> getLoggedUser() {
        return ResponseEntity.ok(userService.getLoggedUser());
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addNewUser(user));
    }

    @GetMapping("/findUser")
    public ResponseEntity<User> findUserByUsernameAndPassword(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.findUserByUsernameAndPassword(username, password));
    }

    @GetMapping("/logout")
    public ResponseEntity<User> logout() {
        userService.logout();
        return ResponseEntity.ok(userService.getLoggedUser());
    }





}
