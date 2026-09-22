package teamdevhub.devhub.shared.security;

/** 관리자 기능에서 사용하는 비밀번호 초기화 계약. */
public interface AdminPasswordReset {
    void reset(String userGuid, String newPassword);
}
