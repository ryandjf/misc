package com.junfengdai.asm;

public class AsmClassByteClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassByteCodeGenerator generator = new ClassByteCodeGenerator();
        byte[] bytes = generator.generateClass(name);
        return defineClass(name, bytes, 0, bytes.length);
    }
}
