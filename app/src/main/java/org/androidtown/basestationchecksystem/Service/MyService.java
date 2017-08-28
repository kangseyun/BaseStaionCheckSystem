package org.androidtown.basestationchecksystem.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import org.androidtown.basestationchecksystem.Model.GMailSender;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import io.realm.Realm;

public class MyService extends Service {
    private GMailSender sender;
    private String mcellID, mlac, FirstcellID;
    private int count = 0;
    private List<String> data;
    private Handler mHandler;
    private Runnable runnable;
    private String id, password;
    private String to_id;
    private String TAG = this.getClass().getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ReciveServiceList s = (ReciveServiceList) intent.getExtras().get("phone");

        data = s.getData();
        id = s.getEmail();
        password = s.getPassword();
        to_id = s.getTo_email();

        mHandler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                BaseSation();
                mHandler.postDelayed(this, 15000);
            }
        };

        mHandler.postDelayed(runnable, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(runnable);
        super.onDestroy();
    }

    public void BaseSation() {
        TelephonyManager tm =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();

        int cellID = location.getCid();
        int lac = location.getLac();

        mcellID = Integer.toString(cellID);
        mlac = Integer.toString(lac);
        FirstcellID = Integer.toString(cellID);

        Random r = new Random();
        setTelephone(data.get(r.nextInt(data.size())));
    }


    public void setTelephone(String number) { // 전화자동걸기,끊기
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse(number));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent); // 전화걸을 전화번호

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(10000, 1000) { //(밀리초(현재10초),??) 걸고 끊기
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        try {
                            Class c = Class.forName(tm.getClass().getName());
                            Method m = c.getDeclaredMethod("getITelephony");
                            m.setAccessible(true);
                            ITelephony telephonyService = (ITelephony) m.invoke(tm);
                            telephonyService.endCall(); //전화 끊기

                            setEmail(); // email post
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }, 0);

    }

    private void setEmail() {
        sender = new GMailSender(id, password); // 보낼 이메일 주소 아이디, 비밀번호
        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Log.i("seyun",id+password);
                    sender.sendMail("기지국 변화", // subject.getText().toString(),
                            "현 기지국 : " + FirstcellID, // body.getText().toString(),
                            id, // from id
                            "tpdbs953@naver.com" // to id
                    );
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

            private void sleep(int i) {
                // TODO Auto-generated method stub
            }
        }).start();
    }
}