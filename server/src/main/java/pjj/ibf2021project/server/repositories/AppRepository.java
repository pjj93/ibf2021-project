package pjj.ibf2021project.server.repositories;

import static pjj.ibf2021project.server.repositories.SQLs.SQL_GET_USER_BY_USERNAME_AND_PASSWORD;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_GET_USER_SUBSCRIPTION_DETAILS;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_INSERT_NEW_USER;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pjj.ibf2021project.server.models.Subscription;

@Repository
public class AppRepository {
    
    @Autowired
    private JdbcTemplate template;

    private Logger logger = Logger.getLogger(AppRepository.class.getName());

    public boolean insertUser(String username, String password) {
        return template.update(SQL_INSERT_NEW_USER, username, password) > 0;
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
            subscription.setEmail_notification(rs.getString("email_notification"));
            subscription.setAuto_trade(rs.getString("auto_trade"));
            
            logger.log(Level.INFO, "number of subscriptions >>> " + subscription.getDescription());

            subscriptions.add(subscription); // add into list
        }
        
        logger.log(Level.INFO, "number of subscriptions >>> " + subscriptions.size());

        return subscriptions;
    }
    // public boolean hasUser(String username) {
	// 	final SqlRowSet rs = template.queryForRowSet(
	// 			SQL_GET_USER_BY_USERNAME, username);
	// 	if (rs.next())
	// 		return rs.getInt("user_count") > 0;
	// 	return false;
	// }
}
