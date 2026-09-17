package teamdevhub.devhub.member.core.user.port.in.usecase;

public interface UserLoginUseCase {

    void updateLastLoginDateTime(String userGuid);
    void validateLoginUser(String userGuid);
}
