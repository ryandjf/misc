package com.junfengdai.asm;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BlowPermSizeWithAsmExample {
    private static final int MAX_CLASS_COUNT = 10000000;
    private static final String FAKE_CLASS_NAME = "MyFakeAsmClassName";

    public static void main(String[] args) {
        AsmClassByteClassLoader loader = new AsmClassByteClassLoader();
        List<Class> clazz = newArrayList();
        for (int i = 0; i < MAX_CLASS_COUNT; i++) {
            try {
                clazz.add(loader.loadClass(FAKE_CLASS_NAME + i));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
