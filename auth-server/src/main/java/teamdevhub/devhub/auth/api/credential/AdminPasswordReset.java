package teamdevhub.devhub.auth.api.credential;

/** 관리자 기능에서 사용자 비밀번호를 초기화할 때 사용하는 최소 계약. */
public interface AdminPasswordReset {
    void reset(String userGuid, String newPassword);
}
