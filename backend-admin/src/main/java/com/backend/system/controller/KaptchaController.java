package com.backend.system.controller;

import com.backend.common.BaseController;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("/kaptcha")
public class KaptchaController extends BaseController {

    @Autowired
    private Producer captchaProducer;

    @RequestMapping("/render")
    public void kaptchaRender(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");
            //生成验证码
            String capText = captchaProducer.createText();
            //向客户端写出
            BufferedImage bi = captchaProducer.createImage(capText);
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            try {
                out.flush();
            } finally {
                out.close();
            }
        } catch (IOException e) {
            logger.info("kaptcha render exception: {}", e);
        }
    }

    @RequestMapping("/custom")
    @ResponseBody
    public void kaptchaRenderCustom(HttpServletRequest request, HttpServletResponse response) {
    }

}
