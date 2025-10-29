package com.app.toeic.util;


import com.app.toeic.user.model.CaptchaProperty;
import jj.play.ns.nl.captcha.Captcha;
import jj.play.ns.nl.captcha.backgrounds.GradiatedBackgroundProducer;
import jj.play.ns.nl.captcha.noise.CurvedLineNoiseProducer;
import jj.play.ns.nl.captcha.noise.StraightLineNoiseProducer;
import jj.play.ns.nl.captcha.text.producer.DefaultTextProducer;
import jj.play.ns.nl.captcha.text.renderer.DefaultWordRenderer;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Log
@UtilityClass
public class CaptchaGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final List<Font> DEFAULT_FONTS = new ArrayList<>();
    private static final int DEFAULT_LENGTH = 4;
    private static final char[] DEFAULT_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    static {
        var localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        var availableFontFamilyNames = localGraphicsEnvironment.getAvailableFontFamilyNames(LocaleContextHolder.getLocale());
        for (var fontName : availableFontFamilyNames) {
            DEFAULT_FONTS.add(new Font(fontName, Font.BOLD, 40));
        }
    }

    public static CaptchaProperty getCaptchaProperty() {
        var color = new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255));
        var captcha = new Captcha.Builder(120, 40)
                .addText(new DefaultTextProducer(DEFAULT_LENGTH, DEFAULT_CHARS), new DefaultWordRenderer(color, DEFAULT_FONTS))
                .addBackground(new GradiatedBackgroundProducer())
                .addNoise(new CurvedLineNoiseProducer(color, 2))
                .addNoise(new StraightLineNoiseProducer(color, 2))
                .addBorder()
                .build();
        try {
            var byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(captcha.getImage(), "jpeg", byteArrayOutputStream);
            return new CaptchaProperty(captcha.getAnswer(), byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            log.log(Level.WARNING, "CaptchaGenerator >> getCaptchaProperty >> Exception: ", e);
            return new CaptchaProperty(StringUtils.EMPTY, new byte[0]);
        }
    }

}
