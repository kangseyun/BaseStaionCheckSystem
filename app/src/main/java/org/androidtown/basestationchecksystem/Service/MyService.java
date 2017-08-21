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

import io.realm.Realm;

public class MyService extends Service {
    private Realm realm;
    private GMailSender sender;
    private String mcellID,mlac,FirstcellID;
    private int count = 0;
    private Thread threads;
    private String number, head;
    private List<String> data;
    private Handler mHandler;
    private Runnable runnable;
    private String id, password;

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "서비스의 onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");
        ReciveServiceList s = (ReciveServiceList) intent.getExtras().get("phone");
        data = s.getData();
        id = s.getEmail();
        password = s.getPassword();
        Log.i("seyun", id + password);

        mHandler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                Basestation(data.get(0));
                mHandler.postDelayed(this, 15000);
            }
        };

        mHandler.postDelayed(runnable, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // 서비스가 종료될 때 실행
        mHandler.removeCallbacks(runnable);
        Log.d("test", "서비스의 onDestroy");
        super.onDestroy();
    }

    public void Basestation(String number) { // 기지국 확인
        TelephonyManager tm =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();

        int cellID = location.getCid();
        int lac = location.getLac();
        Log.i("callId", cellID + "");
        if(count == 0) { FirstcellID = Integer.toString(cellID); count = 1; } // 첫번째 기지국 정보를 대입

        mcellID = Integer.toString(cellID);
        mlac = Integer.toString(lac);

        if(Integer.parseInt(FirstcellID) != Integer.parseInt(mcellID))
        {
            Log.i("Info", "call");
            setTelephone(number, cellID);
            //FirstcellID = Integer.toString(cellID);
        } // 첫번째 기지국과 현 기지국이 다르면 이메일발송, 전화발신, 첫번째 기지국 값에 현기지국값 대입
        else {
            Log.i("Info", "call2");
        }
    }


    public void setTelephone(String number, final int cellId) { // 전화자동걸기,끊기
        Log.i("call",number);
        startActivity(new Intent("android.intent.action.CALL", Uri.parse(number))); // 전화걸을 전화번호


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
                            telephonyService.endCall();

                            setEmail(cellId); // email post

                            Log.i("end","end");
                        } catch (Exception e) {
                            Log.i("Error", e.toString());
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }, 0);

    }

    public void setEmail(int cellid) { // 이메일보내기 (보내는 사람 주소는 첫입력을 제외하고 변경하려면 앱을 종료했다가 다시 실행해야함
        sender = new GMailSender("dcp.k953@gmail.com", "Rokcc590eks@"); // 보낼 이메일 주소 아이디, 비밀번호
        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Log.i("seyun",id+password);
                    sender.sendMail("기지국 변화", // subject.getText().toString(),
                            "현 기지국 " + FirstcellID + "에서 " + mcellID + "로 변화", // body.getText().toString(),
                            "dcp.k953@gmail.com", // from.getText().toString(),
                            "tpdbs953@naver.com" // to.getText().toString()
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