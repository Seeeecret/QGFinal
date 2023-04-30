package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

/**
 * jwt工具类
 *
 * @author Secret
 * @date 2023/04/26
 */
public class JwtUtil {
    /**
     * 密钥
     */
    private static final Key SECRET_KEY;
    private static final long EXPIRATION;
    static {
        Properties props = new Properties();
        try (InputStream is = JwtUtil.class.getClassLoader().getResourceAsStream("JwtUtil.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties");
        }

        // 从属性文件中获取密钥
        String secret = props.getProperty("SECRET_KEY");
        if (secret == null) {
            throw new RuntimeException("Property is not set");
        }

        // 使用 Base64 解码密钥
        byte[] secretBytes = Base64.getDecoder().decode(secret);
        SECRET_KEY = Keys.hmacShaKeyFor(secretBytes);
        EXPIRATION = Long.parseLong(props.getProperty("EXPIRATION"));
    }

    public static String generateToken(String id, String subject, int roleId) {
        // 获取当前时间
        long nowMillis = System.currentTimeMillis();
        // 创建 JWT 的签发时间
        Date now = new Date(nowMillis);
        Claims claims = Jwts.claims();
        claims.put("role", roleId);
        // 生成 JWT Token
        return Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Jws<Claims> parseToken(String token) {
        // 解析 JWT Token
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getToken(HttpServletRequest request) {
        // 从请求头中获取 Token
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static boolean validateToken(String token) {
        // 验证 Token 是否有效
        Jws<Claims> jws = parseToken(token);
        return jws != null && jws.getBody().getExpiration().after(new Date());
    }


    public static String getId(String token) {
        // 从 Token 中获取用户 ID
        Jws<Claims> jws = parseToken(token);
        if (jws != null) {
            return jws.getBody().getId();
        }
        return null;
    }

    public static String getSubject(String token) {
        // 从 Token 中获取用户 subject
        Jws<Claims> jws = parseToken(token);
        if (jws != null) {
            return jws.getBody().getSubject();
        }
        return null;
    }

    public static Integer getRoleId(String token){
        // 从 Token 中获取用户 roleId
        Jws<Claims> jws = parseToken(token);
        if (jws != null) {
            return jws.getBody().get("role", Integer.class);
        }
        return null;
    }
}
