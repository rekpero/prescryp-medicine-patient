package com.prescywallet.presdigi.Model;

import java.util.List;

public class PrescriptionGroupItem {
    private String itemTitle;
    private List<DigitizedPrescriptionItem> listItem;

    public PrescriptionGroupItem(String itemTitle, List<DigitizedPrescriptionItem> listItem) {
        this.itemTitle = itemTitle;
        this.listItem = listItem;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<DigitizedPrescriptionItem> getListItem() {
        return listItem;
    }

    public void setListItem(List<DigitizedPrescriptionItem> listItem) {
        this.listItem = listItem;
    }
}
