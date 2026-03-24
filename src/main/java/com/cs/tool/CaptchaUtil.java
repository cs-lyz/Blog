package com.cs.tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class CaptchaUtil {

    // 验证码字符集（去掉容易混淆的 0、O、1、I）
    private static final String CHAR_SET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int LENGTH = 4;

    /**
     * 生成验证码图片和文本
     * @return 包含验证码文本和图片字节数组的对象
     */
    public static CaptchaInfo generateCaptcha() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        // 背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // 边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // 随机字符
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int x = 15, y = 28;
        for (int i = 0; i < LENGTH; i++) {
            char c = CHAR_SET.charAt(random.nextInt(CHAR_SET.length()));
            sb.append(c);
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString(String.valueOf(c), x + i * 25, y);
        }

        // 干扰线
        for (int i = 0; i < 10; i++) {
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();

        // 将图片转为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", baos);
        } catch (IOException e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }

        return new CaptchaInfo(sb.toString(), baos.toByteArray());
    }

    // 封装验证码文本和图片字节数组
    public static class CaptchaInfo {
        private final String text;
        private final byte[] imageBytes;

        public CaptchaInfo(String text, byte[] imageBytes) {
            this.text = text;
            this.imageBytes = imageBytes;
        }

        public String getText() {
            return text;
        }

        public byte[] getImageBytes() {
            return imageBytes;
        }
    }
}