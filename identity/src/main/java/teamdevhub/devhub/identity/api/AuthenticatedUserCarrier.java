package teamdevhub.devhub.identity.api;

/** Spring Security principal bridge exposed without leaking its adapter implementation. */
public interface AuthenticatedUserCarrier {
    AuthenticatedMember authenticatedUser();
}
