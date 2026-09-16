package teamdevhub.devhub.fake.framework;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import teamdevhub.devhub.identity.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;

import java.util.Collections;

public class FakeAuthenticationManager implements AuthenticationManager {

    private final AuthenticatedUser authenticatedUser;

    public FakeAuthenticationManager(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return new UsernamePasswordAuthenticationToken(
                new UserAuthentication(authenticatedUser),
                authentication.getCredentials(),
                Collections.emptyList()
        );
    }
}
