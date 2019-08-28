package com.prescywallet.presdigi.Model;

public class MedicineObject {
    private String medicineName;

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public MedicineObject(String medicineName) {

        this.medicineName = medicineName;
    }
}
