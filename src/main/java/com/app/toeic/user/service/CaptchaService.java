package com.app.toeic.user.service;


import com.app.toeic.util.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.text.MessageFormat;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CaptchaService {
    JedisPooled jedisPooled;

    public void saveCaptcha(String key, String value, long ttlInSeconds) {
        jedisPooled.setex(MessageFormat.format(Constant.URL_CACHE_CAPTCHA, key), ttlInSeconds, value);
    }

}
