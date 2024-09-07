package org.example.briefi.interfaces;

import org.example.briefi.models.User;

import java.sql.SQLException;

public interface AuthInterface {
    void register(User user) throws SQLException;
    boolean signIn(User user) throws SQLException;
}
