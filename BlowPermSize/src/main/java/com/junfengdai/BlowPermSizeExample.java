package com.junfengdai;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BlowPermSizeExample {
    public static void main(String[] args) {
        FakeClassLoader loader = new FakeClassLoader();
        List<Class> clazz = newArrayList();
        for (int i = 0; ; i++) {
            try {
                clazz.add(loader.loadClass("C" + i));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
