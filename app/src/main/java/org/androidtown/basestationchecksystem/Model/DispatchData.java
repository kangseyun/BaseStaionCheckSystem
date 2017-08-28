package org.androidtown.basestationchecksystem.Model;

import java.io.Serializable;

import io.realm.RealmObject;

public class DispatchData extends RealmObject implements Serializable  {
    private String text;
    public DispatchData() {

    }
    public DispatchData(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}