package org.yearup.data;

import org.yearup.models.Profile;
import org.yearup.models.ShoppingCartItem;

import java.util.Map;

public interface OrderDao {
    void addToOrder(Profile profile, Map<Integer, ShoppingCartItem> cartItems);
}
