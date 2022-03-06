package pjj.ibf2021project.server.controllers;

import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import pjj.ibf2021project.server.models.Ftx;
import pjj.ibf2021project.server.repositories.AppRepository;
import pjj.ibf2021project.server.services.DatabaseService;

@RestController
public class ClientRestController {
    
    @Autowired
    private AppRepository appRepo;

    @Autowired
    private DatabaseService databaseSvc;

    private Logger logger = Logger.getLogger(ClientRestController.class.getName());

    JsonObject response;
    
    @PostMapping(path="/api/client/signup", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody String json) {

        JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
        JsonObject jsonObj = reader.readObject();

        String username = jsonObj.getString("username");
        String password = jsonObj.getString("password");
        
        boolean isAdded = databaseSvc.addNewUser(username, password);

        if(isAdded) {
            boolean addedNewSubscriptions = databaseSvc.addNewUserSubscriptions(username);
            if(addedNewSubscriptions) {
                response = Json.createObjectBuilder()
                                .add("status", "created")
                                .add("message", "created user with subscriptions")
                                .build();
            } else {
                response = Json.createObjectBuilder()
                                .add("status", "created")
                                .add("message", "created user without subscriptions")
                                .build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
        } else {
            response = Json.createObjectBuilder()
                        .add("status", "error")
                        .add("message", "username is an existing user")
                        .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        }
    }

    @PostMapping(path="/api/client/login", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody String json) {

        JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
        JsonObject jsonObj = reader.readObject();

        String username = jsonObj.getString("username");
        String password = jsonObj.getString("password");
        
        boolean isLogin = databaseSvc.loginUser(username, password);

        if(isLogin) {
            response = Json.createObjectBuilder()
                        .add("status", "success")
                        .build();
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        } else {
            response = Json.createObjectBuilder()
                        .add("status", "error")
                        .add("message", "unable to login")
                        .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        }
    }

    @GetMapping(path="/api/client/dashboard", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> dashboard(@RequestHeader("username") String username) {

        // appRepo.getUserSubscriptionDetails("jian_jun3@hotmail.com");
        JsonObject body = databaseSvc.getUserSubscriptions(username);
        
        return ResponseEntity.status(HttpStatus.OK).body(body.toString());
    }

    @GetMapping(path="/api/client/ftx", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFtx(@RequestHeader("username") String username) {

        JsonObject body = databaseSvc.getFtx(username);
        
        if (body != null) {
            return ResponseEntity.status(HttpStatus.OK).body(body.toString());
        } else {
            response = Json.createObjectBuilder()
                .add("status", "error")
                .add("message", "no ftx key found for user")
                .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        }

    }

    @PostMapping(path="/api/client/ftx", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateFtx(@RequestHeader("username") String username, @RequestBody Ftx ftx) {

        String api_key = ftx.getApi_key();
        String api_secret = ftx.getApi_secret();
        boolean isUpdated = false;

        JsonObject ftxObj = databaseSvc.getFtx(username);
        if (ftxObj != null) {
            isUpdated = databaseSvc.updateFtx(api_key, api_secret, username);
        } else {
            isUpdated = databaseSvc.insertFtx(api_key, api_secret, username);
        }
        
        if(isUpdated) {
            response = Json.createObjectBuilder()
                        .add("status", "updated")
                        .build();
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        } else {
            response = Json.createObjectBuilder()
                        .add("status", "error")
                        .add("message", "unable to update")
                        .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        }
    }

    @PostMapping(path="/api/client/subscription/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEmailNotification(@RequestBody String json) {

        JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
        JsonObject jsonObj = reader.readObject();

        boolean email_notification = jsonObj.getBoolean("email_notification");
        String username = jsonObj.getString("username");
        String rule_id = jsonObj.getString("rule_id");

        boolean isUpdated = databaseSvc.updateSubEmailNotification(rule_id, username, email_notification);
        
        if(isUpdated) {
            response = Json.createObjectBuilder()
                        .add("status", "updated")
                        .build();
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        } else {
            response = Json.createObjectBuilder()
                        .add("status", "error")
                        .add("message", "unable to update")
                        .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        }
    }

    @PostMapping(path="/api/client/subscription/trade", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAutoTrade(@RequestBody String json) {

        JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
        JsonObject jsonObj = reader.readObject();

        boolean auto_trade = jsonObj.getBoolean("auto_trade");
        String username = jsonObj.getString("username");
        String rule_id = jsonObj.getString("rule_id");

        boolean isUpdated = databaseSvc.updateSubAutoTrade(rule_id, username, auto_trade);
        
        if(isUpdated) {
            response = Json.createObjectBuilder()
                        .add("status", "updated")
                        .build();
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        } else {
            response = Json.createObjectBuilder()
                        .add("status", "error")
                        .add("message", "unable to update")
                        .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        }
    }
}
