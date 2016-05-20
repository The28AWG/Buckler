package io.github.the28awg.buckler.system.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.the28awg.buckler.system.CommonHelper;

/**
 * Created by the28awg on 23.04.16.
 */
public class Entity {

    protected transient final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, Object> attributes = new HashMap<>();

    /**
     * Return the attributes definition for this user profile. <code>null</code> for a (generic) user profile.
     *
     * @return the attributes definition
     */
    public Definition definition() {
        return new Definition("entity");
    }


    public Long id() {
        return getAttribute("id", Long.class);
    }

    public void id(Long id) {
        addAttribute("id", id);
    }

    public boolean isNew() {
        return getAttribute("id") == null;
    }

    /**
     * Add an attribute and perform conversion if necessary.
     *
     * @param key   key of the attribute
     * @param value value of the attribute
     */
    protected void addAttribute(final String key, Object value) {
        if (value != null) {
            final Definition definition = definition();
            // no attributes definition -> no conversion
            if (definition == null) {
                logger.debug("no conversion => key: {} / value: {} / {}", key, value, value.getClass());
                this.attributes.put(key, value);
            } else {
                value = definition.convert(key, value);
                if (value != null) {
                    Object value2;
                    if (value instanceof Object[]) {
                        value2 = new ArrayList<>(Arrays.asList((Object[]) value));
                    } else {
                        value2 = value;
                    }
                    logger.debug("converted to => key: {} / value: {} / {}", key, value2, value2.getClass());
                    this.attributes.put(key, value2);
                }
            }
        }
    }

    /**
     * Add attributes.
     *
     * @param attributes use attributes
     */
    protected void addAttributes(final Map<String, Object> attributes) {
        if (attributes != null) {
            for (final Map.Entry<String, Object> entry : attributes.entrySet()) {
                addAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Remove an attribute byt its key.
     *
     * @param key the key
     */
    protected void removeAttribute(final String key) {
        CommonHelper.assertNotNull("key", key);
        attributes.remove(key);
    }

    /**
     * Get attributes as immutable map.
     *
     * @return the immutable attributes
     */
    protected Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    /**
     * Return the attribute with name.
     *
     * @param name attribute name
     * @return the attribute with name
     */
    protected Object getAttribute(final String name) {
        return this.attributes.get(name);
    }

    /**
     * Return the attribute with name.
     *
     * @param name  the attribute name
     * @param clazz the class of the attribute
     * @param <T>   the type of the attribute
     * @return the attribute by its name
     * @since 1.8
     */
    protected <T> T getAttribute(final String name, final Class<T> clazz) {
        final Object attribute = getAttribute(name);

        if (attribute == null) {
            return null;
        }

        if (!clazz.isAssignableFrom(attribute.getClass())) {
            throw new ClassCastException("Attribute [" + name
                    + " is of type " + attribute.getClass()
                    + " when we were expecting " + clazz);
        }

        return (T) attribute;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(definition().name());
        builder.append("(");
        for (String name : definition().attributes()) {
            builder.append(name).append("=").append(getAttribute(name)).append(",");
        }
        builder.append(")");
        return builder.toString();
    }
}
