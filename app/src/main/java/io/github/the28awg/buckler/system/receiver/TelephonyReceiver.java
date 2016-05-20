package io.github.the28awg.buckler.system.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.the28awg.buckler.system.Telephony;

public class TelephonyReceiver extends BroadcastReceiver {

    private static final Logger logger = LoggerFactory.getLogger(TelephonyReceiver.class);

    public TelephonyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String phone_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
                try {
                    Phonenumber.PhoneNumber number = instance.parse(phone_number, "");
                    phone_number = instance.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
                } catch (NumberParseException e) {
                    logger.info("NumberParseException was thrown: " + e.toString());
                }
                logger.info("intent.getAction(): {}, thread: {}, this: {}, state: {}, phone_number: {}",
                        intent.getAction(),
                        Thread.currentThread().getName(), getClass().getSimpleName() + '@' + String.format("%-7s", Integer.toHexString(hashCode())),
                        String.format("[%7s]", state),
                        phone_number);
                Telephony.disconnect(true);
            }
        }
    }
}
