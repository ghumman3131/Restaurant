package com.inceptionlabs.restaraunt.DataModels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ghumman on 11/12/2016.
 */

public class rest_main implements Parcelable {

    String rest_name, rest_image, rest_id, rest_type , rest_lati , rest_longi , rest_status,rest_rating , location;
    Boolean selcted;

    public rest_main(Parcel in) {
        rest_name = in.readString();

        rest_id = in.readString();
        rest_image = in.readString();


    }

    public rest_main(){

    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setRest_lati(String rest_lati) {
        this.rest_lati = rest_lati;
    }

    public void setRest_longi(String rest_longi) {
        this.rest_longi = rest_longi;
    }

    public void setRest_rating(String rest_rating) {
        this.rest_rating = rest_rating;
    }

    public void setRest_status(String rest_status) {
        this.rest_status = rest_status;
    }

    public void setRest_type(String rest_type) {
        this.rest_type = rest_type;
    }

    public String getRest_lati() {
        return rest_lati;
    }

    public String getRest_longi() {
        return rest_longi;
    }

    public String getRest_rating() {
        return rest_rating;
    }

    public String getRest_status() {
        return rest_status;
    }

    public String getRest_type() {
        return rest_type;
    }

    public String getRest_id() {
        return rest_id;
    }

    public String getRest_image() {
        return rest_image;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public void setRest_image(String rest_image) {
        this.rest_image = rest_image;
    }

    public void setSelcted(Boolean selcted) {
        this.selcted = selcted;
    }

    public Boolean getSelcted() {
        return selcted;
    }

    public static final Creator<rest_main> CREATOR = new Creator<rest_main>() {
        @Override
        public rest_main createFromParcel(Parcel in) {
            return new rest_main(in);
        }

        @Override
        public rest_main[] newArray(int size) {
            return new rest_main[size];
        }
    };

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rest_name);
        dest.writeString(rest_id);
        dest.writeString(rest_image);
    }
}

