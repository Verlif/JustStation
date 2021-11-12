package idea.verlif.juststation.global.mod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/8/26 15:23
 */
public class JarLoadTool {

    private final String jarPath;

    public JarLoadTool(String jarPath) {
        this.jarPath = jarPath;
    }

    /**
     * 获取所有的类对象
     *
     * @param cl  目标类
     * @param <T> 类泛型
     * @return 类对象列表
     */
    public <T> List<Class<? extends T>> getClass(Class<T> cl) throws IOException, ClassNotFoundException {
        List<Class<? extends T>> list = new ArrayList<>();
        File file = new File(jarPath);
        URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> enu = jarFile.entries();
        while (enu.hasMoreElements()) {
            JarEntry jarEntry = enu.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class")) {
                Class<?> loadClass = classLoader.loadClass(name.replaceAll("/", ".").replace(".class", ""));
                if (cl.isAssignableFrom(loadClass)) {
                    list.add((Class<? extends T>) loadClass);
                }
            }
        }
        return list;
    }

    /**
     * 获取所有实例对象
     *
     * @param cl  目标类
     * @param <T> 类泛型
     * @return 实例对象列表
     */
    public <T> List<T> getObjectList(Class<T> cl) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<Class<? extends T>> classList = getClass(cl);
        List<T> list = new ArrayList<>();
        for (Class<? extends T> cla : classList) {
            if (!cla.isInterface() && !Modifier.isAbstract(cla.getModifiers())) {
                T t = cla.newInstance();
                list.add(t);
            }
        }
        return list;
    }

}
