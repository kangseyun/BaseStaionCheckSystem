package org.androidtown.basestationchecksystem.Model;

<<<<<<< HEAD
import java.io.Serializable;

import io.realm.RealmObject;

public class DispatchData extends RealmObject implements Serializable  {
=======
import io.realm.RealmObject;

public class DispatchData extends RealmObject {
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
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