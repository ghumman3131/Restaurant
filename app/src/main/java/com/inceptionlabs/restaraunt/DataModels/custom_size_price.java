package com.inceptionlabs.restaraunt.DataModels;



/**
 * Created by ghumman on 7/27/2016.
 */
public class custom_size_price {

    private String size , price , item_id , veg_non ,name   , quant;

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public void setVeg_non(String veg_non) {
        this.veg_non = veg_non;
    }

    public String getName() {
        return name;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getQuant() {
        return quant;
    }

    public String getVeg_non() {
        return veg_non;
    }

}

