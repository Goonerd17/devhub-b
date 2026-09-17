package teamdevhub.devhub.member.api;

public enum MemberRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private final String authority;

    MemberRole(String authority) { this.authority = authority; }

    public String getAuthority() { return authority; }
}
