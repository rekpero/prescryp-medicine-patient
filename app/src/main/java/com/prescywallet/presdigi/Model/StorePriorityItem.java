package com.prescywallet.presdigi.Model;

public class StorePriorityItem {
    private String chemistMobile;
    private String chemistName;
    private String quantity;
    private int priority;

    public StorePriorityItem(String chemistMobile, String chemistName, String quantity, int priority) {
        this.chemistMobile = chemistMobile;
        this.chemistName = chemistName;
        this.quantity = quantity;
        this.priority = priority;
    }

    public String getChemistMobile() {
        return chemistMobile;
    }

    public void setChemistMobile(String chemistMobile) {
        this.chemistMobile = chemistMobile;
    }

    public String getChemistName() {
        return chemistName;
    }

    public void setChemistName(String chemistName) {
        this.chemistName = chemistName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
