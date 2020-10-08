package com.backend;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class JavaTest {

    public static void main(String[] args) {
        List<String> list1 = Lists.newArrayList();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        list1.add("d");
        list1.add("e");
        list1.add("f");
        list1.add("g");

        List<String> list2 = Lists.newArrayList();
        list2.add("a");
        list2.add("b");
        list2.add("c");
        list2.add("d");
        list2.add("e");
        list2.add("f");
        list2.add("g");
        list2.add("h");
        list2.add("i");

        // 交集
        List<String> intersection = list1.stream().filter(item -> list2.contains(item)).collect(Collectors.toList());
        // 差集: list1 -> list2
        List<String> reduce1 = list1.stream().filter(m -> !list2.contains(m)).collect(Collectors.toList());
        // 差集: list2 -> list1
        List<String> reduce2 = list2.stream().filter(m -> !list1.contains(m)).collect(Collectors.toList());
        List<String> distinct = list1.stream().distinct().collect(Collectors.toList());

        System.out.println(intersection); // [f, g]
        System.out.println(reduce1);      // [a, b, c, d, e]
        System.out.println(reduce2);      // [h, i]
        System.out.println(distinct);
    }
}
