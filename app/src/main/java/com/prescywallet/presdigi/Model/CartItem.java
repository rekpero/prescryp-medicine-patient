package com.prescywallet.presdigi.Model;

public class CartItem {
    private String MedicineName;
    private String Quantity;
    private String Price;
    private String SellerName;
    private String SellerMobileNumber;
    private String RequirePrescription;
    private String PackageContain;

    public CartItem() {
    }

    public CartItem(String medicineName, String quantity, String price, String sellerName, String sellerMobileNumber, String RequirePrescription, String PackageContain) {
        this.MedicineName = medicineName;
        this.Quantity = quantity;
        this.Price = price;
        this.SellerName = sellerName;
        this.SellerMobileNumber = sellerMobileNumber;
        this.RequirePrescription = RequirePrescription;
        this.PackageContain = PackageContain;
    }

    public CartItem(String medicineName, String quantity, String price, String sellerName, String sellerMobileNumber, String packageContain) {
        MedicineName = medicineName;
        Quantity = quantity;
        Price = price;
        SellerName = sellerName;
        SellerMobileNumber = sellerMobileNumber;
        PackageContain = packageContain;
    }

    public String getPackageContain() {
        return PackageContain;
    }

    public void setPackageContain(String packageContain) {
        PackageContain = packageContain;
    }

    public String getRequirePrescription() {
        return RequirePrescription;
    }

    public void setRequirePrescription(String requirePrescription) {
        RequirePrescription = requirePrescription;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getSellerMobileNumber() {
        return SellerMobileNumber;
    }

    public void setSellerMobileNumber(String sellerMobileNumber) {
        SellerMobileNumber = sellerMobileNumber;
    }
}
