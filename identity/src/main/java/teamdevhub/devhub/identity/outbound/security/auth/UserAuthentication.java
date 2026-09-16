package teamdevhub.devhub.identity.outbound.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;

import java.util.Collection;
import java.util.List;

public class UserAuthentication implements UserDetails, teamdevhub.devhub.identity.api.AuthenticatedUserCarrier {

    private final AuthenticatedUser authenticatedUser;

    public UserAuthentication(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public AuthenticatedUser getUser() {
        return authenticatedUser;
    }

    @Override
    public AuthenticatedUser authenticatedUser() {
        return authenticatedUser;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return authenticatedUser.userGuid();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authenticatedUser.userRole().getAuthority()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserGuid() {
        return authenticatedUser.userGuid();
    }
}
