package motorola.akademia.shop.controller;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.repository.*;
import motorola.akademia.shop.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;


@Controller
@AllArgsConstructor
public class ViewController {

    private final AdminService adminService;
    private final CartService cartService;
    private final OrderListService orderListService;
    private final ProductListService productListService;
    private final UserService userService;



    @GetMapping("/")
    public String loginPage (Model model){
        model.addAttribute("user", userService.emptyUser());

        return "login";
    }

    @GetMapping("/logout")
    public String logout (Model model){

        userService.logout();
        cartService.clearCart();
        model.addAttribute("user", userService.emptyUser());
        return "login";
    }

    @PostMapping("/login")
    public String productList (@ModelAttribute User user, Model model){

        User user1 = userService.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());

       if (userService.login(user) && user1.getUserRole()==UserRole.USER){
           return "redirect:products";
       } else if (userService.login(user) && user1.getUserRole()==UserRole.ADMIN){
           model.addAttribute("products", productListService.all());
           return "adminProducts";
        }else {
           model.addAttribute("user", userService.emptyUser());
           return "loginerror";
       }

    }


    @GetMapping("/addUser")
    public String register(Model model) {

        model.addAttribute("user", new User());

        return "addUser";

    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user, Model model) {

        user.setUserRole(UserRole.USER);

        userService.addNewUser(user);

        return "/login";

    }






    @GetMapping("/products")
    public String showList(Model model) {

        model.addAttribute("products", productListService.all());
        model.addAttribute("item", new Cart.Item());

        model.addAttribute("categories", Category.values());
        model.addAttribute("catName", new Name());

        return "list";
    }



    @GetMapping("/product_details")
    public String showDetails(Model model, @RequestParam(value = "name") String name) {

        model.addAttribute("product", productListService.byName(name));

        return "details";
    }


    @PostMapping("/addToCart")
    public String addToCart(Model model, @ModelAttribute Cart.Item item, @RequestParam(value = "id") int id) {

        cartService.addProductToCart(id, item.getQuantity());

        model.addAttribute("products", productListService.all());
        model.addAttribute("item", new Cart.Item());

        return "redirect:products";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {

        model.addAttribute("items", cartService.all());
        model.addAttribute("totalPrice", cartService.getTotalPriceOfCart().setScale(2, RoundingMode.CEILING));
        model.addAttribute("totalPrice20", cartService.specialOffer20(0.8).setScale(2, RoundingMode.CEILING));
        model.addAttribute("products", productListService.allToCart());


        return "cart";
    }

    @PostMapping("/orders")
    public String orders(Model model) {

        cartService.specialOffer20(0.8);
        Cart cart = new Cart(cartService.all());


        User user = userService.getLoggedUser();
        orderListService.addOrder(cart,user);

        cartService.clearCart();

        return "redirect:products";
    }

    @GetMapping("/orderList")
    public String orderList(Model model) {

        User user1 = userService.findUserByUsernameAndPassword(userService.getLoggedUser().getUsername(), userService.getLoggedUser().getPassword());


        if (userService.login(user1) && user1.getUserRole()==UserRole.USER){
            model.addAttribute("orders", orderListService.getOrdersByUser(userService.getLoggedUser().getUsername()));
            return "orders";
        } else if (userService.login(user1) && user1.getUserRole()==UserRole.ADMIN){
            model.addAttribute("orders", orderListService.all());
            return "ordersA";
        }else {
            return "login";
        }

    }

    @PostMapping("/orderDetails")
    public String orderDetails(Model model, @RequestParam(value = "id") int id) {

        model.addAttribute("cart", orderListService.getOrdersById(id).getCart());
        model.addAttribute("products", productListService.allToCart());
        model.addAttribute("totalPrice20", orderListService.specialOffer20(id,0.8));
        model.addAttribute("totalPrice", orderListService.getTotalPriceFromOrder(orderListService.getOrdersById(id)));

        return "orderDetails";
    }

    @PostMapping("/modify")
    public String modifyCart(Model model, @RequestParam(value = "id") int id) {

        Cart.Item itemToModify=null;

        for (Cart.Item item : cartService.all().keySet()) {
            if (item.getProductId()==id){
                itemToModify=item;
            }
        }

        cartService.deleteProductFromCart(id);

        model.addAttribute("itemToModify", itemToModify);
        model.addAttribute("products", productListService.allToCart());
        model.addAttribute("item", new Cart.Item());


        return "modifyCart";
    }

    @PostMapping("/delete")
    public String deleteProductFromCart(Model model, @RequestParam(value = "id") int id) {

        cartService.deleteProductFromCart(id);
        return "redirect:cart";
    }

    @PostMapping("/modifyProduct")
    public String modifyProduct(Model model, @RequestParam(value = "id") int id) {

      model.addAttribute("product", productListService.productById(id));
      model.addAttribute("newProduct", new Product());

        return "modifyProduct";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(Model model, @RequestParam(value = "id") int id, @ModelAttribute Product newProduct) {

        Product productToModify = productListService.productById(id);

        if (newProduct.getPrice()!= null){
        productToModify.setPrice(newProduct.getPrice());}

        if (newProduct.getDetails()!=null){
        productToModify.setDetails(newProduct.getDetails());}

        model.addAttribute("products", productListService.all());
        return "adminProducts";
    }

    @GetMapping("/adminProducts")
    public String showListAdmin(Model model) {

        model.addAttribute("products", productListService.all());

        return "adminProducts";
    }

    @PostMapping("/orderDetailsA")
    public String orderDetailsA(Model model, @RequestParam(value = "id") int id) {

        model.addAttribute("order", orderListService.getOrdersById(id));
        model.addAttribute("item", new Cart.Item());
        model.addAttribute("cart", orderListService.getOrdersById(id).getCart());
        model.addAttribute("products", productListService.allToCart());

        model.addAttribute("totalPrice", orderListService.getTotalPriceFromOrder(orderListService.getOrdersById(id)));
        model.addAttribute("totalPrice20", orderListService.specialOffer20(id,0.8));

        return "orderDetailsAd";
    }


    @GetMapping("/orderDetailsA")
    public String getOrderDetailsA(Model model, @RequestParam(value = "id") int id) {

        model.addAttribute("order", orderListService.getOrdersById(id));
        model.addAttribute("item", new Cart.Item());
        model.addAttribute("cart", orderListService.getOrdersById(id).getCart());
        model.addAttribute("products", productListService.allToCart());

        model.addAttribute("totalPrice", orderListService.getTotalPriceFromOrder(orderListService.getOrdersById(id)));
        model.addAttribute("totalPrice20", orderListService.specialOffer20(id,0.8));

        return "orderDetailsAd";
    }

    @PostMapping("/updateItem/prodid/{prodId}/orderid/{orderId}")
    public String updateItem(Model model, @ModelAttribute Cart.Item item, @PathVariable int prodId, @PathVariable int orderId) {

        orderListService.getOrdersById(orderId).getCart().getItemMap().keySet().forEach(i ->{
            if (i.getProductId()==prodId){
                i.setQuantity(item.getQuantity());
            }
        });

        cartService.updateTotalPriceWhenChangeQuantity(orderListService.getOrdersById(orderId).getCart());

        model.addAttribute("order", orderListService.getOrdersById(orderId));
        model.addAttribute("item", new Cart.Item());
        model.addAttribute("cart", orderListService.getOrdersById(orderId).getCart());
        model.addAttribute("products", productListService.allToCart());
        model.addAttribute("totalPrice", orderListService.getTotalPriceFromOrder(orderListService.getOrdersById(orderId)));
        model.addAttribute("totalPrice20", orderListService.specialOffer20(orderId,0.8));
        return "redirect:http://localhost:8080/orderDetailsA?id=" +orderId ;
    }

    @PostMapping("/sortByCategory")
    public String sortByCategory(Model model, @ModelAttribute Name catName) {


        if(catName.getName().equals("ALL")){
            model.addAttribute("products", productListService.all());
        } else{
            model.addAttribute("products", productListService.getProductsByCategory(catName.getName()));
        }

        model.addAttribute("item", new Cart.Item());

        model.addAttribute("categories", Category.values());
        model.addAttribute("catName", new Name());

        return "list";
    }

    @GetMapping("/addNewProduct")
    public String addNewProduct(Model model) {

        model.addAttribute("product", new Product());
        model.addAttribute("categories", Category.values());

        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(Model model, @ModelAttribute Product product ) {

        product.setId(productListService.all().stream().mapToInt(a -> a.getId()).max().getAsInt()+1);

        productListService.addProduct(product);

        model.addAttribute("products", productListService.all());

        return "adminProducts";
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(Model model, @RequestParam(value = "id") int id) {

       productListService.deleteProductById(id);
        model.addAttribute("products", productListService.all());
        return "adminProducts";
    }






}
