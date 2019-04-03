package com.luoheng.miu.bean;

public class UserRegister {
    private int id;
    private String mail;
    private String token;

    public UserRegister(){

    }
    public UserRegister(String mail,String token){
        this.mail=mail;
        this.token=token;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
