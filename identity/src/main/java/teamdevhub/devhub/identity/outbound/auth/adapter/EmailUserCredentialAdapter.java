package teamdevhub.devhub.identity.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.identity.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.identity.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.identity.outbound.auth.adapter.mapper.UserCredentialMapper;
import teamdevhub.devhub.identity.outbound.auth.persistence.JpaEmailCredentialRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailUserCredentialAdapter implements EmailUserCredentialRepository {

    private final JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @Override
    public Optional<EmailUserCredential> findByEmail(String email) {
        return jpaEmailCredentialRepository.findByEmail(email)
                .map(UserCredentialMapper::toEmailUserCredential);
    }

    @Override
    public Optional<EmailUserCredential> findByUserGuid(String userGuid) {
        return jpaEmailCredentialRepository.findByUserGuid(userGuid)
                .map(UserCredentialMapper::toEmailUserCredential);
    }
}
