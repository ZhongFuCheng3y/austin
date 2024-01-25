package com.java3y.austin.web.utils;

import com.java3y.austin.web.enums.ReturnCodeEnum;
import com.java3y.austin.web.exception.BusinessException;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {

    public static Claims decodeAndVerify(String token, String key) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            throw new BusinessException(ReturnCodeEnum.TOKEN_EXPIRED);
        } catch (Exception ex) {
            throw new BusinessException(ReturnCodeEnum.TOKEN_INVALID);
        }
    }

    public static String createToken(String subject, String key, long ttl) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ttl * 1000))
                .signWith(SignatureAlgorithm.HS256, key);
        return jwtBuilder.compact();
    }
}