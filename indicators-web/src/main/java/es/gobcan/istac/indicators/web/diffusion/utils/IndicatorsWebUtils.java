package es.gobcan.istac.indicators.web.diffusion.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.util.UriComponentsBuilder;

public class IndicatorsWebUtils {

    // TODO hacer petición REST desde página?
    public static String getJson(UriComponentsBuilder uriComponentsBuilder, String urlPath) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        InputStream response = null;
        if (conn.getResponseCode() == 200) {
            response = conn.getInputStream();
        } else if (conn.getResponseCode() == 404) {
            response = conn.getErrorStream();
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(response, "UTF-8"));
        StringBuffer json = new StringBuffer();
        String output = null;
        while ((output = br.readLine()) != null) {
            json.append(output);
        }
        conn.disconnect();
        return json.toString();
    }
}
