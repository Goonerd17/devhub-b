package teamdevhub.devhub.member.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPositionChangeResult;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkillChangeResult;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileImageCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserProfileUseCase;
import teamdevhub.devhub.member.core.user.port.out.UserPositionRepository;
import teamdevhub.devhub.member.core.user.port.out.UserRepository;
import teamdevhub.devhub.member.core.user.port.out.UserSkillRepository;
import teamdevhub.devhub.member.api.profile.MemberPublicProfile;
import teamdevhub.devhub.member.api.profile.MemberPublicProfileQuery;
import teamdevhub.devhub.member.api.profile.MemberCommunityProfile;
import teamdevhub.devhub.member.api.profile.MemberCommunityProfileQuery;
import teamdevhub.devhub.member.api.profile.MemberApplicationProfile;
import teamdevhub.devhub.member.api.profile.MemberApplicationProfileQuery;
import teamdevhub.devhub.member.api.profile.MemberProjectOwner;
import teamdevhub.devhub.member.api.profile.MemberProjectOwnerQuery;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileService implements UserProfileUseCase, MemberPublicProfileQuery, MemberCommunityProfileQuery, MemberApplicationProfileQuery, MemberProjectOwnerQuery {

    @Override
    public MemberProjectOwner findProjectOwner(String memberGuid) {
        User user = getUserInfo(memberGuid);
        return new MemberProjectOwner(user.getUserGuid(), user.getUsername(), user.getFileGuid());
    }

    private final UserRepository userRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserSkillRepository userSkillRepository;

    @Override
    @Transactional(readOnly = true)
    public java.util.Map<String, MemberApplicationProfile> findApplicationProfiles(List<String> memberGuids) {
        return memberGuids.stream().distinct().collect(java.util.stream.Collectors.toMap(guid -> guid, guid -> {
            User user = userRepository.findByUserGuid(guid);
            List<String> skills = userSkillRepository.findByUserGuid(guid).stream()
                    .map(UserSkill::skillCd).toList();
            return new MemberApplicationProfile(guid, user.getUsername(), user.getIntroduction(), user.getMannerDegree(), skills);
        }));
    }

    @Override
    public User getUserInfo(String userGuid) {
        return userRepository.findByUserGuid(userGuid);
    }

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return getUserWithPositionsAndSkills(userGuid);
    }

    @Override
    public void updateProfileImage(UpdateProfileImageCommand updateProfileImageCommand) {
        User user = userRepository.findByUserGuid(updateProfileImageCommand.userGuid());
        user.updateProfileImage(updateProfileImageCommand);
        userRepository.save(user);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = getUserWithPositionsAndSkills(updateProfileCommand.userGuid());

        if (updateProfileCommand.hasUsernameAndIntroductionChange()) {
            UpdateUserCommand updateUserCommand = new UpdateUserCommand(updateProfileCommand.username(), updateProfileCommand.introduction());
            user.updateBasicProfile(updateUserCommand);
            userRepository.updateUserProfile(user);
        }

        if (updateProfileCommand.hasPositionsChange()) {
            replacePositions(user, updateProfileCommand.positions());
        }

        if (updateProfileCommand.hasSkillsChange()) {
            replaceSkills(user, updateProfileCommand.skills());
        }
    }

    @Override
    public void updateUserMannerDegree(String revieweeGuid, double reviewScore) {
        User user = userRepository.findByUserGuid(revieweeGuid);
        user.applyReviewScore(reviewScore);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberPublicProfile> findPublicProfilesByMemberGuids(List<String> memberGuids) {
        return userRepository.findNamesByUserGuid(memberGuids).entrySet().stream()
                .map(entry -> new MemberPublicProfile(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberCommunityProfile> findCommunityProfiles(List<String> memberGuids) {
        return memberGuids.stream()
                .map(userRepository::findByUserGuid)
                .map(user -> new MemberCommunityProfile(user.getUserGuid(), user.getUsername(), user.getFileGuid()))
                .toList();
    }

    private User getUserWithPositionsAndSkills(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        Set<UserPosition> userPositions = userPositionRepository.findByUserGuid(userGuid);
        Set<UserSkill> userSkills = userSkillRepository.findByUserGuid(userGuid);
        user.loadPositionsAndSkills(userPositions, userSkills);
        return user;
    }

    private void replacePositions(User user, Set<UserPosition> positions) {
        UserPositionChangeResult result = user.changePositions(positions);
        if (result.changed()) {
            userPositionRepository.replace(result.previousPositions(), result.changedPositions());
        }
    }

    private void replaceSkills(User user, Set<UserSkill> skills) {
        UserSkillChangeResult result = user.changeSkills(skills);
        if (result.changed()) {
            userSkillRepository.replace(result.previousSkills(), result.changedSkills());
        }
    }
}
