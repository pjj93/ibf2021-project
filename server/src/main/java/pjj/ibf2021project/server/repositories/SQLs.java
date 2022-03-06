package pjj.ibf2021project.server.repositories;

public interface SQLs {
    public static final String SQL_INSERT_NEW_USER =
                                "insert into user (username, `password`) values (?, ?)";
    public static final String SQL_GET_USER_BY_USERNAME_AND_PASSWORD =
                                "select * from user where username= ? and `password` = ? ";
    public static final String SQL_GET_USER_SUBSCRIPTION_DETAILS =
                                "select "+
                                "subscription.rule_id as rule_id, subscription.username as username, "+
                                "twitter.tag as `tag`, twitter.`description` as `description`, "+
                                "subscription.email_notification as email_notification, subscription.auto_trade as auto_trade "+
                                "from subscription join twitter "+
                                "on subscription.rule_id = twitter.rule_id "+
                                "where subscription.username = ?";
    public static final String SQL_GET_USER_FTX =
                                "select api_key, api_secret "+
                                "from ftx "+
                                "where username = ?";
    public static final String SQL_INSERT_USER_SUBSCRIPTION =
                                "insert into subscription (username, rule_id) values (?, ?)";
    public static final String SQL_UPDATE_USER_SUBSCRIPTION_EMAIL = 
                                "update subscription "+
                                "set email_notification = ? "+
                                "where username = ? and rule_id = ?";
    public static final String SQL_UPDATE_USER_SUBSCRIPTION_AUTOTRADE = 
                                "update subscription "+
                                "set auto_trade = ? "+
                                "where username = ? and rule_id = ?";
    public static final String SQL_INSERT_FTX =
                                "insert into ftx (api_key, api_secret, username) values (?, ?, ?)";
    public static final String SQL_UPDATE_FTX =
                                "update ftx "+
                                "set api_key = ?, api_secret = ? "+
                                "where username = ?";
    public static final String SQL_GET_USERS_BY_SUBSCRIPTION_RULE_ID = 
                                "select username from subscription where rule_id = ?";
}