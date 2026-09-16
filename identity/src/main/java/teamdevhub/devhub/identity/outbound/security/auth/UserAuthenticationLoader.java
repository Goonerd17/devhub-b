package teamdevhub.devhub.identity.outbound.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.identity.core.auth.port.out.EmailUserCredentialRepository;

@Service
@RequiredArgsConstructor
public class UserAuthenticationLoader implements UserDetailsService {

    private final EmailUserCredentialRepository emailUserCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        EmailUserCredential emailUserCredential =
                emailUserCredentialRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(
                emailUserCredential.getUserGuid(),
                emailUserCredential.getEmail(),
                emailUserCredential.getUserRole()
        );

        return new UserAuthentication(authenticatedUser);
    }
}
