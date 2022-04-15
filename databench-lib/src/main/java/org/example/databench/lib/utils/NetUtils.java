package org.example.databench.lib.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {
    public static String getHostName() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
