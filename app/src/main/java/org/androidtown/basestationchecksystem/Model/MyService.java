package org.androidtown.basestationchecksystem.Model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import org.androidtown.basestationchecksystem.Activity.EmailActivity;

import java.lang.reflect.Method;

public class MyService extends Service {

    private GMailSender sender;
    private String mcellID,mlac,FirstcellID;
    private int count = 0;

    public MyService() {
    }

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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        Log.d("test", "서비스의 onDestroy");
    }

    public void Basestation() { // 기지국 확인
        TelephonyManager tm =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();

        int cellID = location.getCid();
        int lac = location.getLac();

        if(count == 0) { FirstcellID = Integer.toString(cellID); count = 1; } // 첫번째 기지국 정보를 대입

        mcellID = Integer.toString(cellID);
        mlac = Integer.toString(lac);

        if(Integer.parseInt(FirstcellID) != Integer.parseInt(mcellID))
        { setTelephone(); setEmail(); FirstcellID = Integer.toString(cellID); } // 첫번째 기지국과 현 기지국이 다르면 이메일발송, 전화발신, 첫번째 기지국 값에 현기지국값 대입
    }

    public void setTelephone() { // 전화자동걸기,끊기
        startActivity(new Intent("android.intent.action.CALL",
                Uri.parse("tel:010-2546-4762"))); // 전화걸을 전화번호
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void setEmail() { // 이메일보내기 (보내는 사람 주소는 첫입력을 제외하고 변경하려면 앱을 종료했다가 다시 실행해야함
        sender = new GMailSender(EmailActivity.semail_from, EmailActivity.spassword_from); // 보낼 이메일 주소 아이디, 비밀번호
        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                try {
                    sender.sendMail("기지국 변화", // subject.getText().toString(),
                            "현 기지국 " + FirstcellID + "에서 " + mcellID + "로 변화", // body.getText().toString(),
                            EmailActivity.semail_from, // from.getText().toString(),
                            EmailActivity.semail_to // to.getText().toString()
                    );
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(MyService.this, "신청 실패", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            private void sleep(int i) {
                // TODO Auto-generated method stub
            }
        }).start();
    }
}