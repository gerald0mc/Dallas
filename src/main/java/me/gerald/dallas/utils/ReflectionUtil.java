package me.gerald.dallas.utils;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author bush
 * @since 4/22/2022
 */
public class ReflectionUtil {
    private static Unsafe UNSAFE;
    private static Reflections REFLECTIONS;

    // Would use static block but java is being gay
    public static void init() {
        REFLECTIONS = new Reflections();
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Set<Class<? extends T>> getSubclasses(Class<T> clazz) {
        // We initialize REFLECTIONS on another thread because it takes a long time.
        // If your computer is slow, it may still be initializing when this is called.
        // This is a non-busy wait to ensure it is loaded before the first use.
        while (ReflectionUtil.REFLECTIONS == null) {
            synchronized (ReflectionUtil.class) {
                try {
                    ReflectionUtil.class.wait();
                } catch (InterruptedException ignored) {}
            }
        }
        return REFLECTIONS.getSubTypesOf(clazz);
    }

    public static String betterSimpleName(Class<?> clazz) {
        return StringUtils.substringAfterLast(clazz.getName(), ".");
    }

    public static List<Field> allInstanceFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        Class<?> superClass = clazz;
        for (; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Collections.addAll(list, superClass.getDeclaredFields());
        }
        list.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return list;
    }

    // Using unsafe for performance, it would work fine with plain reflection
    public static Function<Object, String> fieldValue(Field field) {
        if (Modifier.isStatic(field.getModifiers())) throw new Error("Not implemented lol");
        long offset = UNSAFE.objectFieldOffset(field);
        // lol
        if (boolean.class.equals(field.getType())) return object -> String.valueOf(UNSAFE.getBoolean(object, offset));
        else if (byte.class.equals(field.getType())) return object -> String.valueOf(UNSAFE.getByte(object, offset));
        else if (char.class.equals(field.getType())) return object -> String.valueOf(UNSAFE.getChar(object, offset));
        else if (short.class.equals(field.getType())) return object -> String.valueOf(UNSAFE.getShort(object, offset));
        else if (int.class.equals(field.getType())) return object -> String.valueOf(UNSAFE.getInt(object, offset));
        else if (long.class.equals(field.getType())) return object -> String.valueOf(UNSAFE.getLong(object, offset));
        else if (float.class.equals(field.getType())) return object -> String.valueOf(UNSAFE.getFloat(object, offset));
        else if (double.class.equals(field.getType()))
            return object -> String.valueOf(UNSAFE.getDouble(object, offset));
        else return object -> String.valueOf(UNSAFE.getObject(object, offset));
    }
}
