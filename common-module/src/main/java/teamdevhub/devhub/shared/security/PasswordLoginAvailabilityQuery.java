package teamdevhub.devhub.shared.security;

/** 회원의 비밀번호 로그인 가능 여부 조회 계약. */
public interface PasswordLoginAvailabilityQuery {
    boolean isPasswordLoginAvailable(String memberGuid);
}
