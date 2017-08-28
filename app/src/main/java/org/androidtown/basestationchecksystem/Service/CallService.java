package org.androidtown.basestationchecksystem.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import org.androidtown.basestationchecksystem.Model.DispatchData;
import org.androidtown.basestationchecksystem.Model.GMailSender;
import org.androidtown.basestationchecksystem.Model.MyInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class CallService extends IntentService {
    private Context mContext;
    private GMailSender sender;
    private String id, password, to_id;
    private List<String> data = new ArrayList<>();
    private boolean flag = false;
    private TelephonyManager tm;

    public CallService() {
        super("CallService");
    }
    class PhoneListener extends PhoneStateListener {
        String TAG = getClass().getName();

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if(flag) {
                        setEmail();
                    }
                    Log.d(TAG, "IDLE");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "RINGING");
                    // 수신 연락처에서 체크하기
                    if(check(incomingNumber)) { // 없으면 전화 끊기
                        connectCall();
                        flag = false;
                    } else { // 있으면 전화 받기
                        cancelCall();
                        flag = true;
                    }
                    break;
            }
        }
    }

    PhoneListener phoneListener = null;

    private boolean check(String number) {
        boolean result = false;
        for (String curVal : data){
            Log.i("dta", curVal);
            if (curVal.equals(number)){
                result = true;
            }
        }
        Log.i("result", result + "");
        return result;
    }

    private void cancelCall() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            telephonyService.endCall();
            Log.i("end","end");
        } catch (Exception e) {
            Log.i("Error", e.toString());
            e.printStackTrace();
        }
    }

    private void connectCall() {
        try
        {
            Intent new_intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            new_intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
            mContext.sendOrderedBroadcast(new_intent, null);
            Log.i("call", "caa123");
            new_intent = null;
        } catch (Exception e) {
            e.printStackTrace();
        }


        try
        {
            Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
            buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
            mContext.sendOrderedBroadcast(buttonUp, null);
            buttonUp = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "서비스의 onCreate");
    }

    @Override
    public void onDestroy() {
        // 서비스가 종료될 때 실행
        super.onDestroy();
        Log.i("call", "callService Destory");
        tm.listen(phoneListener, phoneListener.LISTEN_NONE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ReciveServiceList s = (ReciveServiceList) intent.getExtras().get("phone");
        data = s.getData();
        id = s.getEmail();
        password = s.getPassword();
        to_id = s.getTo_email();

        if (phoneListener == null) {
            tm = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
            phoneListener = new PhoneListener();
            tm.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        // do what you need to do here
        Log.i("call", "call");
        return START_STICKY; // you can set it as you want
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    private void setEmail() { // 이메일보내기 (보내는 사람 주소는 첫입력을 제외하고 변경하려면 앱을 종료했다가 다시 실행해야함
        sender = new GMailSender(id, password); // 보낼 이메일 주소 아이디, 비밀번호
        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Log.i("seyun",id+password);
                    sender.sendMail("기지국 변화", // subject.getText().toString(),
                            "전화 받기 성공", // body.getText().toString(),
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
