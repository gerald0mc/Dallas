package me.gerald.dallas.utils;

import net.minecraft.network.Packet;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import java.util.Set;

/**
 * @author bush
 * @since 4/22/2022
 */
public class ReflectionUtil {
    public static final Reflections REFLECTIONS = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner()));

    public static String betterSimpleName(Class<?> clazz) {
        String full = clazz.getName();
        String pckg = clazz.getPackage().getName();
        return StringUtils.substringAfter(full, pckg + ".");
    }
}
