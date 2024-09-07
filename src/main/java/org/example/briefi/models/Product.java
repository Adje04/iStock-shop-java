package org.example.briefi.models;


import org.example.briefi.dbConfig.IDBConfig;
import org.example.briefi.interfaces.ProductInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Product implements ProductInterface {
    private int id;
    private String name;
    private String description;
    private int quantity;
    private int thresholdQuantity;
    private Category category;
    private Connection connection;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getThresholdQuantity() {
        return thresholdQuantity;
    }

    public void setThresholdQuantity(int thresholdQuantity) {
        this.thresholdQuantity = thresholdQuantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Product> list() throws SQLException {
        List<Product> products = new ArrayList<>();


        connection = IDBConfig.getConnection();
        if(connection != null) {

            String req = "SELECT * FROM products";
            PreparedStatement preparedStatement= this.connection.prepareStatement(req);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));

                product.setDescription(resultSet.getString("description"));
                product.setQuantity(resultSet.getInt("Quantity"));

                product.setThresholdQuantity(resultSet.getInt("thresholdQuantity"));

                products.add(product);
            }

            preparedStatement.close ();
            this.connection.close ();
        }
        return products;
    }

    public void create (Product product) throws SQLException {
        connection = IDBConfig.getConnection ();
        if (connection != null) {
            String req = "{CALL createProduct( ?, ?, ?, ?, ?)}";

            CallableStatement callableStatement = connection.prepareCall(req);

            callableStatement.setString(1, product.getName());
            callableStatement.setString(2,product.getDescription());
            callableStatement.setInt(3, product.getQuantity());
            callableStatement.setInt(4, product.getThresholdQuantity());
            callableStatement.setInt(5, product.getCategory().getId());

            callableStatement.execute();

            callableStatement.close();
            this.connection.close();
        }
    }
    public void update(Product product) throws SQLException {
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "UPDATE Products SET name = ?, description = ?, quantity = ? WHERE id = ?";

            PreparedStatement prepareStatement = this.connection.prepareStatement(req);

            prepareStatement.setString(1, product.getName());
            prepareStatement.setString(2, product.getDescription());
            prepareStatement.setInt(3, product.getQuantity());
            prepareStatement.setInt(4, product.getId());


            int row = prepareStatement.executeUpdate();

            System.out.printf(String.valueOf(row));

            prepareStatement.close();
            this.connection.close();
        }
    }

    public void delete(int id) throws SQLException {
        connection = IDBConfig.getConnection();
        if (connection != null) {

            String req = "DELETE FROM products WHERE id = ?";

            PreparedStatement prepareStatement =  this.connection.prepareStatement(req);

            prepareStatement.setInt(1, id);
            int row = prepareStatement.executeUpdate();

            System.out.printf(String.valueOf(row));

            prepareStatement.close();
            this.connection.close();
        }
    }
}