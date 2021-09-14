package motorola.akademia.shop.services;

import motorola.akademia.shop.repository.User;
import motorola.akademia.shop.repository.UserRole;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class UserService {

    private User loggedUser = new User();

    List<User> users = new ArrayList<>(Arrays.asList(
            new User ("Jakub", "Domagała", "kuba", "1234",  UserRole.USER),
            new User ("Admin", "Admin", "admin", "1111",  UserRole.ADMIN))
    );

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public List<User> all() {
        return users;
    }

    public boolean login(User user){
        boolean log = false;
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())){
                log = true;
                loggedUser = u;
                break;
            }
        }
        return log;
    }

    public void logout(){
        loggedUser = new User();
    }

    public void addNewUser(User user){
        users.add(user);
    }

    public User emptyUser(){
        return new User();
    }






}
