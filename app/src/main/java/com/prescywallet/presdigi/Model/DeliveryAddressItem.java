package com.prescywallet.presdigi.Model;

public class DeliveryAddressItem {
    private String locality;
    private String completeAddress;
    private String deliveryInstruction;
    private String deliveryLatitude;
    private String deliveryLongitude;
    private String deliveryNickname;

    public DeliveryAddressItem(String locality, String completeAddress, String deliveryInstruction, String deliveryNickname, String deliveryLatitude, String deliveryLongitude) {
        this.locality = locality;
        this.completeAddress = completeAddress;
        this.deliveryInstruction = deliveryInstruction;
        this.deliveryNickname = deliveryNickname;
        this.deliveryLatitude = deliveryLatitude;
        this.deliveryLongitude = deliveryLongitude;
    }

    public String getDeliveryNickname() {
        return deliveryNickname;
    }

    public void setDeliveryNickname(String deliveryNickname) {
        this.deliveryNickname = deliveryNickname;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
    }

    public String getDeliveryInstruction() {
        return deliveryInstruction;
    }

    public void setDeliveryInstruction(String deliveryInstruction) {
        this.deliveryInstruction = deliveryInstruction;
    }

    public String getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(String deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    public String getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(String deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }
}
