package teamdevhub.devhub.shared.internal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public final class InternalApiAuthenticationFilter extends OncePerRequestFilter {
    public static final String HEADER_NAME = "X-Internal-Api-Key";
    private static final String PRINCIPAL = "internal-service";

    private final byte[] expectedKey;

    public InternalApiAuthenticationFilter(String expectedKey) {
        if (expectedKey == null || expectedKey.isBlank()) {
            throw new IllegalArgumentException("internal.api-key must not be blank");
        }
        this.expectedKey = expectedKey.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String providedKey = request.getHeader(HEADER_NAME);
        if (providedKey == null || !MessageDigest.isEqual(expectedKey, providedKey.getBytes(StandardCharsets.UTF_8))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal API key");
            return;
        }

        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                PRINCIPAL, null, List.of(new SimpleGrantedAuthority("ROLE_INTERNAL_SERVICE")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
