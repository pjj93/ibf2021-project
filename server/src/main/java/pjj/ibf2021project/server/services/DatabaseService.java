package pjj.ibf2021project.server.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import pjj.ibf2021project.server.models.Ftx;
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

    public boolean addNewUserSubscriptions(String username) {
        try {
            appRepo.insertSubscription(username, "1500344672710840323"); // test tweet
            appRepo.insertSubscription(username, "1500360168625692676"); // test coinbase listing
            appRepo.insertSubscription(username, "1500351541449953280"); // real coinbase listing
            appRepo.insertSubscription(username, "1500352439492300804"); // real elon tweets doge
            return true;
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

    public JsonObject getFtx(String username) {

        try {
            Ftx ftx = appRepo.getFtx(username);
            JsonObject jsonObj = Json.createObjectBuilder()
                                    .add("api_key", ftx.getApi_key())
                                    .add("api_secret", ftx.getApi_secret())
                                    .build();
            return jsonObj;
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "error - no ftx key found for user");
            return null;
        }
        
    }

    public boolean updateSubEmailNotification(String rule_id, String username, boolean email_notification) {

        try {
            return appRepo.updateSubEmailNotification(rule_id, username, email_notification) > 0;
        } catch (DataAccessException e) {
            logger.log(Level.INFO, "error - unable to update email_notification in subscription table");
            return false;
        }
    }

    public boolean updateSubAutoTrade(String rule_id, String username, boolean auto_trade) {

        try {
            return appRepo.updateSubAutoTrade(rule_id, username, auto_trade) > 0;
        } catch (DataAccessException e) {
            logger.log(Level.INFO, "error - unable to update auto_trade in subscription table");
            return false;
        }
    }

    public boolean updateFtx(String api_key, String api_secret, String username) {
        logger.log(Level.INFO, "update existing ftx");
        try {
            return appRepo.updateFtx(api_key, api_secret, username) > 0;
        } catch (DataAccessException e) {
            logger.log(Level.INFO, "error - unable to update auto_trade in subscription table");
            return false;
        }
    }

    public boolean insertFtx(String api_key, String api_secret, String username) {
        logger.log(Level.INFO, "inserting new ftx");
        try {
            return appRepo.insertFtx(api_key, api_secret, username);
        } catch (DataAccessException e) {
            logger.log(Level.INFO, "error - unable to add new ftx key");
            return false;
        }
    }
}
