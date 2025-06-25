package org.yearup.data;

import org.yearup.models.ShoppingCartItem;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    void addToOrder(int userid, Map<Integer,ShoppingCartItem> cartItems);
}
