package info.gps360.gpcam.sms_listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import info.gps360.gpcam.utility.Constants;
import info.gps360.gpcam.utility.SharedValues;

/*
 * Copyright (c) 2018-2019 by Vismap Labs Pvt. Lt.

 * All Rights Reserved

 * Company Confidential

 */
public class SmsListener extends BroadcastReceiver {


    private SharedPreferences preferences;
    SmsManager smsManager = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Log.e("sms ",msg_from);
                        operation(context,msgBody,msg_from  );
                        Log.e("sms ",msgBody);

                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }

    public void operation(Context context,String msgBody, String msgFrom){
        try {
            String[] clean=msgBody.split("#");
            String[] contents=clean[0].split(",");

            switch (contents[0]) {
                case Constants.SOS:
                    if (contents[1].equals("A")){
                        SharedValues.saveValue(context,Constants.SOS,contents[2]);
                        smsManager.sendTextMessage(msgFrom, null, "OK, SOS: "+contents[2], null, null);

                    }
                    break;
                case Constants.SERVER:
                    if (contents[1].equals(Constants.GPS)){
                        SharedValues.saveValue(context,Constants.GPS_URL,contents[2]);

                        smsManager.sendTextMessage(msgFrom, null, "OK", null, null);

                    }
                    else if (contents[1].equals(Constants.CAMERA)){
                    SharedValues.saveValue(context, Constants.CAMERA_URL, contents[2]);
                    smsManager.sendTextMessage(msgFrom, null, "OK", null, null);
                    }
                    break;
                case Constants.PARAM:
                    smsManager.sendTextMessage(msgFrom, null, "IMEI:"+SharedValues.getValue(context,Constants.IMEI)+"; TIMER GPS:"+"; TIMER CAMERA:" +"; SOS:"+SharedValues.getValue(context,Constants.SOS)+";", null, null);
                    break;

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}