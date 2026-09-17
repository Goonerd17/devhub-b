package teamdevhub.devhub.auth.outbound.auth.infrastructure.token;

public enum TokenPrefix {

    BEARER("Bearer ");

    private final String value;

    TokenPrefix(String value) { this.value = value; }

    public String withToken(String token) {
        return value + token;
    }

    public boolean matches(String token) {
        return token != null && token.startsWith(value);
    }

    public String strip(String token) {
        return token.substring(value.length());
    }
}
