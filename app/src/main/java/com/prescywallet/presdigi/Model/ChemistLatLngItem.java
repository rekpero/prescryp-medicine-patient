package com.prescywallet.presdigi.Model;

import com.google.android.gms.maps.model.LatLng;

public class ChemistLatLngItem {
    private String chemistMobileNumber;
    private String chemistStoreName;
    private LatLng latLng;

    public ChemistLatLngItem(String chemistMobileNumber, String chemistStoreName, LatLng latLng) {
        this.chemistMobileNumber = chemistMobileNumber;
        this.chemistStoreName = chemistStoreName;
        this.latLng = latLng;
    }

    public String getChemistMobileNumber() {
        return chemistMobileNumber;
    }

    public void setChemistMobileNumber(String chemistMobileNumber) {
        this.chemistMobileNumber = chemistMobileNumber;
    }

    public String getChemistStoreName() {
        return chemistStoreName;
    }

    public void setChemistStoreName(String chemistStoreName) {
        this.chemistStoreName = chemistStoreName;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
