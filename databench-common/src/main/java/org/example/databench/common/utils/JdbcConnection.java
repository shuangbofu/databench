package org.example.databench.common.utils;

import org.example.databench.common.domain.query.result.SqlResult;
import org.example.databench.common.utils.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class);
    private final Connection connection;

    public JdbcConnection(JdbcConnectParam param) throws SQLException, ClassNotFoundException {
        Class.forName(param.getJdbcType().getClassName());
        java.util.Properties info = new java.util.Properties();
        info.put("user", param.getUsername());
        info.put("password", param.getPassword());
        info.put("autoReconnect", "true");
        connection = DriverManager.getConnection(param.getJdbcUrl(), info);
    }

    public SqlResult executeQuery(String sql) throws Exception {
        return execute(() -> JdbcUtils.executeQuery(connection, sql));
    }

    public Integer executeUpdate(String sql) throws Exception {
        return execute(() -> JdbcUtils.executeUpdate(connection, sql));
    }

    private <T> T execute(Supplier<T> supplier) throws Exception {
        if (isActive()) {
            return supplier.get();
        }
        return null;
    }

    private boolean isActive() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException("Connection is not active", e);
        }
    }

    public boolean isValid(int timeout) throws SQLException {
        return connection != null && connection.isValid(timeout);
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.warn("Connection close error", e);
        }
    }
}
