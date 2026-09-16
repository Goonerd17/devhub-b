package teamdevhub.devhub.member.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.identity.core.user.application.service.UserSignupService;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserPositionRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserSkillRepository;
import teamdevhub.devhub.member.api.MemberRegistrationUseCase;
import teamdevhub.devhub.member.core.user.application.service.MemberRegistrationService;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupServiceTest {

    private UserSignupService userSignupService;

    private FakeUserRepository userRepository;
    private FakeUserPositionRepository userPositionRepository;
    private FakeUserSkillRepository userSkillRepository;

    @BeforeEach
    void init() {
        userRepository = new FakeUserRepository();
        userPositionRepository = new FakeUserPositionRepository();
        userSkillRepository = new FakeUserSkillRepository();

        MemberRegistrationUseCase registration = new MemberRegistrationService(userRepository, userPositionRepository, userSkillRepository);
        userSignupService = new UserSignupService(registration, new FakeUuidIdentifierProvider(TEST_USER_GUID_1));
    }

    @Test
    @DisplayName("愿由ъ옄_怨꾩젙???앹꽦?쒕떎")
    void createAdminAccount() {
        // given
        MemberRegistrationUseCase registration = new MemberRegistrationService(userRepository, userPositionRepository, userSkillRepository);
        userSignupService = new UserSignupService(registration, new FakeUuidIdentifierProvider(ADMIN_USER_GUID_1));

        SignupAdminCommand signupAdminCommand = new SignupAdminCommand(null, ADMIN_EMAIL_1, ADMIN_PASSWORD_1, ADMIN_USERNAME_1, "", List.of(), List.of(), null);
        // when
        userSignupService.initializeAdminUser(signupAdminCommand);

        // then
        assertThat(userRepository.findByUserGuid(ADMIN_USER_GUID_1).getUserGuid()).isEqualTo(ADMIN_USER_GUID_1);
        assertThat(userRepository.findByUserGuid(ADMIN_USER_GUID_1).getUserRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("?뚯썝媛???깃났???좎?_?ъ??섍낵_?ㅽ궗????λ맂??")
    void signupStoresPositionsAndSkills() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                TEST_TERMS_AGREEMENT_LIST,
                VERIFICATION_TARGET_1
        );

        // when
        userSignupService.saveEmailUserInfo(signupUserCommand, TEST_USER_GUID_1);
        User persistedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);
        Set<UserPosition> positions = userPositionRepository.findByUserGuid(TEST_USER_GUID_1);
        Set<UserSkill> skills = userSkillRepository.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(persistedUser).isNotNull();
        assertThat(positions).extracting(UserPosition::positionCd).containsExactlyInAnyOrderElementsOf(TEST_POSITION_LIST);
        assertThat(skills).extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrderElementsOf(TEST_SKILL_LIST);
    }

    @Test
    @DisplayName("OAuth_?뚯썝媛?낆씠_?깃났?섎㈃_?ъ??섍낵_?ㅽ궗????λ맂??")
    void signupWithOAuthStoresPositionsAndSkills() {
        SignupOAuthUserCommand signupOAuthUserCommand = new SignupOAuthUserCommand(
                TEMP_TOKEN,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                TEST_TERMS_AGREEMENT_LIST
        );

        userSignupService.saveOAuthUserInfo(signupOAuthUserCommand, TEST_USER_GUID_1);

        Set<UserPosition> positions = userPositionRepository.findByUserGuid(TEST_USER_GUID_1);
        Set<UserSkill> skills = userSkillRepository.findByUserGuid(TEST_USER_GUID_1);
        User persistedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);

        assertThat(positions).extracting(UserPosition::positionCd).containsExactlyInAnyOrderElementsOf(TEST_POSITION_LIST);
        assertThat(skills).extracting(UserSkill::skillCd).containsExactlyInAnyOrderElementsOf(TEST_SKILL_LIST);
        assertThat(persistedUser).isNotNull();
        assertThat(persistedUser.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("?뚯썝媛????濡쒓렇???섏?_?딆?_?ъ슜?먯쓽_理쒖쥌_濡쒓렇???쇱떆??議댁옱?섏?_?딅뒗??")
    void haveNoLastLoginDateForUserWhoHasNotLoggedInAfterSignup() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(UNVERIFIED_EMAIL, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, TEST_TERMS_AGREEMENT_LIST, VERIFICATION_TARGET_1);

        // when
        userSignupService.saveEmailUserInfo(signupUserCommand, TEST_USER_GUID_1);

        // then
        assertThat(userRepository.wasCalled("updateLastLoginDateTime")).isFalse();
    }
}
