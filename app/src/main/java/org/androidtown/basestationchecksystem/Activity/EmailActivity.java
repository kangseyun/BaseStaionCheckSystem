package org.androidtown.basestationchecksystem.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.androidtown.basestationchecksystem.Model.MyInfo;
import org.androidtown.basestationchecksystem.Model.ReceptionData;
import org.androidtown.basestationchecksystem.Model.toEmail;
import org.androidtown.basestationchecksystem.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText memail_from, memail_to, mpassword_from;
    private Button mchange_from, mchange_to;
    private String semail_from, spassword_from, semail_to;
    private Toolbar toolbar;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);

        realm = Realm.getDefaultInstance();

        memail_from = (EditText) findViewById(R.id.email_from);
        memail_to = (EditText) findViewById(R.id.email_to);
        mpassword_from = (EditText) findViewById(R.id.password_from);
        mchange_from = (Button) findViewById(R.id.change_from);
        mchange_from.setOnClickListener(this);
        mchange_to = (Button) findViewById(R.id.change_to);
        mchange_to.setOnClickListener(this);

        realm.beginTransaction();
        final RealmResults<MyInfo> results = realm.where(MyInfo.class).findAll();
        final RealmResults<toEmail> results2 = realm.where(toEmail.class).findAll();

        if(results.size() != 0) {
            memail_from.setText(results.get(0).getEmail());
            mpassword_from.setText(results.get(0).getPassword());
        }

        if(results2.size() != 0) {
            memail_to.setText(results2.get(0).getTo_email());
        }

        realm.commitTransaction();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("기지국");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                        spassword_from = mpassword_from.getText().toString();

                        realm.beginTransaction();
                        final RealmResults<MyInfo> results = realm.where(MyInfo.class).findAll();

                        if(results.size() == 0) {
                            MyInfo data = realm.createObject(MyInfo.class); // 새 객체 만들기
                            data.setEmail(semail_from);
                            data.setPassword(spassword_from);
                        } else {
                            MyInfo info = results.get(0);
                            info.setPassword(spassword_from);
                            info.setEmail(semail_from);
                        }

                        realm.commitTransaction();
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
                        realm.beginTransaction();
                        final RealmResults<toEmail> results = realm.where(toEmail.class).findAll();

                        if(results.size() == 0) {
                            toEmail data = realm.createObject(toEmail.class); // 새 객체 만들기
                            data.setTo_email(semail_to);
                        } else {
                            toEmail info = results.get(0);
                            info.setTo_email(semail_to);
                        }

                        realm.commitTransaction();
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
