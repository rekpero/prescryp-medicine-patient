package com.prescywallet.presdigi.Model;

public class MedicineItem {
    private String MedicineName;

    public MedicineItem(String MedicineName) {
        this.MedicineName = MedicineName;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }
}
