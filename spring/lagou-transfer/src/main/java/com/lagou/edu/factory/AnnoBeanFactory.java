/**
 * FileName: AnnoBeanFactory.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/5 15:50
 * Description:
 */
package com.lagou.edu.factory;

import com.lagou.edu.dao.annos.Autowired;
import com.lagou.edu.dao.annos.Service;
import com.lagou.edu.utils.ClassUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class AnnoBeanFactory {

    //所有的class key为类的全限定名
    private static final Map<String, Class> classMap = new HashMap<>();

    //对象实例bean
    private static final Map<String, Object> singletonObjects = new HashMap<>();

    private static final Map<String, String> aliasMap = new HashMap<>();

    //存储所有的service
    private static final Set<Class> serviceSet = new HashSet<>();

    static {

        InputStream inputStream = AnnoBeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.selectNodes("//component-scan");
            Element element = list.get(0);
            String basePackage = element.attributeValue("base-package");

            classMap.putAll(ClassUtils.getClass(basePackage));
            parseClassMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseClassMap() throws Exception {
        if (classMap.isEmpty()) {
            return;
        }

        //1.创建service 实例并加到map
        for (Map.Entry<String, Class> entry : classMap.entrySet()) {
            String className = entry.getKey();
            Class clazz = entry.getValue();
            if (clazz.isAnnotationPresent(Service.class)) {
                //如果是接口 获取实现类并实例化加到map
                if (clazz.isInterface()) {
                    getBeanOfType(clazz);
                } else {
                    //如果不是接口，直接实例化并加到map
                    put(clazz);
                }
                serviceSet.add(clazz);
            }
        }

        ProxyFactory proxyFactory = getBean(ProxyFactory.class);


        //3.遍历所有的service 服务，获取存在autowired注解的属性，并赋值
        for (Class clazz : serviceSet) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Object obj = getBeanOfType(clazz);
                    Object val = getBeanOfType(declaredField.getType());
                    declaredField.setAccessible(true);
                    declaredField.set(obj, ClassUtils.getProxyObj(proxyFactory, val));
                }
            }
        }

        //3.遍历所有单例池，生成代理对象
        for (Map.Entry<String, Object> entry : singletonObjects.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            Class clazz = obj.getClass();
            if (ClassUtils.needTransProxy(clazz)) {
                if (clazz.getInterfaces() != null && clazz.getInterfaces().length > 0) {
                    singletonObjects.put(key, proxyFactory.getJdkProxy(obj));
                } else {
                    singletonObjects.put(key, proxyFactory.getCglibProxy(obj));
                }
            }
        }
    }



    /**
     * Author:limn_xmj@163.com
     * Date:  2020/3/5 22:44
     * Description:根据接口类型获取所有实现类对象
     */

    private static <T> T getBeanOfType(Class<T> classType) throws Exception {
        if (!classType.isInterface()) {
            T value = (T) singletonObjects.get(classType.getName());
            if (value == null) {
                value = (T) singletonObjects.get(aliasMap.get(classType.getName()));
            }
            return value;
        }
        for (Map.Entry<String, Class> entry : classMap.entrySet()) {
            String implClassName = entry.getKey();
            Class implClass = entry.getValue();
            if (classType != implClass && classType.isAssignableFrom(implClass)) {

                String key = aliasMap.get(classType.getName());
                if (key == null || key.isBlank()) {
                    aliasMap.put(classType.getName(), implClassName);
                    put(implClass);
                }
                key = classType.getName();
                T value = (T) singletonObjects.get(key);
                if (value == null) {
                    key = aliasMap.get(key);
                    value = (T) singletonObjects.get(key);
                }
                return value;
            }
        }
        return null;
    }

    //实例化并加到内存map
    private static void put(Class clazz)throws Exception {
        Object instance = clazz.newInstance();
        String key = clazz.getName();
        if (clazz.isAnnotationPresent(Service.class)) {
            Service annoService = (Service) clazz.getAnnotation(Service.class);
            String annoValue = annoService.value();
            if (annoValue != null && !annoValue.isBlank()) {
                aliasMap.put(key, annoValue);
                key = annoValue;
            }

            if (singletonObjects.get(key) == null) {

                singletonObjects.put(key, instance);
            }
        }

    }

    public static Class getImplClass(Class clazz) {
        return classMap.get(aliasMap.get(clazz.getName()));
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return getBeanOfType(clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
