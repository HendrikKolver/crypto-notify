package co.za.crypto.ping;

import co.za.crypto.ping.Services.SettingsEntity;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class NotificationHandler{
    private double previousBitcoinPrice = 0;
    private double previousLitecoinPrice = 0;
    private double previousEthereumPrice = 0;
    private final String API_TOKEN = "a18f7wk2pi86h69d6g38jxx7eiihxa";
    private final String USER_TOKEN = "ubg8dy2wmkmt2912s3ti3mgifs6xof";

    public void run() {
        try {
//            sendPost("Startup", new SimpleDateFormat().format(new Date()));

            while (true) {
//                System.out.println("Getting prices");
                String bitcoinResponse = sendGet(3);
                String litecoinResponse = sendGet(6);
                String ethereumResponse = sendGet(11);

                double bitcoinPrice = getPriceFromResponse(bitcoinResponse);
                double litecoinPrice = getPriceFromResponse(litecoinResponse);
                double ethereumPrice = getPriceFromResponse(ethereumResponse);
//
//                System.out.println("Previous price: "+previousBitcoinPrice
//                +" currentPrice: "+ bitcoinPrice
//                +" difference: "+ getDifference(previousBitcoinPrice, bitcoinPrice));

                SettingsEntity settings = ValueHolder.getInstance().getSettingsEntity();
                if (previousBitcoinPrice == 0) {
                    previousBitcoinPrice = bitcoinPrice;
                } else if (getDifference(previousBitcoinPrice, bitcoinPrice) > settings.getDifference() || getDifference(previousBitcoinPrice, bitcoinPrice) < -settings.getDifference()) {
                    sendPost("Bitcoin price change " + getDifference(previousBitcoinPrice, bitcoinPrice), "New Bitcoin Price: " + bitcoinPrice);
                    previousBitcoinPrice = bitcoinPrice;
                }

                if (previousLitecoinPrice == 0) {
                    previousLitecoinPrice = litecoinPrice;
                } else if (getDifference(previousLitecoinPrice, litecoinPrice) > settings.getDifference() || getDifference(previousLitecoinPrice, litecoinPrice) < -settings.getDifference()) {
                    sendPost("Litecoin price change " + getDifference(previousLitecoinPrice, litecoinPrice), "New Litecoin Price: " + litecoinPrice);
                    previousLitecoinPrice = litecoinPrice;
                }

                if (previousEthereumPrice == 0) {
                    previousEthereumPrice = ethereumPrice;
                } else if (getDifference(previousEthereumPrice, ethereumPrice) > settings.getDifference() || getDifference(previousEthereumPrice, ethereumPrice) < -settings.getDifference()) {
                    sendPost("Ethereum price change " + getDifference(previousEthereumPrice, ethereumPrice), "New Ethereum Price: " + ethereumPrice);
                    previousEthereumPrice = ethereumPrice;
                }
                Thread.sleep(settings.getRefreshTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getDifference(double previousPrice, double currentPrice) {
        return Math.round(((currentPrice - previousPrice) / previousPrice) * 100);
    }

    private double getPriceFromResponse(String priceResponse) {
        JSONObject response = new JSONObject(priceResponse);
        return response.getJSONObject("response").getJSONObject("entity").getDouble("last_price");
    }

    // HTTP GET request
    private String sendGet(int requestPair) throws Exception {

        String url = "https://www.ICE3X.com/api/v1/stats/marketdepthfull?pair_id=" + requestPair;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }

    // HTTP POST request
    private void sendPost(String title, String message) throws Exception {
        URL url = new URL("https://api.pushover.net/1/messages.json");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("token", API_TOKEN);
        params.put("user", USER_TOKEN);
        params.put("message", message);
        params.put("title", title);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    }
}
