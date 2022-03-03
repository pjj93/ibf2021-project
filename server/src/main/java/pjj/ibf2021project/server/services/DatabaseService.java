package pjj.ibf2021project.server.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import pjj.ibf2021project.server.models.Subscription;
import pjj.ibf2021project.server.repositories.AppRepository;

@Service
public class DatabaseService {
    
    @Autowired
    private AppRepository appRepo;

    private Logger logger = Logger.getLogger(DatabaseService.class.getName());
    
    public boolean addNewUser(String username, String password) {
        try {
            return appRepo.insertUser(username, password);
        } catch (DuplicateKeyException e){
            logger.log(Level.INFO, "error - username is registered");
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        return appRepo.hasUser(username, password);
    }

    public JsonObject getUserSubscriptions(String username) {
        List<Subscription> subscriptions = appRepo.getUserSubscriptionDetails(username);

        JsonArrayBuilder jsonArrBuilder = Json.createArrayBuilder();
        
        for (Subscription subscription : subscriptions) {
            JsonObject jsonSubObj = Json.createObjectBuilder()
                                        .add("rule_id", subscription.getRule_id())
                                        .add("username", subscription.getUsername())
                                        .add("tag", subscription.getTag())
                                        .add("email_notification", subscription.hasEmail_notification())
                                        .add("auto_trade", subscription.hasAuto_trade())
                                        .add("description", subscription.getDescription())
                                        .build();
            jsonArrBuilder.add(jsonSubObj);
        }

        JsonObject jsonSubscriptions = Json.createObjectBuilder()
                                    .add("subscriptions", jsonArrBuilder)
                                    .build();

        return jsonSubscriptions;
    }
}
