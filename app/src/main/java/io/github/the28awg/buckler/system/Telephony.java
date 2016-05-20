package io.github.the28awg.buckler.system;

import android.os.Binder;
import android.os.IBinder;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * Created by the28awg on 22.04.16.
 */
public class Telephony {

    private static final Logger logger = LoggerFactory.getLogger(Telephony.class);

    // TODO: 22.04.16 http://stackoverflow.com/a/20267763

    public static void disconnect(boolean silence) {
        try {
            Class<?> telephonyClass = Class.forName("com.android.internal.telephony.ITelephony");
            //CommonHelper.printMethods(logger, telephonyClass);
            Class<?> telephonyStubClass = telephonyClass.getClasses()[0];
            Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");
            Class<?> serviceManagerNativeClass = Class.forName("android.os.ServiceManagerNative");
            Method getService = serviceManagerClass.getMethod("getService", String.class);
            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
            Binder tmpBinder = new Binder();
            tmpBinder.attachInterface(null, "fake");
            Object serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
            IBinder binder = (IBinder) getService.invoke(serviceManagerObject, "phone");
            Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
            Object telephonyObject = serviceMethod.invoke(null, binder);
            if (silence) {
                Method telephonySilenceRinger = telephonyClass.getMethod("silenceRinger");
                telephonySilenceRinger.invoke(telephonyObject);
            }
            Method telephonyEndCall = telephonyClass.getMethod("endCall");
            telephonyEndCall.invoke(telephonyObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String randomRawPhoneNumber() {
        String tmp = "8963";
        String format = "";
        Random random = new Random();
        for (int i = 1; i <= 7; i++) {
            tmp = tmp.concat(Integer.toString(random.nextInt(9)));
        }
        try {
            Phonenumber.PhoneNumber number = PhoneNumberUtil.getInstance().parse(tmp, "RU");
            format = PhoneNumberUtil.getInstance().format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return format;
    }
}
