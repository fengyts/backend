package com.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import lombok.Data;

public class JavaTest {

    @Data
    public static class Ac {
        private Long id;
        private String name;
        private Date dure;
    }

    public static void test() {
        // "DELETE FROM `erdp-system`.`sys_eldictionary` WHERE id='';";
        String deleteSqlPrefix = "DELETE FROM `erdp-system`.`sys_eldictionary` WHERE id='";
        String deleteSqlSuffix = "';";
        String deleteSql;

        String source = "C:\\Users\\DELL\\Desktop\\失效节点.txt";
        String writeTo = "C:\\Users\\DELL\\Desktop\\失效节点1.txt";
        try (
                BufferedReader br = new BufferedReader(new FileReader(new File(source)));
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(writeTo)));
        ) {
            String line = "";
            while (null != (line = br.readLine())) {
//                System.out.println(line);
                if (line.startsWith("INSERT INTO")) {
                    String[] split = line.split(" VALUES ");
                    String[] values = split[1].split(",");
//                    System.out.println(Arrays.toString(values));
                    String id = values[0].replaceAll("\\('", "").replaceAll("'", "");
                    System.out.println(id);
                    deleteSql = deleteSqlPrefix + id + deleteSqlSuffix;
                    bw.append(deleteSql);
                    bw.newLine();
                    bw.append(line);
                    bw.newLine();
                }
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        ('fa1e4899ac6e43839373aa0b8f3c1068'
//        ('fbfa47c3a3ea4542bbcaa20cca787d6f'
        test();
    }

}
