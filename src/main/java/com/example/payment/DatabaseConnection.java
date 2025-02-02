package com.example.payment;

import java.sql.SQLException;

public interface DatabaseConnection {
    void executeUpdate(String sql) throws SQLException;

}
