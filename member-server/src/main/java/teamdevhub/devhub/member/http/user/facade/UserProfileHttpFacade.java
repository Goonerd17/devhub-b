package teamdevhub.devhub.member.http.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserProfileUseCase;
import teamdevhub.devhub.member.http.user.model.UserBasicResponseDto;
import teamdevhub.devhub.member.http.user.model.UserDetailResponseDto;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileImageCommand;
import teamdevhub.devhub.shared.security.PasswordLoginAvailabilityQuery;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileHttpFacade {
    private final UserProfileUseCase userProfileUseCase;
    private final ObjectProvider<PasswordLoginAvailabilityQuery> passwordLoginAvailabilityQuery;

    public UserBasicResponseDto getUserInfo(String userGuid) {
        return UserBasicResponseDto.fromDomain(userProfileUseCase.getUserInfo(userGuid));
    }

    public UserDetailResponseDto getProfile(String userGuid) {
        User user = userProfileUseCase.getCurrentUserProfile(userGuid);
        PasswordLoginAvailabilityQuery query = passwordLoginAvailabilityQuery.getIfAvailable();
        return UserDetailResponseDto.fromDomain(user, query != null && query.isPasswordLoginAvailable(userGuid));
    }

    public void updateProfileImage(UpdateProfileImageCommand command) { userProfileUseCase.updateProfileImage(command); }
    public void updateProfile(UpdateProfileCommand command) { userProfileUseCase.updateProfile(command); }
}
