package pjj.ibf2021project.server.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import pjj.ibf2021project.server.ServerApplication;

@Service
public class FtxService {
    private Logger logger = Logger.getLogger(ServerApplication.class.getName());
    
    private static final String FTX_KEY = System.getenv("FTX_KEY");
    private static final String FTX_SECRET = System.getenv("FTX_SECRET");
    // private static final String FTX_SUBACCOUNT = "Bot";
    private final WebClient webclient;

    public FtxService (WebClient.Builder webClientBuilder) {
        this.webclient = 
                    webClientBuilder.baseUrl("https://ftx.com")
                                .defaultHeaders(httpHeaders -> {
                                    httpHeaders.set("FTX-KEY", FTX_KEY);
                                    // httpHeaders.set("FTX-SUBACCOUNT", FTX_SUBACCOUNT);
                                    httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                                })
                                .build();
    }

    public void getBalance() {
        String timestamp = Long.toString(System.currentTimeMillis());
        String signature_payload = timestamp + "GET" + "/api/wallet/balances";

        ResponseEntity<String> response = webclient.get()
            .uri("/api/wallet/balances")
            .header("FTX-TS", timestamp)
            .header("FTX-SIGN", calcHmacSha256(signature_payload))
            .retrieve()
            .toEntity(String.class)
            .block();

        int httpstatus = response.getStatusCodeValue();
        String respBody = response.getBody();
        
        logger.log(Level.INFO, "status code >>> " + httpstatus);
        logger.log(Level.INFO, "response >>> " + respBody);
    }

    public String calcHmacSha256(String message) {

        logger.log(Level.INFO, "signature payload >>> " + message);

        byte[] hmacSha256 = null;
        try {
          Mac mac = Mac.getInstance("HmacSHA256");
          SecretKeySpec secretKeySpec = new SecretKeySpec(FTX_SECRET.getBytes(), "HmacSHA256");
          mac.init(secretKeySpec);
          hmacSha256 = mac.doFinal(message.getBytes());
        } catch (Exception e) {
          throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }

        logger.log(Level.INFO, "hashed signature >>> " + Hex.encodeHexString(hmacSha256));
        
        return Hex.encodeHexString(hmacSha256);
      }
    
}
