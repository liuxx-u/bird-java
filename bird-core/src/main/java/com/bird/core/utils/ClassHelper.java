package com.bird.core.utils;

import com.bird.core.exception.DataParseException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author liuxx
 * @date 2017/5/16
 */
public final class ClassHelper {
    private ClassHelper() {
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param pack
     * @return
     */
    public static final Set<Class<?>> getClasses(String pack){
        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath,recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class")&& !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.'+ className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        File[] dirfiles = dir.listFiles(file -> (recursive && file.isDirectory())|| (file.getName().endsWith(".class")));
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."+ file.getName(), file.getAbsolutePath(), recursive,classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取集合中所有clazz的子类
     * @param clazz
     * @param classesAll
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Set<Class<?>> getByInterface(Class clazz, Set<Class<?>> classesAll) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        try {
            /**
             * 循环判断路径下的所有类是否继承了指定类
             * 并且排除父类自己
             */
            Iterator<Class<?>> iterator = classesAll.iterator();
            while (iterator.hasNext()) {
                Class<?> cls = iterator.next();
                /**
                 * isAssignableFrom该方法的解析，请参考博客：
                 * http://blog.csdn.net/u010156024/article/details/44875195
                 */
                if (clazz.isAssignableFrom(cls)) {
                    if (!clazz.equals(cls)) {
                        classes.add(cls);
                    } else {

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("出现异常");
        }
        return classes;
    }

    /**
     * 这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常在程序中很难定位某个相对路径，特别是在B/S应用中。
     * 通过这个方法，我们可以根据我们程序自身的类文件的位置来定位某个相对路径。
     * 比如：某个txt文件相对于程序的Test类文件的路径是../../resource/test.txt，
     * 那么使用本方法Path.getFullPathRelateClass("../../resource/test.txt",Test.class)
     * 得到的结果是txt文件的在系统中的绝对路径。
     *
     * @param relatedPath 相对路径
     * @param cls 用来定位的类
     * @return 相对路径所对应的绝对路径
     * @throws IOException 因为本方法将查询文件系统，所以可能抛出IO异常
     */
    public static final String getFullPathRelateClass(String relatedPath, Class<?> cls) {
        String path = null;
        if (relatedPath == null) {
            throw new NullPointerException();
        }
        String clsPath = getPathFromClass(cls);
        File clsFile = new File(clsPath);
        String tempPath = clsFile.getParent() + File.separator + relatedPath;
        File file = new File(tempPath);
        try {
            path = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 获取class文件所在绝对路径
     *
     * @param cls
     * @return
     * @throws IOException
     */
    public static final String getPathFromClass(Class<?> cls) {
        String path = null;
        if (cls == null) {
            throw new NullPointerException();
        }
        URL url = getClassLocationURL(cls);
        if (url != null) {
            path = url.getPath();
            if ("jar".equalsIgnoreCase(url.getProtocol())) {
                try {
                    path = new URL(path).getPath();
                } catch (MalformedURLException e) {
                }
                int location = path.indexOf("!/");
                if (location != -1) {
                    path = path.substring(0, location);
                }
            }
            File file = new File(path);
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 获取类的class文件位置的URL
     *
     * @param cls
     * @return
     */
    private static URL getClassLocationURL(final Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("null input: cls");
        }
        URL result = null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        final ProtectionDomain pd = cls.getProtectionDomain();
        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            if (cs != null)
                result = cs.getLocation();
            if (result != null) {
                if ("file".equals(result.getProtocol())) {
                    try {
                        if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip")) {
                            result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
                        } else if (new File(result.getFile()).isDirectory()) {
                            result = new URL(result, clsAsResource);
                        }
                    } catch (MalformedURLException ignore) {
                    }
                }
            }
        }
        if (result == null) {
            final ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ? clsLoader.getResource(clsAsResource)
                    : ClassLoader.getSystemResource(clsAsResource);
        }
        return result;
    }

    /**
     * 根据类名获取类信息
     * @param clazz
     * @return
     */
    public static final Class<?> getClass(String clazz) {
        /**
         * Use the Thread context classloader if possible
         */
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            if (loader != null) {
                return Class.forName(clazz, true, loader);
            }
            /**
             * Thread context classloader isn't working out, so use system
             * loader.
             */
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new DataParseException(e);
        }
    }

    /**
     * 获取两个对象之间的差异值
     * @param oldBean
     * @param newBean
     * @return
     */
    public static <T> T getDiff(T oldBean, T newBean) {
        if (oldBean == null && newBean != null) {
            return newBean;
        } else if (newBean == null) {
            return null;
        } else {
            Class<?> cls1 = oldBean.getClass();
            try {
                @SuppressWarnings("unchecked")
                T object = (T) cls1.newInstance();
                BeanInfo beanInfo = Introspector.getBeanInfo(cls1);
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor property : propertyDescriptors) {
                    String key = property.getName();
                    // 过滤class属性
                    if (!key.equals("class")) {
                        // 得到property对应的getter方法
                        Method getter = property.getReadMethod();
                        // 得到property对应的setter方法
                        Method setter = property.getWriteMethod();
                        Object oldValue = getter.invoke(oldBean);
                        Object newValue = getter.invoke(newBean);

                        List<String> specialTypes = Arrays.asList("int","long","boolean","float","double","short");
                        if (newValue != null) {
                            if (oldValue == null) {
                                setter.invoke(object, newValue);
                            } else if (oldValue != null && !newValue.equals(oldValue)) {
                                setter.invoke(object, newValue);
                            } else if (specialTypes.contains(property.getPropertyType().getName())) {
                                setter.invoke(object, newValue);
                            }
                        }
                    }
                }
                return object;
            } catch (Exception e) {
                throw new DataParseException(e);
            }
        }
    }
}
