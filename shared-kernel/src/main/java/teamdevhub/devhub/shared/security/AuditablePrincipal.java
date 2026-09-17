package teamdevhub.devhub.shared.security;

/** Technical contract for principals that expose an audit identifier. */
public interface AuditablePrincipal {
    String auditIdentifier();
}
