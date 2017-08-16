package org.androidtown.basestationchecksystem.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.androidtown.basestationchecksystem.R;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText memail_from, memail_to, mpassword_from;
    private Button mchange_from, mchange_to;
    public static String semail_from, spassword_from, semail_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);
        memail_from = (EditText) findViewById(R.id.email_from);
        memail_to = (EditText) findViewById(R.id.email_to);
        mpassword_from = (EditText) findViewById(R.id.password_from);
        mchange_from = (Button) findViewById(R.id.change_from);
        mchange_from.setOnClickListener(this);
        mchange_to = (Button) findViewById(R.id.change_to);
        mchange_to.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.change_from:
                AlertDialog.Builder alert = new AlertDialog.Builder(EmailActivity.this);

                alert.setTitle(R.string.alert_title);
                alert.setMessage(R.string.alert_message);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        semail_from = memail_from.getText().toString();
                        memail_from.setText("");
                        spassword_from = mpassword_from.getText().toString();
                        mpassword_from.setText("");
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        memail_from.setText("");
                        mpassword_from.setText("");
                    }
                });
                alert.show();
                break;
            case R.id.change_to:
                AlertDialog.Builder malert = new AlertDialog.Builder(EmailActivity.this);

                malert.setTitle(R.string.malert_title);
                malert.setMessage(R.string.malert_message);

                malert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        semail_to = memail_to.getText().toString();
                        memail_to.setText("");
                    }
                });
                malert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        memail_to.setText("");
                    }
                });
                malert.show();
                break;
        }
    }
}
