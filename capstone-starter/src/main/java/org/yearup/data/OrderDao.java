package org.yearup.data;

import org.yearup.models.ShoppingCartItem;

import java.util.List;

public interface OrderDao {
    void addToOrder(int userid, List<ShoppingCartItem> cartItems);
}
