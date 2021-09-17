package motorola.akademia.shop.services;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.repository.Category;
import motorola.akademia.shop.repository.Product;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductListService {

    List<Product> products = new ArrayList<Product>(Arrays.asList(
            new Product(1, "Apple", new BigDecimal("2.50"), "kg", Category.FRUITS, "Jonagold"),
            new Product(2, "Lemon", new BigDecimal("8.10"), "kg", Category.FRUITS, "from Mexico"),
            new Product(3, "Banana", new BigDecimal("4.70"), "kg", Category.FRUITS, "from China"),
            new Product(4, "Plum", new BigDecimal("3.00"), "kg", Category.FRUITS, "Wegierka"),
            new Product(5, "Potato", new BigDecimal("1.50"), "kg", Category.VEGETABLES, "from Poland"),
            new Product(6, "Red pepper", new BigDecimal("6.70"), "kg", Category.VEGETABLES, "from Indie"),
            new Product(7, "Cabbage", new BigDecimal("3.00"), "szt", Category.VEGETABLES, "Chinese"),
            new Product(8, "Onion", new BigDecimal("1.00"), "szt", Category.VEGETABLES, "from Poland"),
            new Product(9, "Water 1.5L", new BigDecimal("2.50"), "szt", Category.DRINKS, "Cisowianka"),
            new Product(10, "Water 0.5L", new BigDecimal("1.50"), "szt", Category.DRINKS, "Cisowianka"),
            new Product(11, "Orange juice 1L", new BigDecimal("4.20"), "szt", Category.DRINKS, "Hortex"),
            new Product(12, "Pepsi 0.75L", new BigDecimal("3.60"), "szt", Category.DRINKS, "PepsiCo"),
            new Product(13, "Lay's Salted 150g", new BigDecimal("5.50"), "szt", Category.SNACKS, "-"),
            new Product(14, "Snickers 100g", new BigDecimal("3.30"), "szt", Category.SNACKS, "-"),
            new Product(15, "Mars 100g", new BigDecimal("3.30"), "szt", Category.SNACKS, "-"),
            new Product(16, "Popcorn 120g", new BigDecimal("2.50"), "szt", Category.SNACKS, "-"),
            new Product(17, "Large Bread", new BigDecimal("5.20"), "szt", Category.BREAD, "Wheat"),
            new Product(18, "Medium Bread", new BigDecimal("3.10"), "szt", Category.BREAD, "Wheat"),
            new Product(19, "Small Bread", new BigDecimal("3.80"), "szt", Category.BREAD, "Whole grain"),
            new Product(20, "Roll", new BigDecimal("0.40"), "szt", Category.BREAD, "Kajzerka")
    ));

    public List<Product> allToCart() {
        return products;
    }

    public List<Product> all() {
        return products.stream().filter(a -> !a.getName().substring(0,3).equals("DEL")).collect(Collectors.toList());
    }

    public Product byName(String name) {
        for (Product product : products) {
            if (name.equals(product.getName())) {
                return product;
            }
        }
        return null;
    }


    public Product productById(int id){
        for (Product product: products){
            if (id ==  product.getId()){
                return product;
            }
        }
        return null;
    }

    public List<Product> getProductsByCategory (String category){
        List<Product> categoryList = new ArrayList<>();

        for (Product p : all()) {
            if(p.getCategory().name().equals(category)){
                categoryList.add(p);
            }
        }
        return categoryList;
    }

    public Product addProduct (Product product){
        products.add(product);
        return product;
    }

    public Product deleteProductById (int id){

        Product detetedProduct = new Product();
        for (Product p : products) {
            if(p.getId()==id){
                String oldName = p.getName();
                p.setName("DELETED_" + oldName);
                detetedProduct=p;
            }
        }
        return detetedProduct;
    }



}
