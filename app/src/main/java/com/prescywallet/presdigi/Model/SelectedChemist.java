package com.prescywallet.presdigi.Model;

public class SelectedChemist {
    private String ChemistMobileNumber;
    private String ChemistStoreName;

    public SelectedChemist(String chemistMobileNumber, String chemistStoreName) {
        ChemistMobileNumber = chemistMobileNumber;
        ChemistStoreName = chemistStoreName;
    }

    public String getChemistMobileNumber() {
        return ChemistMobileNumber;
    }

    public void setChemistMobileNumber(String chemistMobileNumber) {
        ChemistMobileNumber = chemistMobileNumber;
    }

    public String getChemistStoreName() {
        return ChemistStoreName;
    }

    public void setChemistStoreName(String chemistStoreName) {
        ChemistStoreName = chemistStoreName;
    }
}
