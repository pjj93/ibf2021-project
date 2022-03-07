package pjj.ibf2021project.server.services;

import static pjj.ibf2021project.server.Constants.ENV_TWITTER_BEARER_TOKEN;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import pjj.ibf2021project.server.models.Tweet;
import pjj.ibf2021project.server.repositories.AppRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TwitterService {
    
    @Autowired
    private EmailService emailSvc;

    @Autowired
    private FtxService ftxSvc;

    @Autowired
    private AppRepository appRepo;

    private final WebClient webclient;

    private Logger logger = Logger.getLogger(TwitterService.class.getName());

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
                Tweet tweet = parsePayload(resp);
                try { 
                    emailSvc.sendEmail(tweet, appRepo.getUsersSubscriptionByRuleId(tweet.getRule_id())); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ftxSvc.placeOrderHttpClient();
                } catch (IOException e) {
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

    public Tweet parsePayload(String payload) {
        Tweet tweet = new Tweet();
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            final JsonReader reader = Json.createReader(is);
            final JsonObject body = reader.readObject();

            final JsonObject data = body.getJsonObject("data");
            final JsonObject user = body.getJsonObject("includes").getJsonArray("users").getJsonObject(0);
            final JsonObject matching_rule = body.getJsonArray("matching_rules").getJsonObject(0);

            tweet.setText(data.getString("text"));
            tweet.setUsername("@" + user.getString("username"));
            tweet.setRule_id(matching_rule.getString("id"));
            tweet.setTag(WordUtils.capitalize(matching_rule.getString("tag")));

            logger.log(Level.INFO, "text >>> " + tweet.getText());
            logger.log(Level.INFO, "username >>> " + tweet.getUsername());
            logger.log(Level.INFO, "rule_id >>> " + tweet.getRule_id());
            logger.log(Level.INFO, "tag >>> " + tweet.getTag());

            return tweet;
            
        } catch (Exception ex) { }

        return null;
    }
}