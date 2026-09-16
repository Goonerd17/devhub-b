package teamdevhub.devhub.member.core.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import static teamdevhub.devhub.constant.UserTestConstant.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserActiveAndReviewTest {


    private User buildGeneralUser(String userGuid) {
        SignupUserCommand command = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST).verificationTarget(VERIFICATION_TARGET_1).build();
        return User.createGeneralUser(new CreateUserCommand(userGuid, command.username(), command.introduction(), command.positionList(), command.skillList()));
    }

    private User buildBlockedUser(String userGuid) {
        return User.of(userGuid, UserRole.USER, TEST_USERNAME_1, TEST_INTRO_1,
                null, 36.5, true, TEST_BLOCK_END_DATE, false, LocalDateTime.now(), AuditInfo.empty());
    }

    private User buildDeletedUser(String userGuid) {
        User user = buildGeneralUser(userGuid);
        user.withdraw();
        return user;
    }

    // --- assertActive ---

    @Test
    @DisplayName("?쒖꽦_?곹깭???ъ슜?먮뒗_assertActive_瑜??몄텧?대룄_?덉쇅媛_諛쒖깮?섏?_?딅뒗??")
    void assertActive_activeUser_noException() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);

        // when, then ??no exception
        user.assertActive();
    }

    @Test
    @DisplayName("?덊눜???ъ슜?먮뒗_assertActive_瑜??몄텧?섎㈃_USER_WITHDRAWN_?덉쇅媛_諛쒖깮?쒕떎")
    void assertActive_withdrawnUser_throwsDomainRuleException() {
        // given
        User user = buildDeletedUser(TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(user::assertActive)
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_WITHDRAWN.getMessage());
    }

    @Test
    @DisplayName("?뺤????ъ슜?먮뒗_assertActive_瑜??몄텧?섎㈃_USER_BLOCKED_?덉쇅媛_諛쒖깮?쒕떎")
    void assertActive_blockedUser_throwsDomainRuleException() {
        // given
        User user = buildBlockedUser(TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(user::assertActive)
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_BLOCKED.getMessage());
    }

    // --- applyReviewScore ---

    @Test
    @DisplayName("由щ럭_?먯닔_3_0???곸슜?섎㈃_留ㅻ꼫?꾧?_蹂?섏?_?딅뒗??")
    void applyReviewScore_score3_mannerUnchanged() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(3.0);

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner);
    }

    @Test
    @DisplayName("由щ럭_?먯닔_5_0???곸슜?섎㈃_留ㅻ꼫?꾧?_2_0_利앷??쒕떎")
    void applyReviewScore_score5_increasesManner() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(5.0);

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner + 2.0);
    }

    @Test
    @DisplayName("由щ럭_?먯닔_1_0???곸슜?섎㈃_留ㅻ꼫?꾧?_2_0_媛먯냼?쒕떎")
    void applyReviewScore_score1_decreasesManner() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(1.0);

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner - 2.0);
    }

    @Test
    @DisplayName("由щ럭_?먯닔瑜??щ윭踰??곸슜?섎㈃_?꾩쟻?쒕떎")
    void applyReviewScore_multipleTimes_accumulates() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(5.0); // +2.0
        user.applyReviewScore(1.0); // -2.0

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner);
    }

    @Test
    @DisplayName("由щ럭_?먯닔_4_5瑜??곸슜?섎㈃_留ㅻ꼫?꾧?_1_5_利앷??쒕떎")
    void applyReviewScore_score4_5_increasesManner() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(4.5);

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner + 1.5);
    }
}
