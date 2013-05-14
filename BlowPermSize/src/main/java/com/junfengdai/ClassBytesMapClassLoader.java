package com.junfengdai;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class ClassBytesMapClassLoader extends ClassLoader {
    private final Map<String, byte[]> classes = newHashMap();

    public void addClassBytes(String name, byte[] bytes){
        this.classes.put(name, bytes);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = classes.get(name);
        if(classBytes == null) {
            throw new ClassNotFoundException(name);
        }

        Class<?> classObject = defineClass(name, classBytes, 0, classBytes.length);
        if (classObject == null) {
            throw new ClassNotFoundException(name);
        }
        return classObject;
    }
}
