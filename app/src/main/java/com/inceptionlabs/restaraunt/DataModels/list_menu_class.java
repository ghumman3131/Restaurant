package com.inceptionlabs.restaraunt.DataModels;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ghumman on 6/12/2016.
 */
public class list_menu_class implements Parcelable {

    public String name , item_id , veg_non , price , description , image , quant , custom_price_array ;

    public Boolean customize , availability ;

    public list_menu_class(){

    }

    public void setCustom_price_array(String custom_price_array) {
        this.custom_price_array = custom_price_array;
    }

    public String getCustom_price_array() {
        return custom_price_array;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Boolean getAvailability() {
        return availability;
    }


    public void setQuant(String quant) {
        this.quant = quant;
    }

    public String getQuant() {
        return quant;
    }

    public void setCustomize(Boolean customize) {
        this.customize = customize;
    }

    public Boolean getCustomize() {
        return customize;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getVeg_non() {
        return veg_non;
    }

    public void setVeg_non(String veg_non) {
        this.veg_non = veg_non;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }


    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_id() {
        return item_id;
    }

    protected list_menu_class(Parcel in) {

        this.name = in.readString();
        this.item_id = in.readString();
        this.veg_non = in.readString();
        this.price = in.readString();
    }

    public static final Creator<list_menu_class> CREATOR = new Creator<list_menu_class>() {
        @Override
        public list_menu_class createFromParcel(Parcel in) {
            return new list_menu_class(in);
        }

        @Override
        public list_menu_class[] newArray(int size) {
            return new list_menu_class[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}