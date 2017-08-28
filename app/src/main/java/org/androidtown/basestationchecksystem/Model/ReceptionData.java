package org.androidtown.basestationchecksystem.Model;

import java.io.Serializable;

import io.realm.RealmObject;

public class ReceptionData extends RealmObject implements Serializable {
    private String text;

    public ReceptionData() {

    }
    public ReceptionData(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}