package security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JWTGenerator {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private long jwtExpiration;

	private final Logger logger = LogManager.getLogger(JWTGenerator.class);

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + TimeUnit.SECONDS.toMillis(jwtExpiration));

		return Jwts.builder()
				.setSubject(username)
				.claim("authorities", authentication.getAuthorities())
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256, jwtSecret)
				.compact();
	}

	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			logger.error("JWT was expired or incorrect.");
			throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect.");
		}
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
}
