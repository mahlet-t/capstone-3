package org.yearup.data.mysql;

import org.yearup.data.OrderDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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
            statement.setDate(2,java.sql.Date.valueOf(LocalDate.now()));
            statement.setString(3, profile.getAddress());
            statement.setString(4, profile.getCity());
            statement.setString(5, profile.getState());
            statement.setString(6, profile.getZip());
            statement.setBigDecimal(7, BigDecimal.ZERO);
            statement.executeUpdate();
            ResultSet set= statement.getGeneratedKeys();
            if (set.next()){
                int orderId=set.getInt(1);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
