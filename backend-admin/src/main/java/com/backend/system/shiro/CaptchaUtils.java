package com.backend.system.shiro;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * 验证码工具类
 * 
 */
public class CaptchaUtils {
	
	/*
	 * 验证码字体
	 * 如果系统不存在请安装字体
	 */
	public static final String FONT_FAMILY = "Algerian";
	
	/*
	 * 默认验证码长度
	 */
	public static final int DEFAULT_LEN = 4;
	
	/*
	 * 默认验证码字符源
	 * 阿拉伯数字及大写英文字符（不含0、1、I、O等易混淆的字符）
	 */
    public static final String DEFALUT_SOURCE = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    
    private static Random random = new Random();

	/*
	 * 默认验证码字符源
	 * 阿拉伯数字及大写英文字符（不含0、1、I、O等易混淆的字符）
	 */
    public static final String SESSION_KEY = "captchaSessionKey";
    
    public static void generateSessionCaptcha(HttpServletRequest request, 
    		HttpServletResponse response) throws IOException {
        //生成随机字串
        String captcha = CaptchaUtils.generateCaptcha();
        
        //存入会话session
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_KEY, captcha.toUpperCase());
        
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpg");
        outputImage(120, 40, response.getOutputStream(), captcha);
    }
    
    public static boolean verifySessionCaptcha(HttpServletRequest request, String captcha) {
    	if (captcha == null || captcha.trim() == "") {
        	return false;
    	}
    	
    	HttpSession session = request.getSession(true);
        String sCaptcha = (String)session.getAttribute(SESSION_KEY);
        if (sCaptcha == null) {
        	return false;
    	}
    	
    	return captcha.trim().toUpperCase().equals(sCaptcha);
    }
    
    public static boolean verifySessionCaptcha(HttpSession session, String captcha) {
    	if (captcha == null || captcha.trim() == "") {
        	return false;
    	}
    	
        String sCaptcha = (String)session.getAttribute(SESSION_KEY);
        if (sCaptcha == null) {
        	return false;
    	}
    	
    	return captcha.trim().toUpperCase().equals(sCaptcha);
    }
    
    /** 
     * 使用默认字符源生成默认长度验证码  
     *
     * @return
     */
    public static String generateCaptcha() {
        return generateCaptcha(DEFAULT_LEN, DEFALUT_SOURCE);
    }
   
    /** 
     * 使用默认字符源生成指定长度验证码
     * @param len 验证码长度
     * @return
     */  
    public static String generateCaptcha(int len) {
        return generateCaptcha(len, DEFALUT_SOURCE);
    }
    
    /** 
     * 使用指定字符源生成指定长度验证码
     * @param len 验证码长度
     * @param source 验证码字符源
     * @return
     */  
    public static String generateCaptcha(int len, String source) {
        if(source == null || source.trim().length() == 0) {
            source = DEFALUT_SOURCE;
        }
        int sourceLen = source.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder captcha = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
        	captcha.append(source.charAt(rand.nextInt(sourceLen - 1)));
        }
        return captcha.toString();
    }
    
    /** 
     * 输出默认长度随机验证码图片
     * @param w
     * @param h
     * @param of
     * @return
     * @throws IOException
     */  
    public static String outputCaptchaImage(int w, int h, File of) throws IOException {
        String captcha = generateCaptcha(); 
        outputImage(w, h, of, captcha);
        return captcha;
    }
      
    /** 
     * 输出指定长度随机验证码图片
     * @param w
     * @param h
     * @param of
     * @param len
     * @return
     * @throws IOException
     */  
    public static String outputCaptchaImage(int w, int h, File of, int len) throws IOException {
        String captcha = generateCaptcha(len); 
        outputImage(w, h, of, captcha);
        return captcha;
    }
      
    /** 
     * 输出指定长度随机验证码图片
     * @param w
     * @param h
     * @param os
     * @param len
     * @return 
     * @throws IOException
     */  
    public static String outputCaptchaImage(int w, int h, OutputStream os, int len) throws IOException {
        String captcha = generateCaptcha(len);
        outputImage(w, h, os, captcha);
        return captcha;
    }  
      
    /** 
     * 输出指定验证码图片
     * @param w
     * @param h
     * @param of
     * @param captcha
     * @throws IOException
     */  
    public static void outputImage(int w, int h, File of, String captcha) throws IOException {
        if (of == null) {
            return;
        }
        File dir = of.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs(); 
        }
        FileOutputStream fos = null;
        try { 
            of.createNewFile();
            fos = new FileOutputStream(of);
            outputImage(w, h, fos, captcha);
        } catch (IOException e) {
            throw e;
        } finally {
        	if (fos != null) {
        		fos.close();
        	}
		}
    }
      
    /** 
     * 输出指定验证码图片流
     * @param w
     * @param h
     * @param os
     * @param captcha
     * @throws IOException
     */  
    public static void outputImage(int w, int h, OutputStream os, String captcha) throws IOException {
    	int len = captcha.length();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN, 
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, 
                Color.PINK, Color.YELLOW };
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)]; 
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);
        
        g2.setColor(Color.GRAY); // 设置边框色
        g2.fillRect(0, 0, w, h); 
        
        Color c = getRandColor(200, 250);
        g2.setColor(c); // 设置背景色
        g2.fillRect(0, 2, w, h-4);
        
        // 绘制干扰线
        Random random = new Random();
        g2.setColor(getRandColor(160, 200)); // 设置线条的颜色
        for (int i = 0; i < 20; i++) {
        	int x = random.nextInt(w - 1);
            int y = random.nextInt(h - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }  
        
        // 添加噪点
        float yawpRate = 0.05f; // 噪声率
        int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }  
        
        shear(g2, w, h, c); // 使图片扭曲
        
        g2.setColor(getRandColor(100, 160));
        int fontSize = h - 4;
        Font font = new Font(FONT_FAMILY, Font.ITALIC, fontSize); 
        g2.setFont(font);
        char[] chars = captcha.toCharArray();
        for (int i = 0; i < len; i++) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / len) * i + fontSize/2, h/2); 
            g2.setTransform(affine);  
            g2.drawChars(chars, i, 1, ((w-10) / len) * i + 5, h/2 + fontSize/2 - 10);
        }
        
        g2.dispose();
        ImageIO.write(image, "jpg", os);
    }
    
    private static Color getRandColor(int fc, int bc) { 
        if (fc > 255) {
        	fc = 255; 
        } 
        if (bc > 255) {
        	bc = 255;
        }
        int r = fc + random.nextInt(bc - fc); 
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
    
    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }  
        return color;
    }  
      
    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }  
  
    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }  
      
    private static void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2); 
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);
        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period 
                            + (6.2831853071795862D * (double) phase) 
                            / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
    }
  
    private static void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(40) + 10;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1) 
                    * Math.sin((double) i / (double) period 
                            + (6.2831853071795862D * (double) phase) 
                            / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
    	System.out.println(">>>>>>生成验证码 开始<<<<<<");
        File dir = new File("E:/temp/Captchas");
        int w = 200, h = 80;
        for(int i = 0; i < 100; i ++) {
            String captcha = generateCaptcha();
            File file = new File(dir, captcha + ".jpg");
            outputImage(w, h, file, captcha);
            System.out.println(captcha);
        }

    	System.out.println(">>>>>>生成验证码 结束<<<<<<");
    }  
    
   
}