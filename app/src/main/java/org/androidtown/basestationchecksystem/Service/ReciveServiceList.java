package org.androidtown.basestationchecksystem.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by temp on 2017. 8. 18..
 */

public class ReciveServiceList implements Serializable {
    private List<String> data;
    private String email, password;
    public ReciveServiceList(List<String> data, String email, String password) {
        this.data = data;
        this.email = email;
        this.password = password;
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
}
