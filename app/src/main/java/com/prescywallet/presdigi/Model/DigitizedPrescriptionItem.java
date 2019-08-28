package com.prescywallet.presdigi.Model;

public class DigitizedPrescriptionItem {
    private String prescriptionId;
    private String dateOfCreation;
    private String doctorName;
    private String patientName;
    private String dateOfConsultation;

    public DigitizedPrescriptionItem(String prescriptionId, String dateOfCreation, String doctorName, String patientName, String dateOfConsultation) {
        this.prescriptionId = prescriptionId;
        this.dateOfCreation = dateOfCreation;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.dateOfConsultation = dateOfConsultation;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDateOfConsultation() {
        return dateOfConsultation;
    }

    public void setDateOfConsultation(String dateOfConsultation) {
        this.dateOfConsultation = dateOfConsultation;
    }
}
