package com.junfengdai;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;

public class Foo {
    public static void main(String[] args) {
        List<String> values = newArrayList();
        values.add("hello");
        values.add("world");

        Stream<String> result = values.stream().filter(v -> v.isEmpty());
        Optional<String> first = result.findFirst();

    }
}
