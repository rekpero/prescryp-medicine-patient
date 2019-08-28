package com.prescywallet.presdigi.Model;

public class DateItem {
    int day;
    int month;
    int year;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDate(){
        String date = this.day + "/" + this.month + "/" + this.year;
        return date;
    }

    public DateItem(int day, int month, int year) {

        this.day = day;
        this.month = month;
        this.year = year;
    }
}
