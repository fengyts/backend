package com.backend.controller;

import com.backend.common.BaseController;
import com.backend.util.ExportUtil;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.entity.ContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileController extends BaseController {

    private static final String DEFAULT_ENCODING = "UTF-8";

    @PostMapping("/upload")
    public void uplodadFile(HttpServletRequest request, MultipartFile multipartFile) {

    }

    //https://blog.csdn.net/user2025/article/details/107300831
    //https://blog.csdn.net/ai_0922/article/details/82773466
    @GetMapping("download")
    public void downloadFile(HttpServletResponse response) {
        try (
                StringWriter writer = new StringWriter();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ServletOutputStream outputStream = response.getOutputStream();
        ) {
            //创建配置实例
            Configuration configuration = new Configuration(new Version("2.3.30"));
            //设置编码
            configuration.setDefaultEncoding("UTF-8");
            //ftl模板文件
            String templateDirPath = "template";
            Resource resource = new ClassPathResource(templateDirPath);
            File templateDir = resource.getFile();
            configuration.setDirectoryForTemplateLoading(templateDir);
//            configuration.setClassForTemplateLoading(this.getClass(), templateDirPath);

            //获取模板
            String templateName = "template.ftl";
            Template template = configuration.getTemplate(templateName, DEFAULT_ENCODING);
            //将模板和数据模型合并生成文件

            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("userName", "zhangsan");
            dataMap.put("age", 28);
            dataMap.put("birthday", "1999-10-01");
            dataMap.put("birthAddress", "上海");
            dataMap.put("email", "test@163.com");

            //生成文件
            template.process(dataMap, writer);

            ByteArrayInputStream bis = new ByteArrayInputStream(writer.toString().getBytes(DEFAULT_ENCODING));

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] bytes = baos.toByteArray();
            writer.flush();

            String fileName = URLEncoder.encode("下载测试.doc", DEFAULT_ENCODING);
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("download1")
    public void downloadFile1(HttpServletResponse response) {
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("userName", "zhangsan");
        dataMap.put("age", 28);
        dataMap.put("birthday", "1999-10-01");
        dataMap.put("birthAddress", "上海");
        dataMap.put("email", "test@163.com");
        try {
            ExportUtil.exportDoc(response,"下载测试.doc", "template.ftl", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
