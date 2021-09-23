package motorola.akademia.shop.services;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.entities.User;
import motorola.akademia.shop.entities.UserRole;
import motorola.akademia.shop.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserService {

    private User loggedUser = new User();

    private final UserRepository userRepository;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public List<User> all() {
        return userRepository.findAll();
    }

    public boolean login(String username, String password){

        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        loggedUser=user.orElseThrow();
        return true;
    }

    public void logout(){
        loggedUser = null;
    }

    public User addNewUser(User user){
        user.setUserRole(UserRole.USER);
        userRepository.save(user);
        return user;
    }


    public User findUserByUsernameAndPassword (String username, String password){
      return userRepository.findByUsernameAndPassword(username, password).orElseThrow(()-> new RuntimeException("No user with that username and password"));
    }






}
