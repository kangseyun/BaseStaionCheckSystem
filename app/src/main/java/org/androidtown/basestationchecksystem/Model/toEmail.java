package org.androidtown.basestationchecksystem.Model;

import io.realm.RealmObject;

/**
 * Created by temp on 2017. 8. 21..
 */

public class toEmail extends RealmObject {
    private String to_email;

    public toEmail() {

    }

    public toEmail(String to_email) {
        this.to_email = to_email;
    }

    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }
}
