package teamdevhub.devhub.constant;

import teamdevhub.devhub.shared.terms.TermsAgreementItem;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;

import java.time.LocalDateTime;
import java.util.List;

public final class UserTestConstant {

    private UserTestConstant() {}

    public static final String ADMIN_USER_GUID_1 = "1ADMNa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String ADMIN_EMAIL_1 = "admin1@example.com";
    public static final String ADMIN_USERNAME_1 = "AdminUser1";
    public static final String ADMIN_PASSWORD_1 = "adminPassword123!";

    public static final String ACCESS_TOKEN = "access-token-1";
    public static final String REFRESH_TOKEN = "refresh-token-1";
    public static final String TEMP_TOKEN = "temp-token-1";
    public static final String TEST_OAUTH_ID_1 = "testOAuthId";

    public static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_EMAIL_1 = "user1@example.com";
    public static final String TEST_PASSWORD_1 = "password123!";
    public static final String TEST_USERNAME_1 = "User1";
    public static final String TEST_INTRO_1 = "Hello World";

    public static final String TEST_USER_GUID_2 = "USER2a1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_EMAIL_2 = "user2@example.com";
    public static final String TEST_PASSWORD_2 = "password456!";
    public static final String TEST_USERNAME_2 = "User2";
    public static final String TEST_INTRO_2 = "Goodbye World";

    public static final double TEST_MANNER = 36.5;
    public static final boolean TEST_BLOCKED = false;
    public static final boolean TEST_DELETED = false;
    public static final LocalDateTime TEST_LAST_LOGIN = LocalDateTime.of(2025, 1, 1, 12, 0);

    public static final String TEST_USER_POSITION_GUID = "POSa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_USER_SKILL_GUID = "SKLa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_POSITION_CD = "001";
    public static final String TEST_SKILL_CD = "001";

    public static final List<String> TEST_POSITION_LIST = List.of("001");
    public static final List<String> TEST_SKILL_LIST = List.of("001");


    public static final String TEST_EMAIL_CODE = "123456";
    public static final String TEST_EMAIL = "test@test.com";
    public static final String UNVERIFIED_EMAIL = "unverified@example.com";

    public static final String NEW_POSITION_CD = "002";
    public static final String NEW_SKILL_CD = "002";

    public static final String NEW_USERNAME = "NewUsername";
    public static final String NEW_INTRO = "NewIntro";

    public static final List<String> NEW_POSITION_LIST = List.of("002");
    public static final List<String> NEW_SKILL_LIST = List.of("002");


    public static final VerificationTarget VERIFICATION_TARGET_1 = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
    public static final VerificationTarget VERIFICATION_TARGET_2 = VerificationTarget.of(VerificationType.EMAIL, UNVERIFIED_EMAIL);

    public static final String TEST_TERMS_GUID_1 = "TERMS1a1b2c3d4e5f6g7h8i9j10k11l12";

    public static final List<TermsAgreementItem> TEST_TERMS_AGREEMENT_LIST = List.of(new TermsAgreementItem(TEST_TERMS_GUID_1, true));

    public static final String TEST_PROJECT_GUID_1 = "PRJT1a1b2c3d4e5f6g7h8i9j10k11l1";
    public static final String TEST_REVIEW_GUID_1 = "RVEW1a1b2c3d4e5f6g7h8i9j10k11l1";

    public static final String TEST_REPORT_GUID_1 = "RPT1a1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_BOARD_GUID_1 = "BRD1a1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_NEW_PASSWORD = "NewPass1!";
    public static final LocalDateTime TEST_BLOCK_END_DATE = LocalDateTime.of(2025, 12, 31, 23, 59, 59);

}
