package teamdevhub.devhub.member.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.member.core.user.application.service.UserProfileService;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileImageCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserPositionRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserSkillRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserProfileServiceTest {

    private UserProfileService userProfileService;

    private FakeUserRepository userRepository;
    private FakeUserPositionRepository userPositionRepository;
    private FakeUserSkillRepository skillRepository;

    @BeforeEach
    void init() {

        userRepository = new FakeUserRepository();
        userPositionRepository = new FakeUserPositionRepository();
        skillRepository = new FakeUserSkillRepository();

        userProfileService = new UserProfileService(
                userRepository,
                userPositionRepository,
                skillRepository
        );
    }

    @Test
    @DisplayName("?ъ슜???뺣낫瑜?議고쉶?덉쓣_??紐⑤뱺_?뺣낫媛_議고쉶?쒕떎")
    void fetchAllUserInformation() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        UserPosition userPosition = new UserPosition(testUser.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        userPositionRepository.saveAll(userPositions);

        UserSkill userSkill = new UserSkill(testUser.getUserGuid(), TEST_SKILL_CD);
        Set<UserSkill> userSkills = Set.of(userSkill);
        skillRepository.saveAll(userSkills);

        // when, then
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid())).isNotNull();
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getUserGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getUsername()).isEqualTo(testUser.getUsername());
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getIntroduction()).isEqualTo(testUser.getIntroduction());
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getPositions()).isEqualTo(userPositions);
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getSkills()).isEqualTo(testUser.getSkills());
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???대?吏瑜??섏젙?섎㈃_?대떦_?대?吏濡?蹂寃쎈맂??")
    void updateProfileImageCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        CreateUserCommand generalCreateUserCommand =
                new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());

        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        String NEW_PROFILE_IMAGE_GUID = "NEW_PROFILE_IMAGE_GUID";

        UpdateProfileImageCommand updateProfileImageCommand = new UpdateProfileImageCommand(TEST_USER_GUID_1, NEW_PROFILE_IMAGE_GUID);

        // when
        userProfileService.updateProfileImage(updateProfileImageCommand);

        // then
        User updatedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);
        assertThat(updatedUser.getFileGuid()).isEqualTo(NEW_PROFILE_IMAGE_GUID);
    }

    @Test
    @DisplayName("湲곗〈_?꾨줈???대?吏媛_?덉쓣_???덈줈???대?吏濡???뼱?대떎")
    void overwriteProfileImage() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        CreateUserCommand generalCreateUserCommand =
                new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());

        User testUser = User.createGeneralUser(generalCreateUserCommand);
        testUser.updateProfileImage(new UpdateProfileImageCommand(TEST_USER_GUID_1, "OLD_IMAGE_GUID"));

        userRepository.save(testUser);
        UpdateProfileImageCommand updateProfileImageCommand = new UpdateProfileImageCommand(TEST_USER_GUID_1, "NEW_IMAGE_GUID");

        // when
        userProfileService.updateProfileImage(updateProfileImageCommand);

        // then
        User updatedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);
        assertThat(updatedUser.getFileGuid()).isEqualTo("NEW_IMAGE_GUID");
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_?ъ슜?먯쓽_?꾨줈???대?吏瑜??섏젙?섎㈃_?덉쇅媛_諛쒖깮?쒕떎")
    void updateProfileImageWithInvalidUser() {
        UpdateProfileImageCommand updateProfileImageCommand = new UpdateProfileImageCommand("NOT_EXIST_GUID", "IMAGE_GUID");

        assertThatThrownBy(() ->
                userProfileService.updateProfileImage(updateProfileImageCommand)
        ).isInstanceOf(AdapterDataException.class);
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_?됰꽕???먭린?뚭컻??媛믪씠_?녾굅??null_濡??ㅼ뼱?ㅻ㈃_?ъ슜?먭?_湲곕낯?곸쑝濡?媛吏怨??덈뒗_媛믪쑝濡??좎??쒕떎")
    void nullOrEmptyUsernameAndIntroductionKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,null,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).getUsername()).isEqualTo(testUser.getUsername());
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).getIntroduction()).isEqualTo(testUser.getIntroduction());
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_?됰꽕???먭린?뚭컻??蹂寃쎈맂_媛믪쑝濡??ㅼ뼱?ㅻ㈃_?대떦_媛믪쑝濡?蹂寃쎈맂??")
    void updateUsernameAndIntroductionCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, NEW_USERNAME,NEW_INTRO,null,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).getUsername()).isEqualTo(testUser.getUsername());
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).getIntroduction()).isEqualTo(testUser.getIntroduction());
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_愿?ы룷吏?섏씠_媛믪씠_?녾굅??null_濡??ㅼ뼱?ㅻ㈃_?ъ슜?먭?_湲곕낯?곸쑝濡?媛吏怨??덈뒗_媛믪쑝濡??좎??쒕떎")
    void nullOrEmptyUserPositionsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);

        UserPosition userPosition = new UserPosition(testUser.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        userPositionRepository.saveAll(userPositions);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,null,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_愿?ы룷吏?섏쓽_?ъ??섏퐫??媛믪씠_null_濡??ㅼ뼱?ㅻ㈃_蹂寃쎈릺吏_?딅뒗??")
    void nullPositionCodeWithUserPositionsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        Set<UserPosition> userPositions = new HashSet<>(Set.of(new UserPosition(testUser.getUserGuid(), null)));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, userPositions,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_愿?ы룷吏?섏쓽_?ъ??섏퐫??媛믪씠_鍮덇컪?쇰줈_?ㅼ뼱?ㅻ㈃_蹂寃쎈릺吏_?딅뒗??")
    void emptyPositionCodeWithUserPositionsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        Set<UserPosition> userPositions = new HashSet<>(Set.of(new UserPosition(testUser.getUserGuid(), "")));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,userPositions,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_愿?ы룷吏?섏씠_?숈씪??媛믪쑝濡??ㅼ뼱?ㅻ㈃_蹂寃쎈릺吏_?딅뒗??")
    void sameUserPositionsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,TEST_USER_POSITIONS,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_愿?ы룷吏?섏씠_?덈줈??媛??쇰줈_?ㅼ뼱?ㅻ㈃_?덈줈??媛믪쑝濡?蹂寃쎈맂??")
    void updateNewUserPositionCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, NEW_USER_POSITIONS,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(NEW_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_愿?ы룷吏?섏씠_湲곗〈_媛믨낵_?덈줈??媛??쇰줈_?ㅼ뼱?ㅻ㈃_?⑹퀜吏?媛믪쑝濡?蹂寃쎈맂??")
    void updateUserPositionWithExistAndNewCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UserPosition userPosition1 = new UserPosition(testUser.getUserGuid(), TEST_POSITION_CD);
        UserPosition userPosition2 = new UserPosition(testUser.getUserGuid(), NEW_POSITION_CD);
        Set<UserPosition> newUserPositions = Set.of(userPosition1,userPosition2);
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, newUserPositions,null);
        userProfileService.updateProfile(updateProfileCommand);
        Set<UserPosition> currentUserPositions = userPositionRepository.findByUserGuid(testUser.getUserGuid());

        // then
        assertThat(currentUserPositions)
                .hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, NEW_POSITION_CD);
        assertThat(userPositionRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_蹂댁쑀?ㅽ궗??媛믪씠_?녾굅??null_濡??ㅼ뼱?ㅻ㈃_?ъ슜?먭?_湲곕낯?곸쑝濡?媛吏怨??덈뒗_媛믪쑝濡??좎??쒕떎")
    void nullOrEmptyUserSkillsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_蹂댁쑀?ㅽ궗???ㅽ궗肄붾뱶_媛믪씠_null_濡??ㅼ뼱?ㅻ㈃_蹂寃쎈릺吏_?딅뒗??")
    void nullSkillCodeWithUserSkillsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        Set<UserSkill> userSkills = new HashSet<>(Set.of(new UserSkill(testUser.getUserGuid(), null)));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, userSkills);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_蹂댁쑀?ㅽ궗???ㅽ궗肄붾뱶_媛믪씠_鍮덇컪?쇰줈_?ㅼ뼱?ㅻ㈃_蹂寃쎈릺吏_?딅뒗??")
    void emptySkillCodeWithUserSkillsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        Set<UserSkill> userSkills = new HashSet<>(Set.of(new UserSkill(testUser.getUserGuid(), "")));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, userSkills);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_蹂댁쑀?ㅽ궗???숈씪??媛믪쑝濡??ㅼ뼱?ㅻ㈃_?ъ슜?먭?_湲곕낯?곸쑝濡?媛吏怨??덈뒗_媛믪쑝濡??좎??쒕떎")
    void sameUserSkillsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, TEST_USER_SKILLS);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_蹂댁쑀?ㅽ궗???덈줈??媛믪쑝濡??ㅼ뼱?ㅻ㈃_?덈줈??媛믪쑝濡?蹂寃쎈맂??")
    void updateNewUserSkillCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, NEW_USER_SKILLS);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(NEW_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???섏젙?먯꽌_愿?ъ뒪?ъ씠_湲곗〈_媛믨낵_?덈줈??媛믪쑝濡??ㅼ뼱?ㅻ㈃_?⑹퀜吏?媛믪쑝濡?蹂寃쎈맂??")
    void updateUserSkillWithExistAndNewCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UserSkill userSkill1 = new UserSkill(testUser.getUserGuid(), TEST_SKILL_CD);
        UserSkill userSkill2 = new UserSkill(testUser.getUserGuid(), NEW_SKILL_CD);
        Set<UserSkill> newUserSkills = Set.of(userSkill1, userSkill2);

        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, newUserSkills);
        userProfileService.updateProfile(updateProfileCommand);

        Set<UserSkill> currentUserSkills = skillRepository.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(currentUserSkills)
                .hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder(TEST_SKILL_CD, NEW_SKILL_CD);
        assertThat(skillRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("由щ럭_?먯닔濡?留ㅻ꼫?꾨?_?낅뜲?댄듃?섎㈃_??μ냼??諛섏쁺?쒕떎")
    void updateUserMannerDegree_validScore_delegatesToRepository() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        double reviewScore = 1.5;

        // when
        userProfileService.updateUserMannerDegree(TEST_USER_GUID_1, reviewScore);

        // then
        assertThat(userRepository.wasCalled("findByUserGuid")).isTrue();
        assertThat(userRepository.wasCalled("save")).isTrue();
        assertThat(userRepository.findByUserGuid(TEST_USER_GUID_1).getMannerDegree()).isEqualTo(36.5 + (reviewScore - 3));
    }

    @Test
    @DisplayName("?쇰컲_?ъ슜?먮뒗_USER_沅뚰븳??議댁옱?쒕떎")
    void haveUserRoleForGeneralUser() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);

        // when, then
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getUserRole()).isEqualTo(UserRole.USER);
    }
}
