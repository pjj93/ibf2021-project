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
    public static final String SQL_INSERT_USER_SUBSCRIPTION =
                            "insert into subscription (username, rule_id) values (?, ?)";
}
