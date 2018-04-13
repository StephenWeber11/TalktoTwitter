package com.example.akhilajana.talktotwitter;

import java.io.Serializable;

/**
 * Created by Stephen on 4/5/2018.
 */

public class User implements Serializable {
    String location, name, imageUrl;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
