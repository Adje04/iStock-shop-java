package org.example.briefi.interfaces;

import org.example.briefi.models.User;

import java.sql.SQLException;
import java.util.List;

public interface UserInterface {
    List<User> list() throws SQLException;
}