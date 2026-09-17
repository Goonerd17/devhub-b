package teamdevhub.devhub.auth.core.auth.domain;

import lombok.Getter;
import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

@Getter
public class EmailUserCredential {

    private final String userGuid;
    private final String email;
    private String password;
    private final MemberRole userRole;

    private EmailUserCredential(
            String userGuid,
            String email,
            String password,
            MemberRole userRole
    ) {
        this.userGuid = userGuid;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public static EmailUserCredential of(
            String userGuid,
            String email,
            String password,
            MemberRole userRole
    ) {
        return new EmailUserCredential(
                userGuid,
                email,
                password,
                userRole
        );
    }

    public void verifyPassword(boolean matches) {
        if (!matches) {
            throw DomainRuleException.of(ErrorCode.USER_PASSWORD_FAIL);
        }
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
