package teamdevhub.devhub.auth.api.credential;

import java.util.Optional;

public interface MemberEmailQuery {
    Optional<String> findEmail(String memberGuid);
}
