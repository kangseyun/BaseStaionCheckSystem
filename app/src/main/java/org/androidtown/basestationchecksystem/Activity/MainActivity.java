package org.androidtown.basestationchecksystem.Activity;

import android.content.Intent;
<<<<<<< HEAD
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
=======
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

<<<<<<< HEAD
import org.androidtown.basestationchecksystem.Model.DispatchData;
import org.androidtown.basestationchecksystem.Model.MyInfo;
import org.androidtown.basestationchecksystem.Model.ReceptionData;
import org.androidtown.basestationchecksystem.Model.toEmail;
=======
import org.androidtown.basestationchecksystem.BroadcastReceiver.CallReciverBroadcastReceiver;
import org.androidtown.basestationchecksystem.Model.MyInfo;
import org.androidtown.basestationchecksystem.Model.ReceptionData;
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
import org.androidtown.basestationchecksystem.Service.CallService;
import org.androidtown.basestationchecksystem.Service.MyService;
import org.androidtown.basestationchecksystem.R;
import org.androidtown.basestationchecksystem.Service.ReciveServiceList;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
<<<<<<< HEAD
    private Button dispatch, reception, email, START, STOP, CALL_START, CALL_STOP;
    private Toolbar toolbar;
    private Realm realm;
    private ReciveServiceList obj;

=======
    private Button dispatch, reception, email, START, STOP;
    private Toolbar toolbar;
    private Realm realm;
    private ReciveServiceList obj;
    private CallReciverBroadcastReceiver callReciverBroadcastReceivers;
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        databaseInit();

=======

        databaseInit();


        Intent s = new Intent(this, CallService.class);
        startService(s);
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
        dispatch = (Button) findViewById(R.id.dispatch);
        dispatch.setOnClickListener(this);
        reception = (Button) findViewById(R.id.reception);
        reception.setOnClickListener(this);
        email = (Button) findViewById(R.id.email);
        email.setOnClickListener(this);
        START = (Button) findViewById(R.id.START);
        START.setOnClickListener(this);
        STOP = (Button) findViewById(R.id.STOP);
        STOP.setOnClickListener(this);

<<<<<<< HEAD
        CALL_START = (Button) findViewById(R.id.call_START);
        CALL_STOP = (Button) findViewById(R.id.call_STOP);

        CALL_START.setOnClickListener(this);
        CALL_STOP.setOnClickListener(this);

=======
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("기지국");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

<<<<<<< HEAD
    private ReciveServiceList getPhoneNumber(int id) {
        List<String> lists = new ArrayList<String>();

        realm.beginTransaction();

        if(id == 0) {
            RealmQuery<DispatchData> query = realm.where(DispatchData.class);
            RealmResults<DispatchData> result = query.findAll();

            for (DispatchData data:
                    result) {
                lists.add(data.getText());
            }
        } else {
            RealmQuery<ReceptionData> query = realm.where(ReceptionData.class);
            RealmResults<ReceptionData> result = query.findAll();

            for (ReceptionData obj : result) {
                lists.add("tel:" + obj.getText());
            }
=======
    private ReciveServiceList getPhoneNumber() {
        List<String> lists = new ArrayList<String>();

        realm.beginTransaction();
        RealmQuery<ReceptionData> query = realm.where(ReceptionData.class);
        RealmResults<ReceptionData> result = query.findAll();

        for (ReceptionData obj: result) {
            lists.add("tel:"+obj.getText());
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
        }

        RealmQuery<MyInfo> query2 = realm.where(MyInfo.class);
        MyInfo result2 = query2.findAll().first();
<<<<<<< HEAD

        RealmQuery<toEmail> query3 = realm.where(toEmail.class);
        toEmail result3 = query3.findAll().first();

        Log.i("info", result2.getEmail() + ", " + result2.getPassword());
        obj = new ReciveServiceList(lists, result2.getEmail(), result2.getPassword(), result3.getTo_email());
=======
        Log.i("info", result2.getEmail() + ", " + result2.getPassword());
        obj = new ReciveServiceList(lists, result2.getEmail(), result2.getPassword());
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
        realm.commitTransaction();


        return obj;
    }

    public void databaseInit() {
        Realm.init(this);

        RealmConfiguration realmConfiguration =
                new RealmConfiguration.Builder()
                        .name("realm-sample.db")
                        .schemaVersion(1)
                        .deleteRealmIfMigrationNeeded()
                        .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        realm = Realm.getDefaultInstance();
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
            case R.id.dispatch:
                Intent intent = new Intent(this, DispatchActivity.class);
                startActivity(intent);
                break;
            case R.id.reception:
                Intent intent2 = new Intent(this, ReceptionActivity.class);
                startActivity(intent2);
                break;
            case R.id.email:
                Intent intent3 = new Intent(this, EmailActivity.class);
                startActivity(intent3);
                break;
            case R.id.START:
                RealmQuery<ReceptionData> query = realm.where(ReceptionData.class);
                RealmResults<ReceptionData> result = query.findAll();
                if(result.size() == 0) {
                    Toast toast = Toast.makeText(this, "연락처를 추가해주세요", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Log.d("test", "액티비티-서비스 시작버튼클릭");
                    Toast.makeText(this, "기지국 감지가 시작되었습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent4 = new Intent(this, MyService.class);
<<<<<<< HEAD
                    intent4.putExtra("phone", getPhoneNumber(1));
=======
                    intent4.putExtra("phone", getPhoneNumber());
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
                    startService(intent4);
                }
                break;
            case R.id.STOP:
                Log.d("test", "액티비티-서비스 종료버튼클릭");
                Toast.makeText(this, "기지국 감지가 중지되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(this, MyService.class);
                stopService(intent5);
                break;
<<<<<<< HEAD
            case R.id.call_START:
                Intent callStart = new Intent(this, CallService.class);
                callStart.putExtra("phone", getPhoneNumber(0));
                startService(callStart);
                Toast.makeText(this, "전화 수신 감지가 시작되었습니다.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.call_STOP:
                Intent callStop = new Intent(this, CallService.class);
                stopService(callStop);
                Toast.makeText(this, "전화 수신 감지가 중지되었습니다.",Toast.LENGTH_SHORT).show();
                break;
=======
>>>>>>> 9b84b0cf3dc3f56879485cc5d330adf14cac83b7
        }
    }
}