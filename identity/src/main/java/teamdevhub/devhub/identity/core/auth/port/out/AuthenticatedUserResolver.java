package teamdevhub.devhub.identity.core.auth.port.out;

import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;

public interface AuthenticatedUserResolver {

    AuthenticatedUser getAuthenticatedUser(String email, String rawPassword);
}
