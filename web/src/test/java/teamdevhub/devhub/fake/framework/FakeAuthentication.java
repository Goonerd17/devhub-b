package teamdevhub.devhub.fake.framework;

import org.springframework.security.core.Authentication;

public class FakeAuthentication implements Authentication {

    private final Object principal;

    public FakeAuthentication(Object principal) {
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public boolean isAuthenticated() { return true; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}

    @Override
    public String getName() { return null; }

    @Override
    public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() { return java.util.List.of(); }
}