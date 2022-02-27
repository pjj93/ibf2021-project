package pjj.ibf2021project.server.services;

import static pjj.ibf2021project.server.Constants.ENV_TWITTER_BEARER_TOKEN;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import pjj.ibf2021project.server.ServerApplication;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TwitterService {
    
    @Autowired
    private EmailService emailSvc;

    private final WebClient webclient;

    private Logger logger = Logger.getLogger(ServerApplication.class.getName());

    public TwitterService(WebClient.Builder webClientBuilder) {
        this.webclient = webClientBuilder.baseUrl("https://api.twitter.com/2").build();
    }

    public void getUser(String username) {

        ResponseEntity<String> response = webclient.get()
            .uri("/users/by/username/{username}", username)
            .header("Authorization", "Bearer " + ENV_TWITTER_BEARER_TOKEN)
            .retrieve()
            .toEntity(String.class)
            .block();

        int httpstatus = response.getStatusCodeValue();
        String respBody = response.getBody();
        // HttpHeaders respHeaders = response.getHeaders();
        
        logger.log(Level.INFO, "status code >>> " + httpstatus);
        logger.log(Level.INFO, "response >>> " + respBody);
        // logger.log(Level.INFO, "headers >>> " + respHeaders);
    }

    public void getStreamRules() {
        
        ResponseEntity<String> response = webclient.get()
            .uri("/tweets/search/stream/rules")
            .header("Authorization", "Bearer " + ENV_TWITTER_BEARER_TOKEN)
            .retrieve()
            .toEntity(String.class)
            .block();
        
        int httpstatus = response.getStatusCodeValue();
        String respBody = response.getBody();
        
        logger.log(Level.INFO, "status code >>> " + httpstatus);
        logger.log(Level.INFO, "response >>> " + respBody);
    }

    public void addStreamRule(String value, String username, String tag) {

    }

    public void getStream() {

        logger.log(Level.INFO, "STARTING STREAM ...");   

        Flux<String> response = webclient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/tweets/search/stream")
                .queryParam("tweet.fields", "attachments,created_at")
                .queryParam("expansions", "author_id")
                .build())
            .header("Authorization", "Bearer " + ENV_TWITTER_BEARER_TOKEN)
            .retrieve()
            .bodyToFlux(String.class);

        response.subscribe(resp -> {
            if(resp.contains("matching_rules")) {
                logger.log(Level.INFO, resp);
                try { 
                    emailSvc.sendEmail(resp); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addRule(String value, String from, String description) {
        
        value = "posttry";
        from = "testuser69420";
        description = "post from server";

        JsonObject addJsonObj = Json.createObjectBuilder()
                                    .add("value", "%s -is:retweet -is:reply from:%s".formatted(value, from))
                                    .add("tag", description)
                                    .build();

        JsonArray addJsonArr = Json.createArrayBuilder()
                                    .add(addJsonObj)
                                    .build();

        JsonObject jsonBody = Json.createObjectBuilder()
                                    .add("add", addJsonArr)
                                    .build();

        Mono<ResponseEntity<String>> response = webclient.post()
            .uri("/tweets/search/stream/rules")
            .header("Authorization", "Bearer " + ENV_TWITTER_BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(jsonBody.toString()))
            .retrieve()
            .toEntity(String.class);
        
        Disposable resp = 
                response.subscribe(i -> {
                    int httpstatus = i.getStatusCodeValue();
                    String respBody = i.getBody();
                    logger.log(Level.INFO, "status code >>> " + httpstatus);
                    logger.log(Level.INFO, "response >>> " + respBody);
                });
        
        //// Synchronous call
        /*  ResponseEntity<String> response = webclient.post()
                .uri("/tweets/search/stream/rules")
                .header("Authorization", "Bearer " + ENV_TWITTER_BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonBody.toString()))
                .retrieve()
                .toEntity(String.class)
                .block(); */
    }
}