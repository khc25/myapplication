package com.example.myapplication;

public class  ModelUsers {

    String name,email,phoneno,image,address,emergancyppl,role,uid;

    public ModelUsers(){}

    public ModelUsers(String uid,String name, String email,String phoneno,String image,String address,String emergancyppl,String role) {
        this.uid=uid;
        this.name=name;
        this.email = email;
        this.phoneno = phoneno;
        this.image=image;
        this.address=address;
        this.emergancyppl=emergancyppl;
        this.role=role;

    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setEmergancyppl(String emergancyppl) {
        this.emergancyppl = emergancyppl;
    }

    public String getEmergancyppl() {
        return emergancyppl;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
