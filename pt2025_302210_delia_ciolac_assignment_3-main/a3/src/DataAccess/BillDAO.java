package DataAccess;

import Model.Bill;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import Connection.ConnectionFactory;

public class BillDAO {
    protected static final Logger LOGGER = Logger.getLogger(BillDAO.class.getName());

    public void insert(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO log(orderId, clientId, productId, productPrice, quantity, total) VALUES(?, ?, ?, ?, ?, ?)");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);

            //setez primul ? din sql cu ce returneaza bill.orderid()
            statement.setInt(1, bill.orderId());
            statement.setInt(2, bill.clientId());
            statement.setInt(3, bill.productId());
            statement.setInt(4, bill.productPrice());
            statement.setInt(5, bill.quantity());
            statement.setInt(6, bill.total());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public List<Bill> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Bill> list = new ArrayList<Bill>();
        String query = "SELECT * FROM log";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            int id = 0;
            int orderId = 0;
            int clientId = 0;
            int productId = 0;
            int productPrice = 0;
            int quantity = 0;
            int total = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                orderId = resultSet.getInt("orderId");
                clientId = resultSet.getInt("clientId");
                productId = resultSet.getInt("productId");
                productPrice = resultSet.getInt("productPrice");
                quantity = resultSet.getInt("quantity");
                total = resultSet.getInt("total");
            }
            Bill bill = new Bill(id, orderId, clientId, productId, productPrice, quantity, total);
            list.add(bill);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return list;
    }
}
