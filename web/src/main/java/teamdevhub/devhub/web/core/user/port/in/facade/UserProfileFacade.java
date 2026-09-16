package teamdevhub.devhub.web.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.api.credential.PasswordLoginAvailabilityQuery;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileImageCommand;
import teamdevhub.devhub.web.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.web.core.user.port.in.facade.model.UserDetailResponseDto;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserProfileUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileFacade {

    private final UserProfileUseCase userProfileUseCase;
    private final PasswordLoginAvailabilityQuery passwordLoginAvailabilityQuery;

    public UserBasicResponseDto getUserInfo(String userGuid) {
        User user = userProfileUseCase.getUserInfo(userGuid);
        return UserBasicResponseDto.fromDomain(user);
    }

    public UserDetailResponseDto getCurrentUserProfile(String userGuid) {
        User user = userProfileUseCase.getCurrentUserProfile(userGuid);
        // OAuth 전용 가입자는 EMAIL_USER_CREDENTIAL 행이 없다 - 이 존재 여부로 비밀번호 로그인 가능 여부를 판단한다.
        boolean passwordLoginAvailable = passwordLoginAvailabilityQuery.isPasswordLoginAvailable(userGuid);
        return UserDetailResponseDto.fromDomain(user, passwordLoginAvailable);
    }

    public void updateProfileImage(UpdateProfileImageCommand updateProfileImageCommand) {
        userProfileUseCase.updateProfileImage(updateProfileImageCommand);
    }

    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        userProfileUseCase.updateProfile(updateProfileCommand);
    }
}
