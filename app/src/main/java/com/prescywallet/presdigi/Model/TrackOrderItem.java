package com.prescywallet.presdigi.Model;

public class TrackOrderItem {
    private String MedicineName;
    private String Quantity;
    private String Price;
    private String SellerName;
    private String SellerMobileNumber;
    private String PackageContain;
    private String OrderStatus;

    public TrackOrderItem(String medicineName, String quantity, String price, String sellerName, String sellerMobileNumber, String packageContain, String orderStatus) {
        MedicineName = medicineName;
        Quantity = quantity;
        Price = price;
        SellerName = sellerName;
        SellerMobileNumber = sellerMobileNumber;
        PackageContain = packageContain;
        OrderStatus = orderStatus;
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

    public String getPackageContain() {
        return PackageContain;
    }

    public void setPackageContain(String packageContain) {
        PackageContain = packageContain;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
}
