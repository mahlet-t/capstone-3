package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    void addToCart(int userid,int productId);
    ShoppingCart update(int userId, int productId,int quantity);
   void clearCart(int userID);
    void removeItem(int userId, int productId);
}
