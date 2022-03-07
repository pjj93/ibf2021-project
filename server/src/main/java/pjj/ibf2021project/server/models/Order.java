package pjj.ibf2021project.server.models;

public class Order {
    private String market;
    private String side;
    private Double price;
    private Double size;
    private String type;
    private Boolean reduceOnly;
    private Boolean ioc;
    private Boolean postOnly;
    private String clientId;
    
    public Order() {
        this.setReduceOnly(false);
        this.setIoc(false);
        this.setPostOnly(false);
        this.setClientId(null);
    }

    public String getMarket() {
        return market;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public Boolean getPostOnly() {
        return postOnly;
    }
    public void setPostOnly(Boolean postOnly) {
        this.postOnly = postOnly;
    }
    public Boolean getIoc() {
        return ioc;
    }
    public void setIoc(Boolean ioc) {
        this.ioc = ioc;
    }
    public Boolean getReduceOnly() {
        return reduceOnly;
    }
    public void setReduceOnly(Boolean reduceOnly) {
        this.reduceOnly = reduceOnly;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Double getSize() {
        return size;
    }
    public void setSize(Double size) {
        this.size = size;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getSide() {
        return side;
    }
    public void setSide(String side) {
        this.side = side;
    }
    public void setMarket(String market) {
        this.market = market;
    }

    @Override
    public String toString() {
        String str = "{\"market\": " + "\"" + this.getMarket() + "\"" + ", "
                    +"\"side\": " + "\"" + this.getSide() + "\"" + ", "
                    +"\"price\": " + this.getPrice() + ", "
                    +"\"size\": " + this.getSize() + ", "
                    +"\"type\": " + "\"" +  this.getType() + "\"" + ", "
                    +"\"reduceOnly\": " + this.getReduceOnly() + ", "
                    +"\"ioc\": " + this.getIoc() + ", "
                    +"\"postOnly\": " + this.getPostOnly() + ", "
                    +"\"clientId\": " + this.getClientId() + "}"
        ;
        return str;
    } 
}
