package pjj.ibf2021project.server.models;

public class Tweet {
    private String text;
    private String username;
    private String rule_id;
    private String tag;
    
    public String getText() {
        return text;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getRule_id() {
        return rule_id;
    }
    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setText(String text) {
        this.text = text;
    }

    
}
