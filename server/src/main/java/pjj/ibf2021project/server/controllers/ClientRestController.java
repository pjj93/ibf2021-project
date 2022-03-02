package pjj.ibf2021project.server.controllers;

import java.io.ByteArrayInputStream;
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
import pjj.ibf2021project.server.repositories.AppRepository;
import pjj.ibf2021project.server.services.DatabaseService;

@RestController
@CrossOrigin("*")
public class ClientRestController {
    
    @Autowired
    private AppRepository appRepo;

    @Autowired
    private DatabaseService databaseSvc;

    private Logger logger = Logger.getLogger(ClientRestController.class.getName());

    JsonObject response;
    
    @PostMapping(path="/signup", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody String json) {

        JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
        JsonObject jsonObj = reader.readObject();

        String username = jsonObj.getString("username");
        String password = jsonObj.getString("password");
        
        boolean isAdded = databaseSvc.addNewUser(username, password);

        if(isAdded) {
            response = Json.createObjectBuilder()
                        .add("status", "created")
                        .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
        } else {
            response = Json.createObjectBuilder()
                        .add("status", "error")
                        .add("message", "username is an existing user")
                        .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        }
    }
}
