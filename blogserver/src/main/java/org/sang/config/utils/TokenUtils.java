package org.sang.config.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.sang.dataobject.UserDO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class TokenUtils implements Serializable {

    /**
     * Token 有效时长
     */
    @Value("${token.expire}")
    private long tokenExpiration = 3600;

    @Value("${token.key}")
    private String tokenSignKey;

    private static final long serialVersionUID = -3L;

    private final static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 生成 Token 字符串 必须 setAudience 接收者 setExpiration 过期时间 role 用户角色
     *
     * @param userDO 用户信息
     * @return 生成的Token字符串 or null
     */
    public String createToken(UserDO userDO, String roleName) {
        try {
            log.info("tokenSignKey:" + tokenSignKey + ",tokenExpiration:" + tokenExpiration);
            // Token 的过期时间
            Date expirationDate = new Date(System.currentTimeMillis() + tokenExpiration * 1000);
            // 生成 Token
            String token = Jwts.builder()
                    // 设置 Token 签发者 可选
                    .setIssuer("vueblog")
                    // 根据用户名设置 Token 的接受者
                    .setAudience(userDO.getUsername())
                    // 设置过期时间
                    .setExpiration(expirationDate)
                    // 设置 Token 生成时间 可选
                    .setIssuedAt(new Date())
                    // 通过 claim 方法设置一个 key = role，value = userRole 的值
                    .claim("role", roleName)
                    .claim("password", userDO.getPassword())
                    // 设置加密密钥和加密算法，注意要用私钥加密且保证私钥不泄露
                    .signWith(key)
                    .compressWith(CompressionCodecs.GZIP)
                    .compact();
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证 Token ，并获取到用户名和用户权限信息
     *
     * @param token Token 字符串
     * @return sysUser 用户信息
     */
    public UserDO validationToken(String token) {
        try {
//            String user = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
            // 解密 Token，获取 Claims 主体
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            assert claims != null;
            // 验证 Token 有没有过期 过期时间
            Date expiration = claims.getExpiration();
            // 判断是否过期 过期时间要在当前日期之后
            if (!expiration.after(new Date())) {
                return null;
            }
            UserDO userDO = new UserDO();
            userDO.setUsername(claims.getAudience());
            userDO.setRoleName(claims.get("role").toString());
            userDO.setPassword(claims.get("password").toString());
            return userDO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
