package org.nolat.best.tasks;

import mjson.Json;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.nolat.best.preferences.Prefs;
import org.nolat.best.tracking.Track;

//A class made just for debugging stuff. It is ran when the Debug menu item is selected
public class DebugTask {

    public DebugTask() {
        //postJson();
        Json track = Json.object().set("debugClicked", System.currentTimeMillis() / 1000L);
        Track.track(track);
        System.out.println(Track.getUniqueId());
    }

    private void postJson() {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:7000");
            StringEntity params = new StringEntity(Prefs.prefs.toString(), "UTF-8");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            httpClient.execute(request);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
