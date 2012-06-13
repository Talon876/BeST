package org.nolat.best.tasks.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class IP {

    private static final Logger log = Logger.getLogger(IP.class);
    private static String ip = "";

    public static String getIp() {
        if (ip == null || ip.isEmpty()) {
            try {
                URL url = new URL("http://icanhazip.com/");
                URLConnection conn = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                ip = in.readLine();
                return ip;

            } catch (Exception ex) {
                log.error(ex);
            }
            return "127.0.0.1";
        } else {
            return ip;
        }
    }
}
