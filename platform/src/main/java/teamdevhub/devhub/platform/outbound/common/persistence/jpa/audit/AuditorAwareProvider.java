package teamdevhub.devhub.platform.outbound.common.persistence.jpa.audit;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.platform.security.AuditablePrincipal;

import java.util.Optional;

@Component("auditorAwareProvider")
public class AuditorAwareProvider implements AuditorAware<String> {

    public static final String SYSTEM = "system";
    public static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                ANONYMOUS_USER.equals(authentication.getName())) {
            return Optional.of(SYSTEM);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuditablePrincipal auditablePrincipal) {
            return Optional.of(auditablePrincipal.auditIdentifier());
        }

        if (principal instanceof UserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        }

        return Optional.of(SYSTEM);
    }
}
