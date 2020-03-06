/**
 * FileName: ClassUtils.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/5 21:33
 * Description:
 */
package com.lagou.edu.utils;

import com.lagou.edu.dao.annos.Transactional;
import com.lagou.edu.factory.AnnoBeanFactory;
import com.lagou.edu.factory.ProxyFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassUtils {

    private static final String CLASS_SUFFIX = ".class";

    public static Map<String, Class> getClass(String basePackage) throws Exception {

        Map<String, Class> classMap = new HashMap<>();
        List<File> classFiles = getClassFiles(basePackage);
        for (File classFile : classFiles) {
            String filePath = classFile.getAbsolutePath();
            String className = filePath.substring(filePath.indexOf(basePackage.replace(".", "\\")), filePath.indexOf(CLASS_SUFFIX)).replace("\\", ".");
            System.out.println(className);
            Class clazz = Class.forName(className);
            classMap.put(className, clazz);
        }
        return classMap;
    }

    private static List<File> getClassFiles(String basePackage) throws IOException {
        List<File> result = new ArrayList<>();
        URL url = AnnoBeanFactory.class.getClassLoader().getResource("/" + basePackage.replace(".", "/"));
        result.addAll(getClassFiles(new File(url.getFile())));
        return result;
    }

    private static List<File> getClassFiles(File file) {
        List<File> result = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    result.addAll(getClassFiles(f));
                } else {
                    if (f.getAbsolutePath().endsWith(CLASS_SUFFIX)) {
                        result.add(f);
                    }
                }
            }
        }
        return result;
    }

    public static boolean needTransProxy(Class clazz) {
        return clazz.isAnnotationPresent(Transactional.class);
    }

    public static boolean methodNeedTrans(Method method) {
        return method.getDeclaringClass().isAnnotationPresent(Transactional.class)
                || AnnoBeanFactory.getImplClass(method.getDeclaringClass()).isAnnotationPresent(Transactional.class);
    }

    public static Object getProxyObj(ProxyFactory proxyFactory, Object obj) {
        Class clazz = obj.getClass();
        if (needTransProxy(clazz)) {
            if (clazz.getInterfaces() != null && clazz.getInterfaces().length > 0) {
                return proxyFactory.getJdkProxy(obj);
            } else {
                return proxyFactory.getCglibProxy(obj);
            }
        }
        return obj;
    }
}
