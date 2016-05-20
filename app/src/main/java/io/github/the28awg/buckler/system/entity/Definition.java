package io.github.the28awg.buckler.system.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.the28awg.buckler.system.entity.converter.AttributeConverter;
import io.github.the28awg.buckler.system.entity.converter.Converters;

/**
 * Created by the28awg on 23.04.16.
 */
public class Definition {

    private String name;
    private List<String> attributes = new ArrayList<>();
    private Map<String, AttributeConverter<?>> converters = new HashMap<>();

    public Definition(String name) {
        this.name = name;
        attribute("id", Converters.LONG);
    }

    public String name() {
        return name;
    }

    public List<String> attributes() {
        return this.attributes;
    }

    public Definition attribute(final String name, final AttributeConverter<?> converter) {
        attributes.add(name);
        converters.put(name, converter);
        return this;
    }

    public Object convert(final String name, final Object value) {
        AttributeConverter<?> converter = this.converters.get(name);
        if (converter != null && value != null) {
            return converter.convert(value);
        } else {
            return value;
        }
    }
}