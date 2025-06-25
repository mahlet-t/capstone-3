package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrdersController {
    private OrderDao orderDao;
    private UserDao userDao;
    private ShoppingCartDao shoppingCartDao;

    public OrdersController(OrderDao orderDao, UserDao userDao, ShoppingCartDao shoppingCartDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
    }



   @PostMapping
   public void addToOrder(Principal principal){
        try {
            String username= principal.getName();
            User user=userDao.getByUserName(username);
            int userid= user.getId();
            ShoppingCart cart=shoppingCartDao.getByUserId(userid);
          Map<Integer,ShoppingCartItem> items=cart.getItems();
            orderDao.addToOrder(userid,items);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Oops...our bad.");
        }
   }
}
