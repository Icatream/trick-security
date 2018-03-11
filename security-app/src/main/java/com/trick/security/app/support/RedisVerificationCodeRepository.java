package com.trick.security.app.support;

import com.trick.security.core.varification.code.VerificationCodeException;
import com.trick.security.core.varification.code.VerificationCodeRepository;
import com.trick.security.core.varification.code.VerificationCodeType;
import com.trick.security.core.varification.code.pojo.VerificationCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

@Component
public class RedisVerificationCodeRepository implements VerificationCodeRepository {

    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public RedisVerificationCodeRepository(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(ServletWebRequest request, VerificationCodeType type, VerificationCode verificationCode) {
        redisTemplate.opsForValue().set(createKey(request, type), verificationCode, 10, TimeUnit.MINUTES);
    }

    @Override
    public VerificationCode get(ServletWebRequest request, VerificationCodeType type) {
        return (VerificationCode) redisTemplate.opsForValue().get(createKey(request, type));
    }

    @Override
    public void remove(ServletWebRequest request, VerificationCodeType type) {
        redisTemplate.delete(createKey(request, type));
    }

    private String createKey(ServletWebRequest request, VerificationCodeType type) {
        String deviceId = request.getHeader("device-id");
        if (StringUtils.isBlank(deviceId)) {
            throw new VerificationCodeException("请在请求头中携带device-id参数");
        }
        return "code:" + type + ":" + deviceId;
    }
}
