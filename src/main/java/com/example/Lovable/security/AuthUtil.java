package com.example.Lovable.security;

import com.example.Lovable.dto.auth.JwtUserPrincipal;
import com.example.Lovable.entity.User;
import com.example.Lovable.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@Component
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));

    }


    public String generateAccessToken(User user){
        return Jwts
                .builder()
                .subject(user.getUsername())
                .claim("userId",user.getId().toString())
                .claim("roles", Set.of("ADMIN","USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }

    public JwtUserPrincipal verifyAccessToken(String token)
    {
        Claims claims=Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

      Long userId= Long.parseLong(claims.get("userId", String.class));
      String username=claims.getSubject();

      return new JwtUserPrincipal(userId,username,new ArrayList<>());


    }

    public Long getCurrentUserId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !(authentication.getPrincipal() instanceof JwtUserPrincipal))
            throw new AuthenticationCredentialsNotFoundException("No JWT Fount User Not Authenticated");

        return ((JwtUserPrincipal) authentication.getPrincipal()).getUserId();
    }
}
