package teamdevhub.devhub.auth.core.auth.port.out;

import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;

public interface AuthenticatedUserResolver {

    AuthenticatedUser getAuthenticatedUser(String email, String rawPassword);
}
