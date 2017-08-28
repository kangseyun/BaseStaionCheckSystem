package org.androidtown.basestationchecksystem.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by temp on 2017. 8. 18..
 */

public class ReciveServiceList implements Serializable {
    private List<String> data;
    private String email, password, to_email;

    public ReciveServiceList(List<String> data, String email, String password, String to_email) {
        this.data = data;
        this.email = email;
        this.password = password;
        this.to_email = to_email;
    }

    public ReciveServiceList(String email, String password, String to_email) {
        this.email = email;

        this.password = password;
        this.to_email = to_email;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
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

    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }
}
