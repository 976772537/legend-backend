package com.drp.common.utils;

import com.drp.common.bean.User;
import com.drp.common.exception.MyAccessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  16:08:55
 */
public final class JwtUtils {
    private static final  String SECRET = "noTalkSecret";
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final long EXPIRATION_TIME = 640_800_000_000L; //7 day ( count by millisecond )
    private static final String TOKEN_PREFIX = "Bearer";

    public static String generateJwtToken(String username) {
        //noBodyRequest claims
        final HashMap<String, Object> claims = new HashMap<String, Object>(){{
            put(CLAIM_KEY_USERNAME, username);
            put(CLAIM_KEY_CREATED, System.currentTimeMillis());
        }};

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static boolean validateToken(User user) {
        final String token = user.getToken();
        final String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    public static boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private static Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

    private static Date getExpirationDateFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public static String getUsernameFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();//sub
    }

    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsernameFromHeader(String authHeader) {
        String token = getTokenFromHeader(authHeader);
        return getUsernameFromToken(token);
    }

    public static String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(TOKEN_PREFIX.length());
        }
        throw new MyAccessException("didn't catch the token");
    }
}
