package teamdevhub.devhub.identity.core.user.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;

import java.util.List;

@Builder
public record SignupAdminCommand(String userGuid, String email, String password, String username, String introduction,
                                 List<String> positionList, List<String> skillList,
                                 VerificationTarget verificationTarget) {

}
