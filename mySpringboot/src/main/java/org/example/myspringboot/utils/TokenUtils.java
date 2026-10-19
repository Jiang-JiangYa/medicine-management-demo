package org.example.myspringboot.utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.myspringboot.entity.User;
import org.example.myspringboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
@Component
public class TokenUtils {
    private static Algorithm algorithm;
    private static UserMapper mapper;
    @Resource private UserMapper userMapper;
    @Value("${app.jwt-secret}") private String secret;
    @PostConstruct public void init() {
        if (secret == null || secret.length() < 32) throw new IllegalStateException("JWT_SECRET must contain at least 32 characters");
        algorithm = Algorithm.HMAC256(secret); mapper = userMapper;
    }
    public static String createToken(String id, String ignored) {
        return JWT.create().withIssuer("medicine-demo").withAudience(id)
            .withExpiresAt(new Date(System.currentTimeMillis() + 7200000L)).sign(algorithm);
    }
    public static User verify(String token) {
        String id = JWT.require(algorithm).withIssuer("medicine-demo").build().verify(token).getAudience().get(0);
        return mapper.selectById(Integer.valueOf(id));
    }
    public static User getCurrentUser() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : (User) attrs.getRequest().getAttribute("currentUser");
    }
}
