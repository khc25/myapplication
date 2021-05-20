package com.example.myapplication.bean;

public class Contact {
    String name;
    String phonenumber;

    public Contact(){}

    public Contact(String name,String phonenumber){
        this.name=name;
        this.phonenumber=phonenumber;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
