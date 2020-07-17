package me.threedr3am.log.agent;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author threedr3am
 */
public class Agent {

    public static void premain(String agentArg, Instrumentation inst) {
        init(agentArg, inst);
    }

    public static void agentmain(String agentArg, Instrumentation inst) {
        init(agentArg, inst);
    }

    public static synchronized void init(String action, Instrumentation inst) {
        System.out.println("[LOG-AGENT] running ...");
        System.out.println("[LOG-AGENT] init ...");
        System.out.println("[LOG-AGENT] agentArg: " + action);
        try {
            JarFileHelper.addJarToBootstrap(inst);
            CatClassFileTransformer catClassFileTransformer = new CatClassFileTransformer();
            if (action != null && !action.isEmpty()) {
                catClassFileTransformer.setPkgPattern(action);
            }
            inst.addTransformer(catClassFileTransformer, true);
            Class[] classes = inst.getAllLoadedClasses();
            List<Class> classList =  Arrays.asList(classes).stream()
                .filter(c -> catClassFileTransformer.getPattern().matcher(c.getName()).find() && inst.isModifiableClass(c))
                .collect(Collectors.toList());
            classList.forEach(aClass -> System.out.println("[LOG-AGENT] retransformClasses ------------> " + aClass.getName()));
            classes = classList.toArray(new Class[0]);
            if (classes.length > 0) {
                inst.retransformClasses(classes);
            }
        } catch (Throwable e) {
            System.err.println("[LOG-AGENT] Failed to initialize, will continue without security protection.");
            e.printStackTrace();
        }
    }
}
