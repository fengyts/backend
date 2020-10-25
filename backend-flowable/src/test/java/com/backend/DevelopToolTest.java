package com.backend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;

public class DevelopToolTest {

    public static void generate() {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            String tableFile = "/ftl-template/flowable_table.json";
            Resource resource = new ClassPathResource(tableFile, DevelopToolTest.class);
            File file = resource.getFile();
            System.out.println(file.getParent());
            br = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder data = new StringBuilder();
            while (null != (line = br.readLine())) {
                data.append(line);
            }
            JSONObject obj = JSONObject.parseObject(data.toString());
            JSONArray jsonArray = (JSONArray) obj.get("rows");
            System.out.println(jsonArray);
            List<FlowableTableInfo> list = jsonArray.toJavaList(FlowableTableInfo.class);

            // 写脚本
            String truncateScriptSql = file.getParent() + "/truncate_script_sql.sql";
            File outFile = new File(truncateScriptSql);
            if (outFile.exists()) {
                outFile.delete();
//                outFile.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(outFile));
            final String schema = "bk_admin_flowable";
            final String sqlPrefix = "TRUNCATE `";
            for (FlowableTableInfo ft : list) {
                String sql = sqlPrefix;
                String tbName = ft.getTables_in_bk_admin_flowable();
                if (tbName.startsWith("act_hi_") || tbName.startsWith("act_ru_")) {
                    sql += tbName;
                    sql += "`;";
                    bw.append(sql);
                    bw.newLine();
                }
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bw) {
                    bw.close();
                }
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }

    }

    @Data
    public static class FlowableTableInfo {
        private String Tables_in_bk_admin_flowable;
    }

    public static void main(String[] args) {
        generate();
    }

}
