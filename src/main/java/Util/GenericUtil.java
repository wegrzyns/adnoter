package Util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

public class GenericUtil {

    public static <K,V> Function<K,V> cache(Function<K,V> f, Map<K,V> cache)
    {
        return k -> cache.computeIfAbsent(k, f);
    }

    public static <K,V> Function<K,V> cache(Function<K,V> f)
    {
        return cache(f, new IdentityHashMap<>());
    }
}
