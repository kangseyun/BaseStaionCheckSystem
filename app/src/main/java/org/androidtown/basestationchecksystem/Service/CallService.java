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

import org.androidtown.basestationchecksystem.Model.GMailSender;

import java.lang.reflect.Method;

/**
 * Created by temp on 2017. 8. 21..
 */

public class CallService extends IntentService {
    private Context mContext;
    private GMailSender sender;

    public CallService() {
        super("CallService");
    }
리얼루다가 가서 하네
    class PhoneListener extends PhoneStateListener {
        String TAG = getClass().getName();

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "IDLE");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "RINGING");

                    if(true) { // 없으면 전화 끊기
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
                    } else { // 있으면 전화 받기
                        //                    try
//                    {
//                        Intent new_intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//                        new_intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
//                        mContext.sendOrderedBroadcast(new_intent, null);
//                        Log.i("call", "caa123");
//                        new_intent = null;
//                        setEmail(1);

//                    } catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                    try
//                    {
//                        Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
//                        buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
//                        mContext.sendOrderedBroadcast(buttonUp, null);
//                        Log.i("call", "call123");
//                        buttonUp = null;
//                    } catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
                    }
                    break;
            }
        }
    }

    PhoneListener phoneListener = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "서비스의 onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (phoneListener == null) {
            TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
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

    public void setEmail(int cellid) { // 이메일보내기 (보내는 사람 주소는 첫입력을 제외하고 변경하려면 앱을 종료했다가 다시 실행해야함
        sender = new GMailSender("dcp.k953@gmail.com", "Rokcc590eks@"); // 보낼 이메일 주소 아이디, 비밀번호
        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                try {
                    sender.sendMail("기지국 변화", // subject.getText().toString(),
                            "현 기지국 " + "temp" + "에서 " + "temp" + "로 변화", // body.getText().toString(),
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
