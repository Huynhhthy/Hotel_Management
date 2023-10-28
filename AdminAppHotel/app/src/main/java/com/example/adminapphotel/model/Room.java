package com.example.adminapphotel.model;

import java.io.Serializable;

public class Room implements Serializable{
    private String roomID;
    private String nameroom;
    private double priceroom;
    private String typeroom;
    private String statusroom;
    private String descriptionroom;
    private String image1;
    private String image2;
    private String image3;

    public Room() {

    }

    public Room(String roomID, String nameroom, double priceroom, String typeroom, String statusroom, String descriptionroom, String image1, String image2, String image3) {
        this.roomID = roomID;
        this.nameroom = nameroom;
        this.priceroom = priceroom;
        this.typeroom = typeroom;
        this.statusroom = statusroom;
        this.descriptionroom = descriptionroom;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getNameroom() {
        return nameroom;
    }

    public void setNameroom(String nameroom) {
        this.nameroom = nameroom;
    }

    public double getPriceroom() {
        return priceroom;
    }

    public void setPriceroom(double priceroom) {
        this.priceroom = priceroom;
    }

    public String getTyperoom() {
        return typeroom;
    }

    public void setTyperoom(String typeroom) {
        this.typeroom = typeroom;
    }

    public String getStatusroom() {
        return statusroom;
    }

    public void setStatusroom(String statusroom) {
        this.statusroom = statusroom;
    }

    public String getDescriptionroom() {
        return descriptionroom;
    }

    public void setDescriptionroom(String descriptionroom) {
        this.descriptionroom = descriptionroom;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
