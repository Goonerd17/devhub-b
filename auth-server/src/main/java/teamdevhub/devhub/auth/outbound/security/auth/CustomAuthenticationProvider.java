package teamdevhub.devhub.auth.outbound.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.auth.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final EmailUserCredentialRepository emailUserCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String email = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        EmailUserCredential emailUserCredential = emailUserCredentialRepository.findByEmail(email).orElseThrow();

        if (!passwordEncoder.matches(rawPassword, emailUserCredential.getPassword())) {
            throw new BadCredentialsException("invalid password");
        }

        AuthenticatedUser user = AuthenticatedUser.of(
                emailUserCredential.getUserGuid(),
                emailUserCredential.getEmail(),
                emailUserCredential.getUserRole()
        );

        return new UsernamePasswordAuthenticationToken(
                new UserAuthentication(user),
                null,
                List.of(new SimpleGrantedAuthority(user.userRole().getAuthority()))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
