package com.prescywallet.presdigi.Model;

public class User {
    private String id;
    private String name;
    private String gender;
    private String mobile_num;
    private String email;
    private String imageUrl;
    private String password;
    private String custId;



    public User(String id, String name, String gender, String mobile_num, String email, String imageUrl, String custId, String password) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.mobile_num = mobile_num;
        this.email = email;
        this.imageUrl = imageUrl;
        this.custId = custId;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String id) {
        this.custId = custId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User(){

    }
}
