package io.github.the28awg.buckler.system.entity.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class converts a String (depending on a specified format) into a FormattedDate.
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
public class FormattedDateConverter extends DateConverter {

    public FormattedDateConverter(final String format) {
        super(format);
    }

    public FormattedDateConverter(final String format, final Locale locale) {
        super(format, locale);
    }

    @Override
    public FormattedDate convert(final Object attribute) {
        final Date result = super.convert(attribute);
        if (result != null) {
            return new FormattedDate(result, this.format, this.locale);
        }
        return null;
    }

    /**
     * This class represents a formatted date.
     *
     * @author Jerome Leleu
     * @since 1.1.0
     */
    public final class FormattedDate extends Date {

        private static final long serialVersionUID = 7721389956184262608L;

        private String format;

        private Locale locale;

        public FormattedDate() {
        }

        public FormattedDate(final Date date, final String format, final Locale locale) {
            super(date.getTime());
            this.format = format;
            this.locale = locale;
        }

        public String getFormat() {
            return this.format;
        }

        public void setFormat(final String format) {
            this.format = format;
        }

        public Locale getLocale() {
            return this.locale;
        }

        public void setLocale(final Locale locale) {
            this.locale = locale;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            final FormattedDate that = (FormattedDate) o;

            if (format != null ? !format.equals(that.format) : that.format != null) return false;
            return !(locale != null ? !locale.equals(that.locale) : that.locale != null);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (format != null ? format.hashCode() : 0);
            result = 31 * result + (locale != null ? locale.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            SimpleDateFormat simpleDateFormat;
            if (this.locale == null) {
                simpleDateFormat = new SimpleDateFormat(this.format);
            } else {
                simpleDateFormat = new SimpleDateFormat(this.format, this.locale);
            }
            return simpleDateFormat.format(this);
        }
    }
}