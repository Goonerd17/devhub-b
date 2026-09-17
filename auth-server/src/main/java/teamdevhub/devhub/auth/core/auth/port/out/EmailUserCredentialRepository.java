package teamdevhub.devhub.auth.core.auth.port.out;

import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;

import java.util.Optional;

public interface EmailUserCredentialRepository {

    Optional<EmailUserCredential> findByEmail(String email);
    Optional<EmailUserCredential> findByUserGuid(String userGuid);
}
