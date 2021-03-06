package com.pop_al.poptv.model.database;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class PasswordDBModel {
    private int id;
    private String userDetail;
    private String userPassword;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserDetail() {
        return this.userDetail;
    }

    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public PasswordDBModel(String userDetail, String userPassword) {
        this.userDetail = userDetail;
        this.userPassword = userPassword;
    }
}
