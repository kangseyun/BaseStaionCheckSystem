package org.androidtown.basestationchecksystem.Model;

import io.realm.RealmObject;

public class ReceptionData extends RealmObject {
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