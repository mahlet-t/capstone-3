package org.yearup.data.mysql;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    private ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String query = "SELECT * FROM shopping_cart WHERE user_id=?";
        ShoppingCart cart = new ShoppingCart();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int productId = set.getInt("product_id");
                int quantity = set.getInt("quantity");
                Product product = productDao.getById(productId);
                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(quantity);
                cart.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }

    @Override
    public void addToCart(int userid, int productId) {
        String query = "INSERT INTO shopping_Cart(user_id, product_id, quantity) VALUES(?, ?, 1)";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);
            statement.setInt(2, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int userId, int productId, int quantity) {
        String query = "Update shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void clearCart(int userID) {
        String query = "DELETE shopping_cart WHERE user_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeItem(int userId, int productId) {
        String query ="DELETE from shopping_cart WHERE user_id = ? AND product_id = ?";
        try(Connection connection=getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userId);
            statement.setInt(2,productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
