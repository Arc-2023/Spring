package com.vueespring.shiro;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
@Data
@Component
@Slf4j
public class JwtUtils {
    private static String secret = "123456qwe";
    private static long expire = 60*60*48*1000;
    public static String generateToken(long id){
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire);
    return Jwts.builder()
            .setSubject(id+"")
            .setId(id+"")
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS256,secret)
            .compact();
    }
    public static Claims getClaimByToken(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("jwt wrong",e);
            return null;
        }
    }
    public static boolean CheckToken(Date expireTime){
        return expireTime.before(new Date());
    }
}
