package teamdevhub.devhub.auth.core.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.auth.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.auth.api.credential.MemberEmailQuery;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberEmailService implements MemberEmailQuery, teamdevhub.devhub.shared.security.MemberEmailQuery {
    private final EmailUserCredentialRepository emailUserCredentialRepository;

    @Override
    public Optional<String> findEmail(String memberGuid) {
        return emailUserCredentialRepository.findByUserGuid(memberGuid)
                .map(credential -> credential.getEmail());
    }
}
