package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.util.HashMap;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController {
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            // use the shoppingCartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{id}")
    public ShoppingCart addToCart(@PathVariable int id, Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userid = user.getId();
            shoppingCartDao.addToCart(userid, id);

            return shoppingCartDao.getByUserId(userid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops...our bad.");
        }
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{id}")
    public ShoppingCart update(@PathVariable int id, @RequestBody HashMap<String, Integer> requestBody, Principal principal) {
        try {
            int quantity = requestBody.get("quantity");
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userid = user.getId();

            shoppingCartDao.update(userid, id, quantity);
            return shoppingCartDao.getByUserId(userid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops.....our bad.");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    public ShoppingCart clearAll(Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userid = user.getId();
            shoppingCartDao.clearCart(userid);
            return shoppingCartDao.getByUserId(userid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops....our bad.");

        }
    }

    @DeleteMapping("{id}")
    public ShoppingCart removeItem(@PathVariable int id, Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userid = user.getId();
            shoppingCartDao.removeItem(userid, id);
            return shoppingCartDao.getByUserId(userid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad");
        }
    }

}
