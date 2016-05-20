package io.github.the28awg.buckler.system.entity;

import io.github.the28awg.buckler.system.entity.converter.Converters;

/**
 * Created by the28awg on 23.04.16.
 */
public class PhoneNumber extends Entity {

    private static final Definition DEFINITION = new Definition("phone_number")
            .attribute("phone_number", Converters.STRING);

    @Override
    public Definition definition() {
        return DEFINITION;
    }

    public String phone_number() {
        return getAttribute("phone_number", String.class);
    }

    public PhoneNumber phone_number(String phone_number) {
        addAttribute("phone_number", phone_number);
        return this;
    }
}
