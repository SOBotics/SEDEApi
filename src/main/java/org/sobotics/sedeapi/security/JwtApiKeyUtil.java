package org.sobotics.sedeapi.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/**
 * JWT key utility, to get user and password to login to SE from api key
 * 
 * @author Petter Friberg
 *
 */
public class JwtApiKeyUtil implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;
	
	public static final String TEST_SECRET = "TEST";

	public static final String CLAIM_KEY_EMAIL = "email";
	public static final String CLAIM_KEY_PWD = "pwd";
	public static final String CLAIM_KEY_CREATED = "created";

	private String secret;

	
	public JwtApiKeyUtil(String secret){
		this.secret = secret;
	}
	

	public Date getCreatedDateFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		return new Date((Long) claims.get(CLAIM_KEY_CREATED));
	}

	
	public String getEmailFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		return (String) claims.get(CLAIM_KEY_EMAIL);
	
	}
	
	public String getPwdFromToken(String token) {
		final Claims claims = getClaimsFromToken(token);
		return (String) claims.get(CLAIM_KEY_PWD);
	
	}


	public Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
	}

	public String generateToken(String email, String pwd) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_CREATED, new Date());
		claims.put(CLAIM_KEY_EMAIL, email);
		claims.put(CLAIM_KEY_PWD, pwd);
		return generateToken(claims);
	}

	
	public String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, this.secret).compact();
	}

	
	public static void main(String[] args) {
		System.out.println(new JwtApiKeyUtil(TEST_SECRET).generateToken("email", "password"));
	}
}