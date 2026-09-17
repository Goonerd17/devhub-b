package teamdevhub.devhub.auth.core.auth.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.auth.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.auth.api.credential.PasswordLoginAvailabilityQuery;

@Service
@RequiredArgsConstructor
public class PasswordLoginAvailabilityService implements PasswordLoginAvailabilityQuery {

    private final EmailUserCredentialRepository emailUserCredentialRepository;

    @Override
    public boolean isPasswordLoginAvailable(String memberGuid) {
        return emailUserCredentialRepository.findByUserGuid(memberGuid).isPresent();
    }
}
