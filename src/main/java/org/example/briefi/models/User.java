package org.example.briefi.models;


import org.example.briefi.dbConfig.IDBConfig;
import org.example.briefi.interfaces.AuthInterface;
import org.example.briefi.interfaces.UserInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User implements AuthInterface, UserInterface {
    private int id;
    private String username;
    private String password;
    private Connection connection;

    public User() {
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> list() throws SQLException {
        List<User> users = new ArrayList<>();
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "SELECT * FROM users";
            PreparedStatement preparedStatement =
                    this.connection.prepareStatement(req);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));

                users.add(user);
            }

            preparedStatement.close();
            this.connection.close();
        }
        return users;
    }

    @Override
    public void register(User user) throws SQLException {
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(req);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString( 2, user.getPassword());

            int row = preparedStatement.executeUpdate();
            System.out.println(String.valueOf(row));

            preparedStatement.close();
            this.connection.close();
        }


    }

    @Override
    public boolean signIn(User user) throws SQLException {
        connection = IDBConfig.getConnection();
        int rows = 0;
        if (connection != null) {
            String req = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement prepareStatement =
                    this.connection.prepareStatement(req);
            prepareStatement.setString(1, user.getUsername());
            prepareStatement.setString(2, user.getPassword());

            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next()) {
                rows++;
            }
            prepareStatement.close();
            this.connection.close();
        }
        return rows > 0;
    }
}



