package org.example.briefi.models;



import org.example.briefi.dbConfig.IDBConfig;
import org.example.briefi.interfaces.CategoryInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Category implements CategoryInterface {
    private int id;
    private String name;
    private String state;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void create(Category category) throws SQLException {
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "INSERT INTO categories (name, state) VALUES ( ?, ?)";

            PreparedStatement prepareStatement = this.connection.prepareStatement(req);

            prepareStatement.setString(1, category.getName());
            prepareStatement.setString(2, category.getState());
            int row = prepareStatement.executeUpdate();
            System.out.printf(String.valueOf(row));
            prepareStatement.close();
            this.connection.close();
        }
    }

    @Override
    public List<Category> list() throws SQLException {
        List<Category> categories = new ArrayList<>();
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "SELECT * FROM categories";
            PreparedStatement prepareStatement =
                    this.connection.prepareStatement(req);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setState(resultSet.getString("state"));
                categories.add(category);
            }
            prepareStatement.close();
            this.connection.close();
        }
        return categories;
    }

    @Override
    public void update(Category category) throws SQLException {
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "UPDATE categories SET name = ?, state = ? WHERE id = ?";

            PreparedStatement prepareStatement = this.connection.prepareStatement(req);

            prepareStatement.setString(1, category.getName());
            prepareStatement.setString(2, category.getState());
            prepareStatement.setInt(3, category.getId());

            int row = prepareStatement.executeUpdate();

            System.out.printf(String.valueOf(row));

            prepareStatement.close();
            this.connection.close();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        connection = IDBConfig.getConnection();
        if (connection != null) {

            String req = "DELETE FROM categories WHERE id = ?";

            PreparedStatement prepareStatement =  this.connection.prepareStatement(req);

            prepareStatement.setInt(1, id);
            int row = prepareStatement.executeUpdate();

            System.out.printf(String.valueOf(row));

            prepareStatement.close();
            this.connection.close();
        }
    }
}
