package org.example.databench.executor.script.executor;

/**
 * Secure auth config
 */
public class SecureAuth {
    private String privateKeyPath;
    private String host;
    private int port;
    private String username;

    public SecureAuth() {

    }

    public SecureAuth(String privateKeyPath, String host, int port, String username) {
        this.privateKeyPath = privateKeyPath;
        this.host = host;
        this.port = port;
        this.username = username;
    }

    public SecureAuth(String privateKeyPath, String host, String username) {
        this.privateKeyPath = privateKeyPath;
        this.host = host;
        this.username = username;
        port = 22;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port == 0 ? 22 : port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLabel() {
        return username + "@" + host + ":" + port;
    }
}
