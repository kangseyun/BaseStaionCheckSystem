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
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import org.androidtown.basestationchecksystem.Model.GMailSender;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.realm.Realm;

import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;

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
    private boolean Flag = true;
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
        Collections.shuffle(data);

        callTask();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(runnable);
        mHandler.removeCallbacksAndMessages(null);
        Flag = false;
        super.onDestroy();
    }

    public void callTask() {
        mHandler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                Random rands = new Random();
                int randomNum = rands.nextInt(240000 - 180000 + 1) + 180000;

                Random r = new Random();
                TelephonyManager tm =
                        (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                int cellID = 0;
                int lac = 0;

                switch( tm.getPhoneType()) {
                    case PHONE_TYPE_GSM: {
                        GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
                        cellID = cellLocation.getCid();
                        lac = cellLocation.getLac();

                    }
                    case PHONE_TYPE_CDMA: {
                        CdmaCellLocation cellLocation = (CdmaCellLocation) tm.getCellLocation();
                        cellID = cellLocation.getBaseStationLongitude();
                        lac = cellLocation.getBaseStationLatitude();
                    }
                    default: {
                    }
                }

                mcellID = Integer.toString(cellID);
                mlac = Integer.toString(lac);
                FirstcellID = Integer.toString(cellID);


                Intent intent = new Intent("android.intent.action.CALL", Uri.parse(data.get(r.nextInt(data.size()))));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                new CountDownTimer(randomNum, 1000) {
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
                            if(Flag) {
                                mHandler.postDelayed(runnable, 3000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        };

        mHandler.postDelayed(runnable, 1000);
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
                            to_id // to id
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