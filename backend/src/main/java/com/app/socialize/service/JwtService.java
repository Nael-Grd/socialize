package com.app.socialize.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
    private String secretKey;
	
	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000* 24 * 60 * 60))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Key getSignInKey() {
	    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
	    return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String extractEmail(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(getSignInKey()) // On donne notre clé secrète pour vérifier le sceau
	            .build()
	            .parseClaimsJws(token) // On lit le token
	            .getBody() // On récupère le contenu (le "décret")
	            .getSubject(); // On extrait le Sujet (l'email qu'on y avait caché)
	}
}

