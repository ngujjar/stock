//package com.trade.security;
//
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import io.jsonwebtoken.*;
//
//import java.security.Key;
//import java.security.Signature;
//import java.util.Date;
//
//import static org.yaml.snakeyaml.tokens.Token.ID.Key;
//
//@Component
//public class JwtUtil {
//    private final String SECRET_KEY = "jW9Kx3tReVn8tKZ4nLB2br3qfXk91uEoYhW2K7c9yD0=";
//    private static final long EXPIRATION_TIME = 1000*60*60;
//    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//
//
//    public String generateToken(String username){
//                return Jwts.builder()
//                        .setSubject(username)
//                        .setIssuedAt(new Date())
//                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                        .signWith(key, SignatureAlgorithm.HS256)
//                        .compact();
//    }
//     public String extractUsername(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//     }
//
//     public boolean isTokenValid(String token, UserDetails userDetails){
//        try{
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        }catch (JwtException e){
//            return false;
//        }
//     }
//
//}


package com.trade.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "jW9Kx3tReVn8tKZ4nLB2br3qfXk91uEoYhW2K7c9yD0=";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
