package com.luoheng.miu.bean;

public class User {
    public enum State {
        REGISTERED,
        UNACTIVE
    }
    private int id;
    private String mail;
    private String name;
    private String passwords;
    private State state;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public State getState() {
        return state;
    }

    public String getStateString(){
        if(state==State.REGISTERED)
            return "Registered";
        else
            return "UnActive";
    }

    public void setState(State state) {
        this.state = state;
    }
    public void setState(String state){
        if(state.equals("Registered")){
            this.state=State.REGISTERED;
        }
        else if(state.equals("UnActive")){
            this.state=State.UNACTIVE;
        }
    }
}
