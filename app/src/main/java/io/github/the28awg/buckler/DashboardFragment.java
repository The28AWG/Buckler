package io.github.the28awg.buckler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.the28awg.buckler.system.Telephony;
import io.github.the28awg.buckler.system.entity.EntityHelper;
import io.github.the28awg.buckler.system.entity.PhoneNumber;
import io.github.the28awg.buckler.system.sql.Selection;

public class DashboardFragment extends HelperFragment {

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        PhoneNumber number = EntityHelper.entity(PhoneNumber.class);
        if (number != null) {
            number.definition().attributes();
            number.phone_number(Telephony.randomRawPhoneNumber());
            EntityHelper.insert(number);
            System.out.println(number.toString());

            Selection selection = Selection.selection("");
            for (PhoneNumber phoneNumber : EntityHelper.select(selection, PhoneNumber.class)) {
                System.out.println(phoneNumber.toString());
            }
        }
        return root;
    }
}
