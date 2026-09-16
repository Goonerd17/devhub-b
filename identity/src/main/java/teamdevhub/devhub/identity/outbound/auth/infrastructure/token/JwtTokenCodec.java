package teamdevhub.devhub.identity.outbound.auth.infrastructure.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.token.vo.JwtClaims;
import teamdevhub.devhub.platform.outbound.common.exception.AuthRuleException;
import teamdevhub.devhub.identity.core.auth.port.out.token.vo.AccessTokenInfo;
import teamdevhub.devhub.identity.core.auth.port.out.token.vo.TempTokenInfo;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.platform.time.TimeProvider;
import teamdevhub.devhub.identity.core.auth.port.out.token.TokenIssueProvider;
import teamdevhub.devhub.identity.core.auth.port.out.token.TokenParseProvider;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenCodec implements TokenIssueProvider, TokenParseProvider {

    private final TimeProvider timeProvider;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    @Override
    public String createAccessToken(AuthenticatedUser authenticatedUser) {
        LocalDateTime now = timeProvider.now();
        LocalDateTime expireAt = now.plusMinutes(30);
        return Jwts.builder()
                .setSubject(authenticatedUser.userGuid())
                .claim(JwtClaims.TOKEN_TYPE, TokenType.ACCESS.name())
                .claim(JwtClaims.EMAIL, authenticatedUser.loginId())
                .claim(JwtClaims.USER_ROLE, authenticatedUser.userRole().name())
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expireAt))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    @Override
    public String createRefreshToken(String userGuid) {
        LocalDateTime now = timeProvider.now();
        LocalDateTime expireAt = now.plusDays(7);
        return Jwts.builder()
                        .setSubject(userGuid)
                        .claim(JwtClaims.TOKEN_TYPE, TokenType.REFRESH.name())
                        .setIssuedAt(toDate(now))
                        .setExpiration(toDate(expireAt))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    @Override
    public String createTempToken(String oauthId, VerificationProvider verificationProvider, String email) {
        LocalDateTime now = timeProvider.now();
        LocalDateTime expireAt = now.plusMinutes(3);
        return Jwts.builder()
                .setSubject(oauthId)
                .claim(JwtClaims.TOKEN_TYPE, TokenType.TEMP.name())
                .claim(JwtClaims.OAUTH_PROVIDER, verificationProvider.name())
                .claim(JwtClaims.EMAIL, email)
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expireAt))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    @Override
    public String removeBearer(String token) {
        if (!TokenPrefix.BEARER.matches(token)) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
        return TokenPrefix.BEARER.strip(token);
    }

    @Override
    public AccessTokenInfo getAccessTokenInfo(String accessToken) {
        Claims claims = parseClaims(accessToken);
        TokenType tokenType = extractTokenType(claims);

        if (tokenType != TokenType.ACCESS) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        return new AccessTokenInfo(
                claims.getSubject(),
                claims.get(JwtClaims.EMAIL, String.class),
                MemberRole.valueOf(claims.get(JwtClaims.USER_ROLE, String.class))
        );
    }

    @Override
    public TempTokenInfo getTempTokenInfo(String tempToken) {
        Claims claims = parseClaims(tempToken);
        TokenType tokenType = extractTokenType(claims);

        if (tokenType != TokenType.TEMP) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        return new TempTokenInfo(
                claims.getSubject(),
                VerificationProvider.valueOf(claims.get(JwtClaims.OAUTH_PROVIDER, String.class)),
                claims.get(JwtClaims.EMAIL, String.class)
        );
    }

    @Override
    public String getRefreshTokenInfo(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        TokenType tokenType = TokenType.valueOf(claims.get(JwtClaims.TOKEN_TYPE, String.class));
        if (tokenType != TokenType.REFRESH) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
        return claims.getSubject();
    }

    private TokenType extractTokenType(Claims claims) {
        String type = claims.get(JwtClaims.TOKEN_TYPE, String.class);
        if (!StringUtils.hasText(type)) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
        return TokenType.valueOf(type);
    }

    private Claims parseClaims(String token) {

        if (!StringUtils.hasText(token)) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setClock(() -> Date.from(timeProvider.now().toInstant(ZoneOffset.UTC)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw AuthRuleException.of(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
    }

    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.toInstant(ZoneOffset.UTC));
    }
}
