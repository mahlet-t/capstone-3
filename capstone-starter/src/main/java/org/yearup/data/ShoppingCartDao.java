package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    void addToCart(int userid,int productId);
    void update(int userId, int productId,int quantity);
   void clearCart(int userID);
    void removeItem(int userId, int productId);
}
