package com.healthmantra.myapplication.Models;

public class SuggestionMo {
    private String subject;
    private String desc;
    private String name;
    private String phone;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SuggestionMo(String subject, String desc, String name, String phone) {
        this.subject = subject;
        this.desc = desc;
        this.name = name;
        this.phone = phone;
    }

    public SuggestionMo() {
    }


}
