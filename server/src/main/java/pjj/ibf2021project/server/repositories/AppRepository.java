package pjj.ibf2021project.server.repositories;

import static pjj.ibf2021project.server.repositories.SQLs.SQL_GET_USERS_BY_SUBSCRIPTION_RULE_ID;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_GET_USER_BY_USERNAME_AND_PASSWORD;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_GET_USER_FTX;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_GET_USER_SUBSCRIPTION_DETAILS;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_INSERT_FTX;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_INSERT_NEW_USER;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_INSERT_USER_SUBSCRIPTION;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_UPDATE_FTX;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_UPDATE_USER_SUBSCRIPTION_AUTOTRADE;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_UPDATE_USER_SUBSCRIPTION_EMAIL;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pjj.ibf2021project.server.models.Ftx;
import pjj.ibf2021project.server.models.Subscription;

@Repository
public class AppRepository {
    
    @Autowired
    private JdbcTemplate template;

    private Logger logger = Logger.getLogger(AppRepository.class.getName());

    public boolean insertUser(String username, String password) {
        return template.update(SQL_INSERT_NEW_USER, username, password) > 0;
    }

    public boolean insertSubscription(String username, String rule_id) {
        return template.update(SQL_INSERT_USER_SUBSCRIPTION, username, rule_id) > 0;
    }

    public boolean hasUser(String username, String password) {
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_USER_BY_USERNAME_AND_PASSWORD, username, password);
        if (rs.next())
            return true;
        return false;
    }

    public List<Subscription> getUserSubscriptionDetails(String username) {
        List<Subscription> subscriptions = new ArrayList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_USER_SUBSCRIPTION_DETAILS, username);

        while(rs.next()) {
            Subscription subscription = new Subscription();
            subscription.setUsername(rs.getString("username"));
            subscription.setRule_id(rs.getString("rule_id"));
            subscription.setTag(rs.getString("tag"));
            subscription.setDescription(rs.getString("description"));
            subscription.setEmail_notification(rs.getBoolean("email_notification"));
            subscription.setAuto_trade(rs.getBoolean("auto_trade"));

            subscriptions.add(subscription); // add into list
        }

        return subscriptions;
    }

    public List<String> getUsersSubscriptionByRuleId(String rule_id) {
        
        List<String> users = new ArrayList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_USERS_BY_SUBSCRIPTION_RULE_ID, rule_id);

        while(rs.next()) {
            users.add(rs.getString("username"));
        }

        return users;
    }

    public Ftx getFtx(String username) {
        Ftx ftx = new Ftx();

        final SqlRowSet rs = template.queryForRowSet(SQL_GET_USER_FTX, username);

        if(rs.next()) {
            ftx.setApi_key(rs.getString("api_key"));
            ftx.setApi_secret(rs.getString("api_secret"));
        } else {
            return null;
        }

        return ftx;
    }

    public int updateSubEmailNotification(String rule_id, String username, boolean email_notification) {

        return template.update(SQL_UPDATE_USER_SUBSCRIPTION_EMAIL, email_notification, username, rule_id);
        
    }

    public int updateSubAutoTrade(String rule_id, String username, boolean auto_trade) {

        return template.update(SQL_UPDATE_USER_SUBSCRIPTION_AUTOTRADE, auto_trade, username, rule_id);
        
    }

    public boolean insertFtx(String api_key, String api_secret, String username) {
        return template.update(SQL_INSERT_FTX, api_key, api_secret, username) > 0;
    }

    public int updateFtx(String api_key, String api_secret, String username) {

        return template.update(SQL_UPDATE_FTX, api_key, api_secret, username);
    }
}