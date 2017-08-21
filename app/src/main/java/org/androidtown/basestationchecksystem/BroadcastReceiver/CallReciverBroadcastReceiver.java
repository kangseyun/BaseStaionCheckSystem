package org.androidtown.basestationchecksystem.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by temp on 2017. 8. 18..
 */

public class CallReciverBroadcastReceiver extends BroadcastReceiver {
    private String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG," onReceive()");



//        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
//            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//            final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);
//
//            Intent serviceIntent = new Intent(context, CallingService.class);
//            serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
//            context.startService(serviceIntent);
//
//        }



    }
}
