package org.androidtown.basestationchecksystem;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by temp on 2017. 9. 18..
 */

public class ContactParser {
    private File file;
    private File sdcard = null;
    private List<String> data = new ArrayList<String>();
    private int flag;

    public ContactParser(int flag) {
        String sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        sdcard = Environment.getExternalStorageDirectory();
        if(flag == 0) {
            file = new File(sdPath, "Contact.txt");

        } else {
            file = new File(sdPath, "Email.txt");
        }
    }

    public List<String> load() {
        Log.i("location", sdcard.toString());

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                data.add(line);

                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.i("Contact", e.toString());
            //You'll need to add proper error handling here
        }
        Log.i("Contact", text.toString());

        return data;
    }


}
