package teamdevhub.devhub.shared.security;

import java.util.Optional;

/** 회원 이메일 조회 계약. */
public interface MemberEmailQuery {
    Optional<String> findEmail(String memberGuid);
}
