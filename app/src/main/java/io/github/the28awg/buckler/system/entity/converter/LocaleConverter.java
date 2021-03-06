package io.github.the28awg.buckler.system.entity.converter;

import java.util.Locale;

/**
 * This class converts a String to a Locale.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public final class LocaleConverter implements AttributeConverter<Locale> {

    @Override
    public Locale convert(final Object attribute) {
        if (attribute != null) {
            if (attribute instanceof String) {
                final String s = ((String) attribute).replaceAll("-", "_");
                final String[] parts = s.split("_");
                final int length = parts.length;
                if (length == 2) {
                    return new Locale(parts[0], parts[1]);
                } else if (length == 1) {
                    return new Locale(parts[0]);
                }
            } else if (attribute instanceof Locale) {
                return (Locale) attribute;
            }
        }
        return null;
    }
}