package org.example.databench.common.utils;

import java.util.Objects;

public class JdbcConnectParam {

    private final String jdbcUrl;
    private String username = "";
    private String password = "";
    private JdbcType jdbcType;

    public JdbcConnectParam(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public JdbcConnectParam(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        jdbcType = JdbcType.parseFromUrl(jdbcUrl);
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JdbcConnectParam that = (JdbcConnectParam) o;
        return Objects.equals(jdbcUrl, that.jdbcUrl) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jdbcUrl, username);
    }
}
