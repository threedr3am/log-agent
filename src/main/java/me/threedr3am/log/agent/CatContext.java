package me.threedr3am.log.agent;

import java.util.HashSet;
import java.util.Set;

/**
 * @author threedr3am
 */
public class CatContext {

    private static ThreadLocal<Set<String>> cache = new ThreadLocal();

    public static boolean check(String method) {
        if (cache.get() == null) {
            cache.set(new HashSet());
            System.out.println("[LOG-AGENT] call begin +++++++++++++++++++++++++++++++++++++++++++++++++ ");
        }
        if (cache.get().contains(method))
            return false;
        cache.get().add(method);
        return true;
    }
}
