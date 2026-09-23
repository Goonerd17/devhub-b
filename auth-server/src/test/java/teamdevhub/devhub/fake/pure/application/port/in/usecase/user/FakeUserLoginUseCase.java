package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.port.out.AuthMemberPort;
import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;

import java.util.HashSet;
import java.util.Set;

public class FakeUserLoginUseCase implements AuthMemberPort {
    private final Set<String> withdrawnUsers = new HashSet<>();
    private final Set<String> updatedLoginUsers = new HashSet<>();

    @Override
    public void register(AuthMemberRegistration registration) {
    }

    @Override
    public boolean adminExists() {
        return false;
    }

    @Override
    public void assertCanLogIn(String memberGuid) {
        if (withdrawnUsers.contains(memberGuid)) {
            throw DomainRuleException.of(ErrorCode.USER_WITHDRAWN);
        }
    }

    @Override
    public void recordSuccessfulLogin(String memberGuid) {
        updatedLoginUsers.add(memberGuid);
    }

    @Override
    public MemberRole findCurrentRole(String memberGuid) {
        return MemberRole.USER;
    }

    public boolean isLoginTimeUpdated(String userGuid) {
        return updatedLoginUsers.contains(userGuid);
    }

    public void givenWithdrawnUser(String userGuid) {
        withdrawnUsers.add(userGuid);
    }
}
