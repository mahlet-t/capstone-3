package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;

import org.yearup.models.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrdersController {
    private OrderDao orderDao;
    private ProfileDao profileDao
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;

    public OrdersController(OrderDao orderDao, ProfileDao profileDao,UserDao userDao, ShoppingCartDao shoppingCartDao) {
        this.orderDao = orderDao;
        this.profileDao = profileDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao=userDao;
    }

   @PostMapping
   public void addToOrder(Principal principal){
        try {
            String username= principal.getName();
            User user=userDao.getByUserName(username);
            int userid=user.getId();
           Profile profile = profileDao.getByUserId(userid);
            ShoppingCart cart=shoppingCartDao.getByUserId(userid);
          Map<Integer,ShoppingCartItem> items=cart.getItems();
            orderDao.addToOrder(profile,items);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Oops...our bad.");
        }
   }
}
