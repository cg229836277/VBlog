package org.sang.ratelimit;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Configuration
@Slf4j
public class LimitInterceptor {

    private static final String UNKNOWN = "unknown";

    private final RedisTemplate<String, Serializable> limitRedisTemplate;

    private static final Long SUCCESS_RESULT = 1L;

    @Autowired
    public LimitInterceptor(RedisTemplate<String, Serializable> limitRedisTemplate) {
        this.limitRedisTemplate = limitRedisTemplate;
    }

    /**
     * @param pjp controller拦截器
     */
    @Around("execution(public * *(..)) && @annotation(Limit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        String key;
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();

        /**
         * 根据限流类型获取不同的key ,如果不传我们会以方法名作为key
         */
        switch (limitType) {
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }

        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix(), key));
        try {
            String luaScript = buildLuaScript1();
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
            Long result = limitRedisTemplate.execute(redisScript, keys, limitPeriod, limitCount);
            log.info("Access try result is {} for name={} and key = {}", result, name, key);
            if (Objects.equals(result, SUCCESS_RESULT)) {
                return pjp.proceed();
            } else {
                throw new RuntimeException("You have been dragged into the blacklist");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            if (e instanceof RuntimeException) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
            throw new RuntimeException("server exception");
        }
    }

    /**
     * @description 编写 redis Lua 限流脚本
     */
    public String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        // 调用不超过最大值，则直接返回
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c");
        lua.append("\nend");
        // 执行计算器自加
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c");
        return lua.toString();
    }

    /**
     * @description 编写 redis Lua 限流脚本
     */
    public String buildLuaScript1() {
        StringBuilder lua = new StringBuilder();
        lua.append("local times = redis.call('incr',KEYS[1])");
        lua.append("\nif times == 1 then");
        // 调用不超过最大值，则直接返回
        lua.append("\n\tredis.call('expire',KEYS[1], ARGV[1])");
        lua.append("\nend\n");
        // 执行计算器自加
        lua.append("\nif times > tonumber(ARGV[2]) then");
        lua.append("\n\treturn 0");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nend");
        lua.append("\nreturn 1");
        return lua.toString();
    }


    /**
     * @description 获取id地址
     */
    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
