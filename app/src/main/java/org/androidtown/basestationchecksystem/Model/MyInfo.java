package org.androidtown.basestationchecksystem.Model;

import io.realm.RealmObject;

/**
 * Created by temp on 2017. 8. 21..
 */

public class MyInfo extends RealmObject {
    private String email;
    private String password;

    public MyInfo() {

    }

    public MyInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
