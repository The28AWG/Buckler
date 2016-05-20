package io.github.the28awg.buckler.system.entity.converter;

/**
 * This class defines the default converters.
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
public final class Converters {

    public final static LocaleConverter LOCALE = new LocaleConverter();
    public final static StringConverter STRING = new StringConverter();
    public final static BooleanConverter BOOLEAN = new BooleanConverter();
    public final static IntegerConverter INTEGER = new IntegerConverter();
    public final static LongConverter LONG = new LongConverter();
    public final static FormattedDateConverter DATE_TZ_GENERAL = new FormattedDateConverter("yyyy-MM-dd'T'HH:mm:ssz");
    public final static FormattedDateConverter DATE_TZ_RFC822 = new FormattedDateConverter("yyyy-MM-dd'T'HH:mm:ss'Z'");

}