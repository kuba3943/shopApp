package motorola.akademia.shop.controller;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.entities.User;
import motorola.akademia.shop.entities.*;
import motorola.akademia.shop.repositories.CartRepository;
import motorola.akademia.shop.repositories.ItemRepository;
import motorola.akademia.shop.repositories.ProductRepository;
import motorola.akademia.shop.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;


@Controller
@AllArgsConstructor
public class ViewController {

    //private final AdminService adminService;
    private final CartService cartService;
    private final OrderListService orderListService;
    private final ProductListService productListService;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    @GetMapping("/")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());

        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {

        userService.logout();
        cartService.clearCart();
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String productList(@ModelAttribute User user, Model model) {

        User user1 = userService.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());

        if (userService.login(user.getUsername(), user.getPassword()) && user1.getUserRole() == UserRole.USER) {
            return "redirect:products";
        } else if (userService.login(user.getUsername(), user.getPassword()) && user1.getUserRole() == UserRole.ADMIN) {
            List<Product> products = productListService.all();
            products.sort(Comparator.comparingLong(Product::getId));
            model.addAttribute("products", products);
            return "adminProducts";
        } else {
            model.addAttribute("user", new User());
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

        userService.addNewUser(user);

        return "/login";

    }


    @GetMapping("/products")
    public String showList(Model model) {

        List<Product> products = productListService.all();
        products.sort(Comparator.comparingLong(Product::getId));
        model.addAttribute("products", products);

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
    public String addToCart(Model model, @ModelAttribute Cart.Item item, @RequestParam(value = "id") Long id) {

        cartService.addProductToCart(productListService.productById(id), item.getQuantity());

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


        Cart cart = new Cart(cartService.all());
        User user = userService.getLoggedUser();

        User user1 = userService.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        cartService.addCartToDB(cart);
        orderListService.addOrder(cart, user1);

        cartService.clearCart();

        return "redirect:products";
    }

    @GetMapping("/orderList")
    public String orderList(Model model) {

        User user1 = userService.findUserByUsernameAndPassword(userService.getLoggedUser().getUsername(), userService.getLoggedUser().getPassword());


        if (userService.login(user1.getUsername(), user1.getPassword()) && user1.getUserRole() == UserRole.USER) {
            model.addAttribute("orders", orderListService.getOrdersByUser(userService.getLoggedUser().getUsername()));
            return "orders";
        } else if (userService.login(user1.getUsername(), user1.getPassword()) && user1.getUserRole() == UserRole.ADMIN) {
            model.addAttribute("orders", orderListService.all());
            return "ordersA";
        } else {
            return "login";
        }

    }

    @PostMapping("/orderDetails")
    public String orderDetails(Model model, @RequestParam(value = "id") Long id) {

        model.addAttribute("cart", orderListService.getOrdersById(id).getCart());
        model.addAttribute("products", productListService.allToCart());
        model.addAttribute("totalPrice20", orderListService.specialOffer20(id, 0.8));
        model.addAttribute("totalPrice", orderListService.getTotalPriceFromOrder(orderListService.getOrdersById(id)));

        return "orderDetails";
    }

    @PostMapping("/modify")
    public String modifyCart(Model model, @RequestParam(value = "id") Long id) {

        Cart.Item itemToModify = null;

        for (Cart.Item item : cartService.all()) {
            if (item.getProduct().getId().equals(id)) {
                itemToModify = item;
            }
        }

        cartService.deleteProductFromCart(id);

        model.addAttribute("itemToModify", itemToModify);
        model.addAttribute("products", productListService.allToCart());
        model.addAttribute("item", new Cart.Item());


        return "modifyCart";
    }

    @PostMapping("/delete")
    public String deleteProductFromCart(Model model, @RequestParam(value = "id") Long id) {

        cartService.deleteProductFromCart(id);
        return "redirect:cart";
    }

    @PostMapping("/modifyProduct")
    public String modifyProduct(Model model, @RequestParam(value = "id") Long id) {

        model.addAttribute("product", productListService.productById(id));
        model.addAttribute("newProduct", new Product());

        return "modifyProduct";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(Model model, @RequestParam(value = "id") Long id, @ModelAttribute Product newProduct) {

        Product productToModify = productListService.productById(id);

        if (newProduct.getPrice() != null) {
            productToModify.setPrice(newProduct.getPrice());
        }

        productRepository.save(productToModify);

        List<Order> ordersToUpdate = orderListService.getAllOrdersWithProduct(id);

        for (Order o : ordersToUpdate) {
            cartService.updateTotalPriceWhenChangeQuantity(o.getCart());
            cartRepository.save(o.getCart());
        }

        if (newProduct.getDetails() != null) {
            productToModify.setDetails(newProduct.getDetails());
        }
        List<Product> products = productListService.all();
        products.sort(Comparator.comparingLong(Product::getId));
        model.addAttribute("products", products);

        return "adminProducts";
    }

    @GetMapping("/adminProducts")
    public String showListAdmin(Model model) {

        List<Product> products = productListService.all();
        products.sort(Comparator.comparingLong(Product::getId));
        model.addAttribute("products", products);

        return "adminProducts";
    }

    @PostMapping("/orderDetailsA")
    public String orderDetailsA(Model model, @RequestParam(value = "id") Long id) {

        model.addAttribute("order", orderListService.getOrdersById(id));
        model.addAttribute("item", new Cart.Item());
        model.addAttribute("cart", orderListService.getOrdersById(id).getCart());
        model.addAttribute("products", productListService.allToCart());

        model.addAttribute("totalPrice", orderListService.getTotalPriceFromOrder(orderListService.getOrdersById(id)));
        model.addAttribute("totalPrice20", orderListService.specialOffer20(id, 0.8));

        return "orderDetailsAd";
    }


    @GetMapping("/orderDetailsA")
    public String getOrderDetailsA(Model model, @RequestParam(value = "id") Long id) {

        model.addAttribute("order", orderListService.getOrdersById(id));
        model.addAttribute("item", new Cart.Item());
        model.addAttribute("cart", orderListService.getOrdersById(id).getCart());
        model.addAttribute("products", productListService.allToCart());

        model.addAttribute("totalPrice", orderListService.getTotalPriceFromOrder(orderListService.getOrdersById(id)));
        model.addAttribute("totalPrice20", orderListService.specialOffer20(id, 0.8));

        return "orderDetailsAd";
    }

    @PostMapping("/updateItem/prodid/{prodId}/orderid/{orderId}")
    public String updateItem(Model model, @ModelAttribute Cart.Item item, @PathVariable Long prodId, @PathVariable Long orderId) {

        orderListService.getOrdersById(orderId).getCart().getItem().forEach(i -> {
            if (i.getProduct().getId().equals(prodId)) {
                i.setQuantity(item.getQuantity());
                itemRepository.save(i);
            }
        });

        Cart cart = orderListService.getOrdersById(orderId).getCart();
        cartService.updateTotalPriceWhenChangeQuantity(cart);

        cartRepository.save(cart);


        model.addAttribute("order", orderListService.getOrdersById(orderId));
        model.addAttribute("item", new Cart.Item());
        model.addAttribute("cart", orderListService.getOrdersById(orderId).getCart());
        model.addAttribute("products", productListService.allToCart());
        model.addAttribute("totalPrice", orderListService.getTotalPriceFromOrder(orderListService.getOrdersById(orderId)));
        model.addAttribute("totalPrice20", orderListService.specialOffer20(orderId, 0.8));
        return "redirect:http://localhost:8080/orderDetailsA?id=" + orderId;
    }

    @PostMapping("/sortByCategory")
    public String sortByCategory(Model model, @ModelAttribute Name catName) {


        if (catName.getName().equals("ALL")) {
            model.addAttribute("products", productListService.all());
        } else {
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
    public String addProduct(Model model, @ModelAttribute Product product) {

        productListService.addProduct(product);

        List<Product> products = productListService.all();
        products.sort(Comparator.comparingLong(Product::getId));
        model.addAttribute("products", products);


        return "adminProducts";
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(Model model, @RequestParam(value = "id") Long id) {

        productListService.deleteProductById(id);
        model.addAttribute("products", productListService.all());
        return "adminProducts";
    }


}
