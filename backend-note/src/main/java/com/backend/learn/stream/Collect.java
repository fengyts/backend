package com.backend.learn.stream;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Collect {

    private static void collect1() {
        //Stream 流
        Stream<String> stream = Stream.of("aaa", "bbb", "ccc", "bbb");
        //收集流中的数据到集合中
        //1.收集流中的数据到 list
        List<String> list = stream.collect(Collectors.toList());
        System.out.println(list);

        //2.收集流中的数据到 set
        Set<String> collect = stream.collect(Collectors.toSet());
        System.out.println(collect);

        //3.收集流中的数据(ArrayList)(不收集到list,set等集合中,而是)收集到指定的集合中
        ArrayList<String> arrayList = stream.collect(Collectors.toCollection(ArrayList::new));
        System.out.println(arrayList);

        //4.收集流中的数据到 HashSet
        HashSet<String> hashSet = stream.collect(Collectors.toCollection(HashSet::new));
        System.out.println(hashSet);

    }

    public static void collectTest() {
        List<Order> orders = Lists.newArrayList();

        Order order = new Order("1", "no1", new BigDecimal(8.8), new Date(), 1);
        orders.add(order);
        Order order1 = new Order("2", "no2", new BigDecimal(6.6), new Date(), 2);
        orders.add(order1);
        Order order2 = new Order("3", "no2", new BigDecimal(6.66), new Date(), 2);
        orders.add(order2);

//        Map<Integer, Order> maps = orders.stream()
//                .collect(Collectors.toMap(Order::getType, Function.identity()));

        // 拼接
        List<Character> str = Lists.newArrayList();
        for (char c = 'a'; c < 'f'; c++) {
            str.add(c);
        }
        String strs = str.stream().map(c -> c.toString()).collect(Collectors.joining(","));
        String strs1 = str.stream().map(c -> c.toString()).collect(Collectors.joining("prefix", ",", "suffix"));
        System.out.println(strs);
        System.out.println(strs1);

        System.out.println();
    }

    public static List<Student> createStream() {
        Stream<Student> studentStream = Stream.of(
                new Student("赵丽颖", 58, 95),
                new Student("杨颖", 56, 88),
                new Student("迪丽热巴", 56, 99),
                new Student("柳岩", 52, 77)
        );
        List<Student> list = studentStream.collect(Collectors.toList());
        return list;
    }

    public static void testJuHe() {
        Stream<Student> studentStream = Stream.of(
                new Student("赵丽颖", 58, 95),
                new Student("杨颖", 56, 88),
                new Student("迪丽热巴", 56, 99),
                new Student("柳岩", 52, 77)
        );
        List<Student> list = studentStream.collect(Collectors.toList());

        // min(max) 或者 (聚合)实现
        Student min = studentStream.min((s1, s2) -> s1.getAge() - s2.getAge()).get();
        Student min1 = studentStream.collect(Collectors.minBy((s1, s2) -> s1.getAge() - s2.getAge())).get();

        System.out.println();
    }

    public static void testGroupBy() {
        Stream<Student> studentStream = Stream.of(
                new Student("赵丽颖", 58, 95),
                new Student("杨颖", 56, 88),
                new Student("迪丽热巴", 56, 99),
                new Student("柳岩", 52, 77)
        );
        List<Student> list = studentStream.collect(Collectors.toList());


        //1.按照分数>=60 分为"及格"一组  <60 分为"不及格"一组
        Map<String, List<Student>> map = studentStream.collect(Collectors.groupingBy(s -> {
            if (s.getScore() >= 60) {
                return "及格";
            } else {
                return "不及格";
            }
        }));
        map.forEach((key, value) -> {
            System.out.println(key + "---->" + value);
        });


        System.out.println();

        // 多级分组操作
        //多级分组
        //1.先根据年龄分组,然后再根据成绩分组
        //分析:第一个Collectors.groupingBy() 使用的是(年龄+成绩)两个维度分组,所以使用两个参数 groupingBy()方法
        //    第二个Collectors.groupingBy() 就是用成绩分组,使用一个参数 groupingBy() 方法
        Map<Integer, Map<Integer, Map<String, List<Student>>>> map1 = studentStream.collect(
                Collectors.groupingBy(
                        str -> str.getAge(),
                        Collectors.groupingBy(str -> str.getScore(), Collectors.groupingBy((student) -> {
                                    if (student.getScore() >= 60) {
                                        return "及格";
                                    } else {
                                        return "不及格";
                                    }
                                })
                        )
                )
        );
        map1.forEach((key, value) -> {
            System.out.println("年龄:" + key);
            value.forEach((k2, v2) -> {
                System.out.println("\t" + v2);
            });
        });


        // 分区操作
        // 分区操作 和 分组操作 区别：分组可以有多个组。分区只会有两个区( true 和 false)
        Map<Boolean, List<Student>> partitionMap = studentStream.collect(Collectors.partitioningBy(s -> s.getScore() > 60));
        partitionMap.forEach((key, value) -> {
            System.out.println(key + "---->" + value);
        });
        partitionMap.forEach((key,value)->{
            System.out.println(key + "---->" + value);
        });


    }

    public static void main(String[] args) {
        collectTest();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order implements Serializable {
        private static final long serialVersionUID = -7301750392753896377L;

        private String id;
        private String orderNo;
        private BigDecimal amount;
        private Date orderDate;
        private Integer type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Student implements Serializable {
        private static final long serialVersionUID = -6423566777786058269L;

        private String name;
        private int age;
        private int score;
    }

}
