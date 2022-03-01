package pjj.ibf2021project.server.controllers;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import pjj.ibf2021project.server.ServerApplication;
import pjj.ibf2021project.server.repositories.AppRepository;

@RestController
@CrossOrigin("*")
public class ClientRestController {
    
    @Autowired
    private AppRepository appRepo;

    private Logger logger = Logger.getLogger(ServerApplication.class.getName());
    
    @PostMapping(path="/signup", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody String json) {

        JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
        JsonObject jsonObj = reader.readObject();

        String username = jsonObj.getString("username");
        String password = jsonObj.getString("password");
    
        logger.log(Level.INFO, "requestbody >>> " + json);
        logger.log(Level.INFO, "username >>> " + username);
        logger.log(Level.INFO, "password >>> " + password);

        int rowsaffected = appRepo.addNewUser(username, password);
        logger.log(Level.INFO, "rows affected >>> " + rowsaffected);

        String defaultusername = "default@gmail.com";
        logger.log(Level.INFO, "default username >>> " + defaultusername);

        JsonObject created = Json.createObjectBuilder()
                        .add("status", "created")
                        .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(created.toString());
    }
}
