package org.example.briefi.interfaces;

import org.example.briefi.models.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductInterface {
        List<Product> list() throws SQLException;
        void create(Product product) throws SQLException;
        void update(Product product) throws SQLException;
        void delete(int id) throws SQLException;
}