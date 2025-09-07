package com.patria.apps.helper;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.BlackListToken;
import com.patria.apps.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.repository.BlackListTokenRepository;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import com.patria.apps.repository.UsersRepository;

@Service
public class JWTHelperService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.audience}")
    private String jwtAudience;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    @Autowired
    private AESHelperService aesService;

    public String generateToken(Long userId, Long expired) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            String token = Jwts.builder()
                    .setIssuer(jwtIssuer)
                    .setIssuedAt(getDate(LocalDate.now()))
                    .claim("user_id", aesService.encrypt(userId.toString()))
                    .setAudience(jwtAudience)
                    .setExpiration(getDate(LocalDate.now().plusDays(expired)))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

            blackListTokenRepository.deleteByToken(token);

            return token;
        } catch (Exception ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generate token", ex);
        }
    }

    public Optional<Users> getUserByToken(String token, HttpServletRequest request) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String path = request.getRequestURI();

        if (!path.contains("/v1/api/auth/")) {
            boolean checkBlackListToken = blackListTokenRepository.findByTokenAndDeletedAtIsNull(token.trim()).isPresent();
            if (checkBlackListToken) {
                return null;
            }
        }

        Claims claims = null;

        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            return null;
        }
        Long userId = aesService.getDecryptedString(String.valueOf(claims.get("user_id")));
        return usersRepository.findById(userId);
    }

    public void killToken(String token) {
        Optional<BlackListToken> checkToken = blackListTokenRepository.findByTokenAndDeletedAtIsNull(token.trim());
        if (checkToken.isEmpty()) {
            BlackListToken dataToken = new BlackListToken();
            Users createdBy = SecurityHelperService.getPrincipal();
            dataToken.setToken(token.trim());
            dataToken.setCreatedBy(createdBy.getId());
            dataToken.setCreatedAt(LocalDateTime.now());
            blackListTokenRepository.save(dataToken);
        }
    }

    private Date getDate(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }
}
