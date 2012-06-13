package org.nolat.best.tracking;

import java.security.MessageDigest;

import mjson.Json;

import org.apache.log4j.Logger;
import org.nolat.best.preferences.Prefs;
import org.nolat.best.tasks.net.IP;

public class Track {

    private static final Logger log = Logger.getLogger(Track.class);

    public static void track(Json trackedInfo) {
        if (Prefs.prefs.at("app.collectdata").asBoolean()) {
            Json message = getHeaders().set("info", trackedInfo);
            log.info("Sending tracking data: " + message.toString());
        } else {
            log.warn("Tracking is disabled. The following message was NOT sent: " + trackedInfo.toString());
        }
    }

    private static Json getHeaders() {
        Json j = Json.object().set("id", getUniqueId());
        return j;
    }

    public static String getUniqueId() {
        StringBuffer uid = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageBytes = (System.getProperty("user.name") + IP.getIp()).getBytes("UTF-8");
            byte[] digestedMessage = md.digest(messageBytes);
            for (int i = 0; i < digestedMessage.length; i++) {
                uid.append(Integer.toHexString(0xFF & digestedMessage[i]));
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("Generated uid: " + uid.toString());
        return uid.toString();
    }
}
