package io.github.the28awg.buckler.system;

import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by the28awg on 23.04.16.
 */
public class CommonHelper {

    /**
     * Return if the String is not blank.
     *
     * @param s string
     * @return if the String is not blank
     */
    public static boolean isNotBlank(final String s) {
        if (s == null) {
            return false;
        }
        return s.trim().length() > 0;
    }

    /**
     * Return if the String is blank.
     *
     * @param s string
     * @return if the String is blank
     */
    public static boolean isBlank(final String s) {
        return !isNotBlank(s);
    }

    /**
     * Compare two String to see if they are equals (both null is ok).
     *
     * @param s1 string
     * @param s2 string
     * @return if two String are equals
     */
    public static boolean areEquals(final String s1, final String s2) {
        return s1 == null ? s2 == null : s1.equals(s2);
    }

    /**
     * Compare two String to see if they are equals ignoring the case and the blank spaces (both null is ok).
     *
     * @param s1 string
     * @param s2 string
     * @return if two String are equals ignoring the case and the blank spaces
     */
    public static boolean areEqualsIgnoreCaseAndTrim(final String s1, final String s2) {
        if (s1 == null && s2 == null) {
            return true;
        } else if (s1 != null && s2 != null) {
            return s1.trim().equalsIgnoreCase(s2.trim());
        } else {
            return false;
        }
    }

    /**
     * Compare two String to see if they are not equals.
     *
     * @param s1 string
     * @param s2 string
     * @return if two String are not equals
     */
    public static boolean areNotEquals(final String s1, final String s2) {
        return !areEquals(s1, s2);
    }

    /**
     * Return if a collection is empty.
     *
     * @param coll a collection
     * @return whether it is empty
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Return if a collection is not empty.
     *
     * @param coll a collection
     * @return whether it is not empty
     */
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * Verify that a boolean is true otherwise throw a {@link RuntimeException}.
     *
     * @param value   the value to be checked for truth
     * @param message the message to include in the exception if the value is false
     */
    public static void assertTrue(final boolean value, final String message) {
        if (!value) {
            throw new RuntimeException(message);
        }
    }

    /**
     * Verify that a String is not blank otherwise throw a {@link RuntimeException}.
     *
     * @param name  name if the string
     * @param value value of the string
     */
    public static void assertNotBlank(final String name, final String value) {
        assertTrue(!isBlank(value), name + " cannot be blank");
    }

    /**
     * Verify that an Object is not <code>null</code> otherwise throw a {@link RuntimeException}.
     *
     * @param name name of the object
     * @param obj  object
     */
    public static void assertNotNull(final String name, final Object obj) {
        assertTrue(obj != null, name + " cannot be null");
    }

    /**
     * Verify that an Object is <code>null</code> otherwise throw a {@link RuntimeException}.
     *
     * @param name name of the object
     * @param obj  object
     */
    public static void assertNull(final String name, final Object obj) {
        assertTrue(obj == null, name + " must be null");
    }

    public static Method[] getMethods(Class clazz) {
        List<Method> result = new ArrayList<>();
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                int modifiers = method.getModifiers();
                if (Modifier.isPublic(modifiers)) {
                    result.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return result.toArray(new Method[result.size()]);
    }

    public static void printMethods(Logger logger, Class<?> clazz) {
        logger.info("Class: {}", clazz.toString());
        for (Method method : getMethods(clazz)) {
            logger.info("     {}", toString(method));
        }
    }

    public static String toString(Method method) {
        StringBuilder result = new StringBuilder(Modifier.toString(method.getModifiers()));

        if (result.length() != 0) {
            result.append(' ');
        }
        result.append(method.getReturnType().getName());
        result.append(' ');
        result.append(method.getName());
        result.append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        result.append(toString(parameterTypes));
        result.append(")");
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes.length != 0) {
            result.append(" throws ");
            result.append(toString(exceptionTypes));
        }
        return result.toString();
    }

    public static String toString(Class<?>[] types) {
        if (types.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        appendTypeName(result, types[0]);
        for (int i = 1; i < types.length; i++) {
            result.append(',');
            appendTypeName(result, types[i]);
        }
        return result.toString();
    }

    public static void appendTypeName(StringBuilder out, Class<?> c) {
        int dimensions = 0;
        while (c.isArray()) {
            c = c.getComponentType();
            dimensions++;
        }
        out.append(c.getName());
        for (int d = 0; d < dimensions; d++) {
            out.append("[]");
        }
    }
}
