package com.junfengdai;

public class FakeClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        FakeClassByteCodeGenerator generator = new FakeClassByteCodeGenerator();
        byte[] bytes = generator.generateClass(name);
        return defineClass(name, bytes, 0, bytes.length);
    }
}
