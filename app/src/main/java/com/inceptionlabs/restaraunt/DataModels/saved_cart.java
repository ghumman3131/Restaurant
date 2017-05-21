package com.inceptionlabs.restaraunt.DataModels;

public class saved_cart {

    String item_id , itemname , veg_non , description , qty , cost ;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuant(String quant) {
        this.qty = quant;
    }

    public void setPrice(String price) {
        this.cost = price;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setName(String name) {
        this.itemname = name;
    }

    public void setVeg_non(String veg_non) {
        this.veg_non = veg_non;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return itemname;
    }

    public String getPrice() {
        return cost;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getQuant() {
        return qty;
    }

    public String getVeg_non() {
        return veg_non;
    }
}
