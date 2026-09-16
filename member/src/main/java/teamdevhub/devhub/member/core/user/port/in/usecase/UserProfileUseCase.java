package teamdevhub.devhub.member.core.user.port.in.usecase;

import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileImageCommand;

public interface UserProfileUseCase {

    User getUserInfo(String userGuid);
    User getCurrentUserProfile(String userGuid);
    void updateProfileImage(UpdateProfileImageCommand updateProfileImageCommand);
    void updateProfile(UpdateProfileCommand updateProfileCommand);
    void updateUserMannerDegree(String revieweeGuid, double reviewScore);
}