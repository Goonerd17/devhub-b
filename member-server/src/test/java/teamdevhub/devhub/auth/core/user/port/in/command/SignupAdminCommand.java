package teamdevhub.devhub.auth.core.user.port.in.command;

import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import java.util.List;

public record SignupAdminCommand(String userGuid, String email, String password, String username,
        String introduction, List<String> positionList, List<String> skillList, VerificationTarget verificationTarget) {
    public static Builder builder() { return new Builder(); }
    public static final class Builder {
        private String userGuid, email, password, username, introduction;
        private List<String> positionList, skillList;
        private VerificationTarget verificationTarget;
        public Builder userGuid(String v) { userGuid = v; return this; }
        public Builder email(String v) { email = v; return this; }
        public Builder password(String v) { password = v; return this; }
        public Builder username(String v) { username = v; return this; }
        public Builder introduction(String v) { introduction = v; return this; }
        public Builder positionList(List<String> v) { positionList = v; return this; }
        public Builder skillList(List<String> v) { skillList = v; return this; }
        public Builder verificationTarget(VerificationTarget v) { verificationTarget = v; return this; }
        public SignupAdminCommand build() { return new SignupAdminCommand(userGuid, email, password, username,
                introduction, positionList, skillList, verificationTarget); }
    }
}
