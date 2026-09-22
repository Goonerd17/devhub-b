package teamdevhub.devhub.auth.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.auth.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.auth.outbound.auth.adapter.mapper.UserCredentialMapper;
import teamdevhub.devhub.auth.outbound.auth.persistence.JpaEmailCredentialRepository;
import teamdevhub.devhub.auth.outbound.auth.persistence.JpaOAuthCredentialRepository;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCredentialAdapter implements UserCredentialRepository {

    private final JpaEmailCredentialRepository jpaEmailCredentialRepository;
    private final JpaOAuthCredentialRepository jpaOAuthCredentialRepository;

    @Override
    public Optional<AuthenticatedUser> findUserCredentialByUserGuid(String userGuid) {
        return jpaEmailCredentialRepository.findByUserGuid(userGuid)
                .map(UserCredentialMapper::toAuthenticatedUser)
                .or(() -> jpaOAuthCredentialRepository.findByUserGuid(userGuid)
                        .map(UserCredentialMapper::toAuthenticatedUser));
    }

    @Override
    public Optional<AuthenticatedUser> findEmailUserCredentialByEmail(String email) {
        return jpaEmailCredentialRepository.findByEmail(email)
                .map(UserCredentialMapper::toAuthenticatedUser);
    }

    @Override
    public Optional<AuthenticatedUser> findOAuthUserCredentialByOAuth(VerificationProvider verificationProvider, String oauthId) {
        return jpaOAuthCredentialRepository.findByProviderAndOauthId(verificationProvider, oauthId)
                .map(UserCredentialMapper::toAuthenticatedUser);
    }

    @Override
    public void saveEmailUserCredential(AuthenticatedUser authenticatedUser, String encryptedPassword) {
        jpaEmailCredentialRepository.save(
                UserCredentialMapper.toEmailCredentialEntity(authenticatedUser, encryptedPassword)
        );
    }

    @Override
    public void saveOAuthUserCredential(AuthenticatedUser authenticatedUser, VerificationProvider verificationProvider, String oauthId) {
        jpaOAuthCredentialRepository.save(
                UserCredentialMapper.toOAuthCredentialEntity(authenticatedUser, verificationProvider, oauthId)
        );
    }

    @Override
    public EmailUserCredential findEmailCredentialByUserGuid(String userGuid) {
        EmailCredentialEntity emailCredentialEntity = jpaEmailCredentialRepository.findByUserGuid(userGuid)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));

        return UserCredentialMapper.toEmailUserCredential(emailCredentialEntity);
    }

    @Override
    public void savePassword(EmailUserCredential emailUserCredential) {
        EmailCredentialEntity emailCredentialEntity = UserCredentialMapper.toEmailCredentialEntity(emailUserCredential);
        jpaEmailCredentialRepository.save(emailCredentialEntity);
    }
}
