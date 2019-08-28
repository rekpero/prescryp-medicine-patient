package com.prescywallet.presdigi.Model;

public class OrderHistoryItem {
    private String OrderNumber;
    private String DateOfOrder;
    private String TimeOfOrder;
    private String OrderStatus;
    private String GrandTotal;

    public OrderHistoryItem(String orderNumber, String dateOfOrder, String timeOfOrder, String orderStatus, String grandTotal) {
        OrderNumber = orderNumber;
        DateOfOrder = dateOfOrder;
        TimeOfOrder = timeOfOrder;
        OrderStatus = orderStatus;
        GrandTotal = grandTotal;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getDateOfOrder() {
        return DateOfOrder;
    }

    public void setDateOfOrder(String dateOfOrder) {
        DateOfOrder = dateOfOrder;
    }

    public String getTimeOfOrder() {
        return TimeOfOrder;
    }

    public void setTimeOfOrder(String timeOfOrder) {
        TimeOfOrder = timeOfOrder;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        GrandTotal = grandTotal;
    }
}
