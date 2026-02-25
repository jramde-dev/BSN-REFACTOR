package dev.jramde.book_network.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * This service will do anything related to the jwt token like:
 * <p>- generate the token</p>,
 * <p>- validate the token</p>,
 * <p>- decode the token and extract information from the token</p>
 */
// @Service
public class JrJwtService {

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    private final String SECRET_KEY;

    /**
     * Constructor. It will automatically generate secret key for authentication.
     *
     * @throws NoSuchAlgorithmException : exception if problem occurs
     */
    public JrJwtService() throws NoSuchAlgorithmException {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("hmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // 2
    public String generateBuiltToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration);
    }

    // 3
    private String buildToken(HashMap<String, Object> extraClaims, UserDetails userDetails, long jwtExpiration) {
        List<String> authorities = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities", authorities)
                .signWith(getSignInkey())
                .compact();
    }

    // 6
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 7
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 8
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 9
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 3'
    private Key getSignInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // 1
    public String generateToken(UserDetails userDetails) {
        return generateBuiltToken(new HashMap<>(), userDetails);
    }

    // ****** Methods used anywhere in the project *******

    /**
     * 4. Check if token is valid.
     *
     * @param token       : token we receive
     * @param userDetails : security user information
     * @return true or false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 5
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
