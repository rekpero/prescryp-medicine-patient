package com.prescywallet.presdigi.Model;

import java.util.List;

public class MedicinePackagePriorityItem {
    private String Package;
    private List<StorePriorityItem> storePriorityItems;

    public MedicinePackagePriorityItem(String aPackage, List<StorePriorityItem> storePriorityItems) {
        Package = aPackage;
        this.storePriorityItems = storePriorityItems;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public List<StorePriorityItem> getStorePriorityItems() {
        return storePriorityItems;
    }

    public void setStorePriorityItems(List<StorePriorityItem> storePriorityItems) {
        this.storePriorityItems = storePriorityItems;
    }
}
