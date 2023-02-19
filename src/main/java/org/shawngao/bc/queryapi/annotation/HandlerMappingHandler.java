package org.shawngao.bc.queryapi.annotation;

import org.shawngao.bc.queryapi.QueryApi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HandlerMappingHandler {

    private Set<Class<?>> requestMappingClass;
    private Map<String, RequestObject> requestMappingMap;

    public Set<Class<?>> getClasses() {
        String targetPackageName = this.getClass()
                .getPackageName().replaceAll("annotation", "server");
        String targetPackagePath = targetPackageName.replaceAll("\\.", "/");
        Enumeration<URL> dirs;
        Set<Class<?>> classSet = new HashSet<>();
        try {
            List<JarEntry> jarEntryList = new ArrayList<>();
            dirs = QueryApi.instance.getClass().getClassLoader().getResources(targetPackagePath);
            URL url = dirs.nextElement();
            if ("jar".equals(url.getProtocol())) {
                JarFile jarFile;
                jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                while (jarEntryEnumeration.hasMoreElements()) {
                    JarEntry jarEntry = jarEntryEnumeration.nextElement();
                    String name = jarEntry.getName();
                    if (name.startsWith(targetPackagePath) && jarEntry.getName().endsWith(".class")) {
                        jarEntryList.add(jarEntry);
                    }
                }
                jarEntryList.forEach(v -> {
                    String className = v.getName().replace('/', '.');
                    className = className.substring(0, className.length() - 6);
                    try {
                        classSet.add(QueryApi.instance.getClass().getClassLoader().loadClass(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    private void getRequestMapping() {
        if (requestMappingClass == null) {
            requestMappingClass = new HashSet<>();
        }
        Set<Class<?>> classSet = getClasses();
        if (classSet.isEmpty()) {
            return;
        }
        classSet.forEach(v -> {
            if (v.getAnnotation(RequestMapping.class) != null) {
                requestMappingClass.add(v);
            }
        });
    }

    public void getRequestMappingMethod() {
        getRequestMapping();
        requestMappingMap = new HashMap<>();
        requestMappingClass.forEach(v -> {
            Method[] methods = v.getMethods();
            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    requestMappingMap.put(
                            v.getAnnotation(RequestMapping.class).value()
                                    + requestMapping.value(),
                            new RequestObject(v, method)
                    );
                }
            }
        });
    }

    public Map<String, RequestObject> getRequestMappingMap() {
        return requestMappingMap;
    }
}
