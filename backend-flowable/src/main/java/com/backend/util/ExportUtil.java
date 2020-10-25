package com.backend.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportUtil {

    private static final String FTL_FP = "ftl-template";

    private static Configuration configuration = init();
    private static final String encoding = "UTF-8";
    private static String exportPath = "E:\\temp\\";

    /**
     * 构造函数
     * 配置模板路径
     *
     * @param
     */
    public static Configuration init() {
        Configuration configuration = new Configuration(new Version("2.3.30"));
        configuration.setDefaultEncoding(encoding);
        configuration.setClassForTemplateLoading(ExportUtil.class, "/ftl-template");
        return configuration;
    }

    /**
     * 获取模板
     *
     * @param name
     * @return
     * @throws Exception
     */
    public static Template getTemplate(String name) throws Exception {
        return configuration.getTemplate(name);
    }

    /**
     * 导出word文档到指定目录
     *
     * @param fileName
     * @param tplName
     * @param data
     * @throws Exception
     */
    public static void exportDocFile(String fileName, String tplName, Map<String, Object> data) throws Exception {
        log.debug("导出word到D:\test");
        //如果目录不存在，则创建目录
        File exportDirs = new File(exportPath);
        if (!exportDirs.exists()) {
            exportDirs.mkdirs();
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportPath + fileName), encoding));
        getTemplate(tplName).process(data, writer);
    }

    /**
     * 导出word文档到客户端
     *
     * @param response
     * @param fileName
     * @param tplName
     * @param data
     * @throws Exception
     */
    public static void exportDoc(HttpServletResponse response, String fileName, String tplName, Map<String, Object> data) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msword");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        // 把本地文件发送给客户端
        Writer out = response.getWriter();
        Template template = getTemplate(tplName);
        template.process(data, out);
        out.close();
    }

}
