package com.hcc.config.sercurity.component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenuntil {
    //荷载用户名key
    private static final String CLAIM_KEY_USERNAME = "sub";
    //创建时间
    private static final  String CLAIM_KEY_CREATED = "created";
    //密钥盐
    @Value("${jwt.secret}")
    private String secret;
    //失效时间
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户名生产token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){

        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());


        return generateToken(claims);
    }

    /**
     *
     * 根据荷载生成jwt Token
     * @param claims
     * @return
     */
    public String generateToken(Map<String, Object> claims){

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate()) //失效时间
                .signWith(SignatureAlgorithm.HS512,secret) //签名盐
                .compact();

    }

    /**
     * 生成token失效时间
     * @return
     */
    public Date generateExpirationDate() {
        // 失效时间是就当前系统时间加 有效时间
        return new Date(System.currentTimeMillis() + expiration*1000);
    }

    /**
     *  从token中获取登录的用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getUserClaimFromToken(token);
            username = claims.getSubject();
        }catch (Exception e) {
            e.printStackTrace();
            username = null;
        }

        return username;
    }

    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    public Claims getUserClaimFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }


    /**
     * 验证token是否有效  (token有效 + 用户一致)
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);   // 终归是来源于token的

        // 然后在token有效的前提下对比用户名是否一致
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }



    /**
     * 判断token是否以及失效
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDateFromToken(token);   // 拿到载荷里设置的有效时间
        return expireDate.before(new Date());          // 验证载荷里的时间是否以及失效(失效返回true)
    }



    /**
     * 获取token过期时间
     * @param token
     * @return
     */
    public Date getExpiredDateFromToken(String token) {
        Claims claims = getUserClaimFromToken(token);
        return claims.getExpiration();
    }



    /**
     * 判断token是否可以被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }


    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getUserClaimFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());

        return generateToken(claims);
    }




}
