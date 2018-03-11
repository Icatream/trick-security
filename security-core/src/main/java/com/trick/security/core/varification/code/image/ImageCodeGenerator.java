package com.trick.security.core.varification.code.image;

import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.verification.ImageCodeProperties;
import com.trick.security.core.varification.code.VerificationCodeGenerator;
import com.trick.security.core.varification.code.pojo.VerificationCode;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class ImageCodeGenerator implements VerificationCodeGenerator {

    private final ImageCodeProperties codeProperties;

    public ImageCodeGenerator(SecurityProperties securityProperties) {
        this.codeProperties = securityProperties.getCode().getImage();
    }

    @Override
    public void generateAndSend(ServletWebRequest request, VerificationCode verificationCode) throws IOException {
        BufferedImage image = generate(request, verificationCode.getCode());

        ImageIO.write(image, "JPEG", request.getResponse().getOutputStream());
    }

    private BufferedImage generate(ServletWebRequest request, String code) {
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", codeProperties.getWidth());
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", codeProperties.getHeight());
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandomColor(random, 200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandomColor(random, 160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        for (int i = 0; i < code.length(); i++) {
            String s = code.substring(i, i + 1);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(s, 13 * i + 6, 16);
        }
        g.dispose();
        return image;
    }

    private Color getRandomColor(Random random, int fc, int bc) {
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
}
