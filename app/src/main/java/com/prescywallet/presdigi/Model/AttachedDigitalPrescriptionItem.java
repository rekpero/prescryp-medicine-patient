package com.prescywallet.presdigi.Model;

public class AttachedDigitalPrescriptionItem {
    private String prescriptionId;
    private String date;

    public AttachedDigitalPrescriptionItem(String prescriptionId, String date) {
        this.prescriptionId = prescriptionId;
        this.date = date;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
