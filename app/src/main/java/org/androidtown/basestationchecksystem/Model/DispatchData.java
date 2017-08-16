package org.androidtown.basestationchecksystem.Model;

import io.realm.RealmObject;

public class DispatchData extends RealmObject {
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