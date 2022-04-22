package me.gerald.dallas.utils;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import java.util.Set;

/**
 * @author bush
 * @since 4/22/2022
 */
public class ReflectionUtil {
    private static final Reflections REFLECTIONS = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner()));

    public static <T> Set<Class<? extends T>> getSubclasses(Class<T> clazz) {
        return REFLECTIONS.getSubTypesOf(clazz);
    }

    //public static String si
}
