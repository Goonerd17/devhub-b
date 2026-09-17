package teamdevhub.devhub.shared.security;

/** Minimal security principal contract consumed by business adapters. */
public interface CurrentUserPrincipal {
    String userGuid();
    String roleName();
}
