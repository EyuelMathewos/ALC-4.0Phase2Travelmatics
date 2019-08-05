package com.psalms34.travelmatics;

import java.io.Serializable;

public class traveldeal implements Serializable {
    private String id;
    private String title;
    private String price;
    private String description;
    private String imageurl;
    public traveldeal(){}

    public traveldeal(String title, String price, String description, String imageurl) {
        this.setId(id);
        this.setTitle(title);
        this.setPrice(price);
        this.setDescription(description);
        this.setImageurl(imageurl);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
