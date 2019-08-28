package com.prescywallet.presdigi.Model;

public class MedicineAvailableItem {
    private String chemistMobileNumber;
    private String chemistStoreName;
    private String quantity;
    private String packaging;
    private String medicineName;
    private int priority;


    public MedicineAvailableItem(String chemistMobileNumber, String chemistStoreName, String quantity, String packaging, String medicineName, int priority) {
        this.chemistMobileNumber = chemistMobileNumber;
        this.chemistStoreName = chemistStoreName;
        this.quantity = quantity;
        this.packaging = packaging;
        this.medicineName = medicineName;
        this.priority = priority;
    }

    public String getChemistStoreName() {
        return chemistStoreName;
    }

    public void setChemistStoreName(String chemistStoreName) {
        this.chemistStoreName = chemistStoreName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getChemistMobileNumber() {
        return chemistMobileNumber;
    }

    public void setChemistMobileNumber(String chemistMobileNumber) {
        this.chemistMobileNumber = chemistMobileNumber;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}
