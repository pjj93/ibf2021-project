package pjj.ibf2021project.server.services;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import pjj.ibf2021project.server.models.Order;
import reactor.core.publisher.Mono;

@Service
public class FtxService {
    private Logger logger = Logger.getLogger(FtxService.class.getName());
    
    private static final String FTX_KEY = System.getenv("FTX_KEY");
    private static final String FTX_SECRET = System.getenv("FTX_SECRET");
    private static final String FTX_SUBACCOUNT = "Bot";
    private final WebClient webclient;

    public FtxService (WebClient.Builder webClientBuilder) {
        this.webclient = 
                    webClientBuilder.baseUrl("https://ftx.com")
                                .defaultHeaders(httpHeaders -> {
                                    httpHeaders.set("FTX-KEY", FTX_KEY);
                                    httpHeaders.set("FTX-SUBACCOUNT", FTX_SUBACCOUNT);
                                    httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                                })
                                .build();
    }

    public void getBalance() {
        String timestamp = Long.toString(System.currentTimeMillis());
        String signature_payload = timestamp + "GET" + "/api/wallet/balances";

        Mono<ResponseEntity<String>> response = webclient.get()
            .uri("/api/wallet/balances")
            .header("FTX-TS", timestamp)
            .header("FTX-SIGN", calcHmacSha256(signature_payload))
            .retrieve()
            .toEntity(String.class);

        response.subscribe(i -> {
            int httpstatus = i.getStatusCodeValue();
            String respBody = i.getBody();
            logger.log(Level.INFO, "status code >>> " + httpstatus);
            logger.log(Level.INFO, "response >>> " + respBody);
        });
    }

    public String calcHmacSha256(String message) {

        byte[] hmacSha256 = null;
        try {
          Mac mac = Mac.getInstance("HmacSHA256");
          SecretKeySpec secretKeySpec = new SecretKeySpec(FTX_SECRET.getBytes(), "HmacSHA256");
          mac.init(secretKeySpec);
          hmacSha256 = mac.doFinal(message.getBytes());
        } catch (Exception e) {
          throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        
        return Hex.encodeHexString(hmacSha256);
      }

    public void NewOrder() {
        JsonObject payload = Json.createObjectBuilder()
                                .add("market", "FTM/USD")
                                .add("side", "buy")
                                .add("price", 1.0)
                                .add("type", "limit")
                                .add("size", 3.0)
                                // .add("reduceOnly", false)
                                // .add("ioc", false)
                                // .add("postOnly", false)
                                // .add("clientId", JsonValue.NULL)
                                .build();
        
        JSONObject payloadalt = new JSONObject();
        payloadalt.put("market", "FTM-PERP");
        payloadalt.put("side", "buy");
        payloadalt.put("price", 1.0);
        payloadalt.put("type", "limit");
        payloadalt.put("size", 3.0);
        payloadalt.put("reduceOnly", false);
        payloadalt.put("ioc", false);
        payloadalt.put("postOnly", false);
        payloadalt.put("clientId", JSONObject.NULL);
        
        Order order = new Order();

        order.setMarket("FTM/USD");
        order.setSide("buy");
        order.setPrice(1.0);
        order.setType("limit");
        order.setSize(3.0);

        logger.log(Level.INFO, "from POJO toString() >>> " + order.toString());
        logger.log(Level.INFO, "from org.json >>> " + payloadalt.toString());

        // logger.log(Level.INFO, payload.toString());

        String timestamp = Long.toString(System.currentTimeMillis());
        String signature_payload = timestamp + "POST" + "/api/orders" + payload.toString();

        logger.log(Level.INFO, "signature payload >>> " + signature_payload);
        // logger.log(Level.INFO, "signature >>> " + calcHmacSha256(signature_payload));

        try {
            Mono<ResponseEntity<String>> response = webclient.post()
            .uri("/api/orders")
            .header("FTX-TS", timestamp)
            .header("FTX-SIGN", calcHmacSha256(signature_payload))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .retrieve()
            .toEntity(String.class);

            response.subscribe(i -> {
                int httpstatus = i.getStatusCodeValue();
                String respBody = i.getBody();
                logger.log(Level.INFO, "status code >>> " + httpstatus);
                logger.log(Level.INFO, "response >>> " + respBody);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void placeOrderHttpClient() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://ftx.com/api/orders");

        JsonObject payload = Json.createObjectBuilder()
                                .add("market", "FTM/USD")
                                .add("side", "buy")
                                .add("price", 1.0)
                                .add("type", "limit")
                                .add("size", 3.0)
                                // .add("reduceOnly", false)
                                // .add("ioc", false)
                                // .add("postOnly", false)
                                // .add("clientId", JsonValue.NULL)
                                .build();

        String timestamp = Long.toString(System.currentTimeMillis());
        String signature_payload = timestamp + "POST" + "/api/orders" + payload.toString();

        StringEntity entity = new StringEntity(payload.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("FTX-KEY", FTX_KEY);
        httpPost.setHeader("FTX-TS", timestamp);
        httpPost.setHeader("FTX-SIGN", calcHmacSha256(signature_payload));
        httpPost.setHeader("FTX-SUBACCOUNT", FTX_SUBACCOUNT);
   
        CloseableHttpResponse response = client.execute(httpPost);
        
        logger.log(Level.INFO, "httpclient status>>> " + response.getStatusLine());
        logger.log(Level.INFO, "httpclient entity>>> " + EntityUtils.toString(response.getEntity()));
      
        if(response.getStatusLine().toString().contains("200 OK")) {
            logger.log(Level.INFO, "send email notification");
        }

        client.close();
    }
    
}