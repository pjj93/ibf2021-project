package pjj.ibf2021project.server.models;

public class Subscription {
    private String rule_id;
    private String username;
    private String email_notification;
    private String auto_trade;
    private String tag;
    private String description;

    public String getRule_id() {
        return rule_id;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAuto_trade() {
        return auto_trade;
    }
    public void setAuto_trade(String auto_trade) {
        this.auto_trade = auto_trade;
    }
    public String getEmail_notification() {
        return email_notification;
    }
    public void setEmail_notification(String email_notification) {
        this.email_notification = email_notification;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    
}
