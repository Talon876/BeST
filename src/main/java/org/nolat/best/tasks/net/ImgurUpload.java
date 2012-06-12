package org.nolat.best.tasks.net;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import mjson.Json;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.nolat.best.Tray;

public class ImgurUpload implements Runnable {

    private static final Logger log = Logger.getLogger(ImgurUpload.class);

    private static final String IMGUR_ENDPOINT = "http://api.imgur.com/2/upload.json";
    private static final String IMGUR_API_KEY = "901c1a267cb6c289b6b4638b901ca620";
    private final BufferedImage image;

    public ImgurUpload(BufferedImage image) {
        this.image = image;
        new Thread(this).start();
    }

    @Override
    public void run() {
        if (image != null) {
            String url = upload();
            if (url != null && !url.isEmpty()) {
                StringSelection ss = new StringSelection(url);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
                Tray.publishMessage("The link is in the system clipboard.", "Success!");
            } else {
                Tray.publishMessage(
                        "Imgur may be down, or you may have exceeded the upload limit... or aliens stole your screenshot.",
                        "Fail!");
            }
        } else {
            log.info("Image was null");
        }
    }

    private String upload() {
        String url = null;
        try {
            ByteArrayOutputStream temp = new ByteArrayOutputStream();

            File screenshot = new File(System.getProperty("user.home") + "/.BeST/screenshots/"
                    + (System.currentTimeMillis() / 1000L) + ".png");
            ImageIO.write(image, "png", temp);
            ImageIO.write(image, "png", screenshot);

            URL imgurEndpoint = new URL(IMGUR_ENDPOINT);
            String imageb64 = Base64.encodeBase64String(temp.toByteArray());
            temp.close();

            URLConnection connection = imgurEndpoint.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
            String data = "image=" + URLEncoder.encode(imageb64, "UTF-8") + "&key="
                    + URLEncoder.encode(IMGUR_API_KEY, "UTF-8");
            output.write(data);
            output.flush();

            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line = "";
            while ((line = input.readLine()) != null) {
                result += line;
            }
            url = parseJson(result);

        } catch (IOException e) {
            log.error(e);
        }
        return url;
    }

    private String parseJson(String jsonAsString) {
        String url = null;
        Json json = Json.read(jsonAsString);
        url = json.asJsonMap().get("upload").asJsonMap().get("links").asJsonMap().get("original").asString();

        log.info("URL: " + url);
        return url;
    }
}
