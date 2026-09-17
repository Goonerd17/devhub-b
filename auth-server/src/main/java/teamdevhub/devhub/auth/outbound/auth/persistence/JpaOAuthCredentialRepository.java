package teamdevhub.devhub.auth.outbound.auth.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.auth.outbound.auth.adapter.entity.OAuthCredentialEntity;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import java.util.Optional;

public interface JpaOAuthCredentialRepository extends JpaRepository<OAuthCredentialEntity, String> {

    Optional<OAuthCredentialEntity> findByProviderAndOauthId(VerificationProvider provider, String oauthId);
    Optional<OAuthCredentialEntity> findByUserGuid(String userGuid);
}