package teamdevhub.devhub.member.api;

/**
 * Identity가 인증 성공 시 Member에 요청할 수 있는 최소 lifecycle capability다.
 */
public interface MemberLoginActivityUseCase {

    void assertMemberCanLogIn(String memberGuid);

    void recordSuccessfulLogin(String memberGuid);
}
