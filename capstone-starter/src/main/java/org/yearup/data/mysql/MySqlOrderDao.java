package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

import java.util.Map;
@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {
    private ShoppingCartDao shoppingCartDao;
    public MySqlOrderDao(DataSource dataSource,ShoppingCartDao shoppingCartDao) {
        super(dataSource);
        this.shoppingCartDao=shoppingCartDao;
    }

    @Override
    public void addToOrder( Profile profile ,Map<Integer, ShoppingCartItem> cartItems) {
        String query="INSERT INTO orders(user_id, date, address, city, state, zip, shipping_amount) VALUES(?, ?, ?, ?, ?, ?, ?);";
        try(Connection connection=getConnection()){
            PreparedStatement statement= connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,profile.getUserId());
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setString(3, profile.getAddress());
            statement.setString(4, profile.getCity());
            statement.setString(5, profile.getState());
            statement.setString(6, profile.getZip());
            statement.setBigDecimal(7, BigDecimal.ZERO);
            statement.executeUpdate();
            ResultSet set= statement.getGeneratedKeys();
            int orderId = 0;
            if (set.next()){
              orderId= set.getInt(1);
            }
            for (ShoppingCartItem item: cartItems.values()){
                String itemQuery="INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount) VALUES(?, ?, ?, ?, ?);";
                PreparedStatement stm= connection.prepareStatement(itemQuery,PreparedStatement.RETURN_GENERATED_KEYS);
                stm.setInt(1,orderId);
                stm.setInt(2,item.getProductId());
                stm.setBigDecimal(3,item.getProduct().getPrice());
                stm.setInt(4,item.getQuantity());
                stm.setBigDecimal(5,item.getDiscountPercent());
                stm.executeUpdate();
                ResultSet key=stm.getGeneratedKeys();
                if (key.next()){
                  set.getInt(1);
                }
            }
            shoppingCartDao.clearCart(profile.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
