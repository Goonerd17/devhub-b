package teamdevhub.devhub.auth.outbound.member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.member.AuthMemberGateway;
import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;

@Component
public class AuthMemberHttpClient implements AuthMemberGateway {
    private final RestClient client;

    public AuthMemberHttpClient(RestClient.Builder builder,
            @Value("${services.member.base-url:http://member-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public void register(AuthMemberRegistration registration) {
        client.post().uri("/internal/auth/members").body(registration).retrieve().toBodilessEntity();
    }
    public boolean adminExists() {
        Boolean result = client.get().uri("/internal/auth/members/admin/exists").retrieve().body(Boolean.class);
        return Boolean.TRUE.equals(result);
    }
    public void assertCanLogIn(String memberGuid) {
        client.post().uri("/internal/auth/members/{memberGuid}/login/assert", memberGuid).retrieve().toBodilessEntity();
    }
    public void recordSuccessfulLogin(String memberGuid) {
        client.post().uri("/internal/auth/members/{memberGuid}/login/success", memberGuid).retrieve().toBodilessEntity();
    }
    public MemberRole findCurrentRole(String memberGuid) {
        return client.get().uri("/internal/auth/members/{memberGuid}/role", memberGuid).retrieve().body(MemberRole.class);
    }
}
