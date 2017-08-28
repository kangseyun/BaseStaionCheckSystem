package org.androidtown.basestationchecksystem.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by temp on 2017. 8. 18..
 */

public class ReciveServiceList implements Serializable {
    private List<String> data;
<<<<<<< HEAD
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
=======
    private String email, password;
    public ReciveServiceList(List<String> data, String email, String password) {
        this.data = data;
        this.email = email;
        this.password = password;
    }

>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7

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
<<<<<<< HEAD

    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }
=======
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
}
